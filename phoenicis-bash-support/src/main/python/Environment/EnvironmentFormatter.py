#!/usr/bin/env python
# coding=utf-8

# Copyright (C) 2015 Pâris Quentin

# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

class EnvironmentFormatter(object):
    @classmethod
    def getEnvironmentVarsFromBashBase64EncodedString(cls, environmentStr):
        exportedEnvironement = environmentStr.decode("base64")
        results = {}
        for line in exportedEnvironement.split("\n"):
            if("declare -x " in line):
                try:
                    line = line.replace("\r", "").replace("declare -x ", "")
                    line = line.split("=")
                    key = line[0]
                    value = "=".join(line[1::1])[1:-1]
                    results[key] = value
                except IndexError:
                    pass

        return results