/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.wine.registry;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.ParseException;

/***
 * Registry parser class
 * If someone wants to improve this code, feel free to do it!
 */
public class RegistryParser {
    private static final char QUOTE = '"';
    private final File registryFile;
    private final String rootName;
    enum ParseState {
        INITIAL,
        READING_NAME,
        SEPARATOR,
        READING_VALUE
    }
    public RegistryParser(File registryFile, String rootName) {
        this.registryFile = registryFile;
        this.rootName = rootName;
    }

    public RegistryKey parseFile() throws IOException, ParseException {
        BufferedReader bufferReader = new BufferedReader(new FileReader(registryFile));
        RegistryKey root = new RegistryKey(rootName);
        RegistryKey lastNode = null;
        String currentLine;
        int lineNumber = 1;
        do {
            currentLine = bufferReader.readLine();
            if(currentLine == null) {
                break;
            }

            if(currentLine.startsWith(";")
                    || currentLine.startsWith("#")
                    || StringUtils.isBlank(currentLine)
                    || currentLine.startsWith("@")) {
                lineNumber++;
                continue;
            }

            if(currentLine.startsWith("[")) {
                lastNode = this.parseDirectoryLine(root, currentLine);
            } else {
                if(lineNumber == 1) {
                    lineNumber++;
                    continue;
                } else if(lastNode == null) {
                    throw new ParseException(String.format("Invalid registry file. Error found line %s", lineNumber), 0);
                } else {
                    this.parseValueLine(lastNode, currentLine, lineNumber);
                }
            }
            lineNumber++;
        } while(currentLine != null);

        return root;
    }

    private void parseValueLine(RegistryKey lastNode, String currentLine, int lineNumber) throws ParseException {
        if(!currentLine.startsWith("\"")) {
            throw new ParseException(String.format("Invalid registry file. Error found line %s", lineNumber), 0);
        }

        ParseState parseState = ParseState.INITIAL;
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        Boolean ignoreNextQuote = false;

        for(int i = 0; i < currentLine.length(); i++) {
            char currentChar = currentLine.charAt(i);

            if(parseState == ParseState.INITIAL) {
                if(currentChar == QUOTE) {
                    parseState = ParseState.READING_NAME;
                }
            } else if(parseState == ParseState.READING_NAME) {
                if(currentChar == '"' && !ignoreNextQuote) {
                    parseState = ParseState.SEPARATOR;
                } else if(currentChar == '\\' && !ignoreNextQuote) {
                    ignoreNextQuote = true;
                } else {
                    nameBuilder.append(currentChar);
                    ignoreNextQuote = false;
                }
            } else if(parseState == ParseState.SEPARATOR) {
                if(currentChar != '=') {
                    throw new ParseException(String.format("Invalid registry file. Error found line %s", lineNumber), 0);
                } else {
                    parseState = ParseState.READING_VALUE;
                }
            } else if(parseState == ParseState.READING_VALUE) {
                valueBuilder.append(currentChar);
            }
        }

        String name = nameBuilder.toString();
        try {
            RegistryValue value = RegistryValue.fromString(name, valueBuilder.toString());
            lastNode.addChild(value);
        } catch(IllegalArgumentException e) {
            throw new ParseException(String.format("Error on line %s: %s", lineNumber, e), 0);
        }
    }

    private RegistryKey parseDirectoryLine(RegistryKey root, String currentLine) {
        String extractedLine = extractDirectoryLine(currentLine);
        String[] splitLine = extractedLine.split("\\\\\\\\");
        RegistryKey currentKey = root;
        for(String registryKeyName: splitLine) {
            RegistryKey childrenSearched = (RegistryKey) currentKey.getChild(registryKeyName);
            if(childrenSearched != null) {
                currentKey = childrenSearched;
            } else {
                RegistryKey newChild = new RegistryKey(registryKeyName);
                currentKey.addChild(newChild);
                currentKey = newChild;
            }
        }

        return currentKey;
    }

    private String extractDirectoryLine(String currentLine) {
        return currentLine.substring(1, currentLine.indexOf(']'));
    }
}
