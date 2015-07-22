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
import os

from com.playonlinux.framework import Environment

class EnvironmentLoader():
    @staticmethod
    def setup(setupWindowNetcatServer):
        os.environ["PLAYONLINUX"] = os.path.realpath(os.path.join(os.path.dirname(__file__), "../", "../" "bash"))
        os.environ["POL_PORT"] = str(setupWindowNetcatServer.getPort())
        os.environ["POL_COOKIE"] = setupWindowNetcatServer.getCookie()
        os.environ["POL_OS"] = Environment.getOperatinSystem().fetchLegacyName()
        os.environ["POL_USER_ROOT"] = Environment.getUserRoot()
        os.environ["AMD64_COMPATIBLE"] = str(Environment.getArchitecture().name() == "AMD64").upper()
        os.environ["PATH"] = Environment.getPath()
        os.environ["LD_LIBRARY_PATH"] = Environment.getLibraryPath()
        os.environ["DYLD_LIBRARY_PATH"] = Environment.getDyldLibraryPath()