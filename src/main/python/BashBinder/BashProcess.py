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
import signal
import threading
import subprocess
import os
from java.lang import NoSuchFieldException
from java.lang import Exception


class BashProcess(threading.Thread):
    def __init__(self, processArguments):
        threading.Thread.__init__(self)
        self.processArguments = processArguments

    def run(self):
        self.process = subprocess.Popen(self.processArguments, stdout = subprocess.PIPE)
        self.process.wait()
        self.healthChecker.release()

    def stop(self):
        if(self.process.pid is None):
            pid = self.getPid(self.process)
        else: # Jython bug #2221 workaround
            pid = self.process.pid
        os.kill(pid, signal.SIGKILL)

    def getPid(self, process):
        try:
            field = process._process.getClass().getDeclaredField("pid")
            field.setAccessible(True)
        except (NoSuchFieldException, Exception):
            return None
        else:
            return field.getInt(process._process)

    def setHealthChecker(self, healthChecker):
        self.healthChecker = healthChecker