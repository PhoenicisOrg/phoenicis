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

package com.playonlinux.win32.registry;

public class RegistryValue<T extends AbstractValueType> extends AbstractRegistryNode {

    private final T content;

    public RegistryValue(String name, T content) {
        super(name);
        this.content = content;
    }

    public static RegistryValue<AbstractValueType> fromString(String name, String inputString) {
        RegistryValue<AbstractValueType> parsedValue;
        if (inputString.startsWith("\"") && inputString.endsWith("\"")) {
            String valueContentString = inputString.substring(1, inputString.length() - 1);
            valueContentString = valueContentString.replaceAll("\\\\\\\"", "\"");
            valueContentString = valueContentString.replaceAll("\\\\\\\\", "\\\\");
            if (valueContentString.endsWith("\\0")) {
                valueContentString = valueContentString.substring(0, valueContentString.length() - 2);
            }
            parsedValue = new RegistryValue<>(name, new StringValueType(valueContentString));
        } else if (inputString.contains(":")) {
            int colPosition = inputString.indexOf(':');
            final String valueTypeString = inputString.substring(0, colPosition);
            String valueContentString = inputString.substring(colPosition + 1, inputString.length());
            if (valueContentString.endsWith("\\0")) {
                valueContentString = valueContentString.substring(0, valueContentString.length() - 2);
            }
            switch (valueTypeString) {
                case "str(7)": // Multi String
                    parsedValue = new RegistryValue<>(name, new MultiStringValueType(valueContentString));
                    break;
                case "str(2)": // Expandable String
                    parsedValue = new RegistryValue<>(name, new ExpandableValueType(valueContentString));
                    break;
                case "dword": // DWORD
                    long dwordValue = Long.valueOf(valueContentString, 16);
                    parsedValue = new RegistryValue<>(name, new DwordValueType(dwordValue));
                    break;
                case "hex": // Binary
                case "hex(6)": // FIXME
                case "hex(2)":
                case "hex(7)":
                    String[] binariesString = valueContentString.split(",");
                    byte[] binaries = new byte[binariesString.length];
                    for (int i = 0; i < binariesString.length; i++) {
                        binaries[i] = (byte) (Integer.valueOf(binariesString[i], 16) - 128);
                    }
                    parsedValue = new RegistryValue<>(name, new BinaryValueType(binaries));
                    break;
                default:
                    parsedValue = new RegistryValue<>(name, new StringValueType("")); // FIXME
                    break;
            }

        } else {
            throw new IllegalArgumentException(String.format("Unable to format registry line: %s", inputString));
        }

        return parsedValue;
    }

    public String getText() {
        return content.getText();
    }

    @Override
    public String toString() {
        return String.format("- (%s) %s -> %s", content.getClass().getSimpleName(), this.getName(), content.getText());
    }

    @Override
    public String toRegString() {
        return String.format("\"%s\"=%s", this.getName(), content.toRegString());
    }
}
