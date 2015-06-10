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

package com.playonlinux.wine;

import java.io.*;
import java.text.ParseException;

/**
 * Represents a Windows shortcut (typically visible to Java only as a '.lnk' file).
 *
 * Retrieved 2011-09-23 from http://stackoverflow.com/questions/309495/windows-shortcut-lnk-parser-in-java/672775#672775
 * Originally called LnkParser
 *
 * Written by: (the stack overflow users, obviously!)
 *   Apache Commons VFS dependency removed by crysxd (why were we using that!?) https://github.com/crysxd
 *   Headerified, refactored and commented by Code Bling http://stackoverflow.com/users/675721/code-bling
 *   Network file support added by Stefan Cordes http://stackoverflow.com/users/81330/stefan-cordes
 *   Adapted by Sam Brightman http://stackoverflow.com/users/2492/sam-brightman
 *   Based on information in 'The Windows Shortcut File Format' by Jesse Hager &lt;jessehager@iname.com&gt;
 *   And somewhat based on code from the book 'Swing Hacks: Tips and Tools for Killer GUIs'
 *     by Joshua Marinacci and Chris Adamson
 *     ISBN: 0-596-00907-0
 *     http://www.oreilly.com/catalog/swinghks/
 */
public class WindowsShortcut
{
    private boolean isDirectory;
    private boolean isLocal;
    private String readFile;

    public WindowsShortcut(File file) throws IOException, ParseException {
        try (InputStream in = new FileInputStream(file)) {
            parseLink(getBytes(in));
        }
    }

    /**
     * Provides a quick test to see if this could be a valid link !
     * If you try to instantiate a new WindowShortcut and the link is not valid,
     * Exceptions may be thrown and Exceptions are extremely slow to generate,
     * therefore any code needing to loop through several files should first check this.
     *
     * @param file the potential link
     * @return true if may be a link, false otherwise
     * @throws IOException if an IOException is thrown while reading from the file
     */
    public static boolean isPotentialValidLink(File file) throws IOException {
        final int minimumLength = 0x64;
        boolean isPotentiallyValid;
        try (InputStream fis = new FileInputStream(file)) {
            isPotentiallyValid = file.isFile()
                    && file.getName().toLowerCase().endsWith(".lnk")
                    && fis.available() >= minimumLength
                    && isMagicPresent(getBytes(fis, 32));
        }
        return isPotentiallyValid;
    }

    /**
     * @return the name of the filesystem object pointed to by this shortcut
     */
    public String getRealFilename() {
        return readFile;
    }

    /**
     * Tests if the shortcut points to a local resource.
     * @return true if the 'local' bit is set in this shortcut, false otherwise
     */
    public boolean isLocal() {
        return isLocal;
    }

    /**
     * Tests if the shortcut points to a directory.
     * @return true if the 'directory' bit is set in this shortcut, false otherwise
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Gets all the bytes from an InputStream
     * @param in the InputStream from which to read bytes
     * @return array of all the bytes contained in 'in'
     * @throws IOException if an IOException is encountered while reading the data from the InputStream
     */
    private static byte[] getBytes(InputStream in) throws IOException {
        return getBytes(in, null);
    }

    /**
     * Gets up to max bytes from an InputStream
     * @param in the InputStream from which to read bytes
     * @param max maximum number of bytes to read
     * @return array of all the bytes contained in 'in'
     * @throws IOException if an IOException is encountered while reading the data from the InputStream
     */
    private static byte[] getBytes(InputStream in, Integer max) throws IOException {
        // read the entire file into a byte buffer
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buff = new byte[256];
        while (max == null || max > 0) {
            int n = in.read(buff);
            if (n == -1) {
                break;
            }
            bout.write(buff, 0, n);
            if (max != null)
                max -= n;
        }
        in.close();
        return bout.toByteArray();
    }

    private static boolean isMagicPresent(byte[] link) {
        final int magic = 0x0000004C;
        final int magicOffset = 0x00;
        return link.length >= 32 && bytesToDword(link, magicOffset) == magic;
    }

    /**
     * Gobbles up link data by parsing it and storing info in member fields
     * @param link all the bytes from the .lnk file
     */
    private void parseLink(byte[] link) throws ParseException {
        try {
            if (!isMagicPresent(link))
                throw new ParseException("Invalid shortcut; magic is missing", 0);

            // get the flags byte
            byte flags = link[0x14];

            // get the file attributes byte
            final int fileAttsOffset = 0x18;
            byte fileAtts = link[fileAttsOffset];
            byte isDirMask = (byte)0x10;
            isDirectory = (fileAtts & isDirMask) > 0;

            // if the shell settings are present, skip them
            final int shellOffset = 0x4c;
            final byte hasShellMask = (byte)0x01;
            int shellLen = 0;
            if ((flags & hasShellMask) > 0) {
                // the plus 2 accounts for the length marker itself
                shellLen = bytesToWord(link, shellOffset) + 2;
            }

            // get to the file settings
            int fileStart = 0x4c + shellLen;

            final int fileLocationInfoFlagOffsetOffset = 0x08;
            int fileLocationInfoFlag = link[fileStart + fileLocationInfoFlagOffsetOffset];
            isLocal = (fileLocationInfoFlag & 2) == 0;
            // get the local volume and local system values
            //final int localVolumeTableOffsetOffset = 0x0C;
            final int basenameOffsetOffset = 0x10;
            final int networkVolumeTableOffsetOffset = 0x14;
            final int finalnameOffsetOffset = 0x18;
            int finalnameOffset = link[fileStart + finalnameOffsetOffset] + fileStart;
            String finalname = getNullDelimitedString(link, finalnameOffset);
            if (isLocal) {
                int basenameOffset = link[fileStart + basenameOffsetOffset] + fileStart;
                String basename = getNullDelimitedString(link, basenameOffset);
                readFile = basename + finalname;
            } else {
                int networkVolumeTableOffset = link[fileStart + networkVolumeTableOffsetOffset] + fileStart;
                int shareNameOffsetOffset = 0x08;
                int shareNameOffset = link[networkVolumeTableOffset + shareNameOffsetOffset]
                        + networkVolumeTableOffset;
                String shareName = getNullDelimitedString(link, shareNameOffset);
                readFile = shareName + "\\" + finalname;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            ParseException exception = new ParseException("Could not be parsed, probably not a valid WindowsShortcut", 0);
            exception.initCause(e);
            throw exception;
        }
    }

    private static String getNullDelimitedString(byte[] bytes, int off) {
        int len = 0;
        // count bytes until the null character (0)
        while (true) {
            if (bytes[off + len] == 0) {
                break;
            }
            len++;
        }
        return new String(bytes, off, len);
    }

    /*
     * convert two bytes into a short note, this is little endian because it's
     * for an Intel only OS.
     */
    private static int bytesToWord(byte[] bytes, int off) {
        return ((bytes[off + 1] & 0xff) << 8) | (bytes[off] & 0xff);
    }

    private static int bytesToDword(byte[] bytes, int off) {
        return (bytesToWord(bytes, off + 2) << 16) | bytesToWord(bytes, off);
    }

}