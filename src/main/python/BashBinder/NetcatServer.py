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

import socket
import threading
import thread
import string
import random

from java.lang import Throwable
from CommandParser import CommandParser


class NetcatServer(threading.Thread):
    def __init__(self, setupWindowManager):
        threading.Thread.__init__(self)
        self._host = '127.0.0.1'
        self._port = 30000
        self._cookie = self._generateUniqueCookie()
        self._running = True
        self.setupWindowManager = setupWindowManager
        self.healthChecker = None

    def connectionHandler(self, connection):
        clientCommand = ""
        while True:
            try:
                chunk = connection.recv(2048)
            except socket.error, (erno, msg):
                if(erno == 9):
                    break
                else:
                    raise

            clientCommand += chunk.replace("\n", "")
            if "\n" in chunk:
                break

        try:
            result = self.processReceivedCommand(clientCommand)
        except Throwable, e:
            print "Exception encountered. Will close the SetupWindow server now"
            self.healthChecker.sendException(e)
        else:
            connection.send(str(result))
        finally:
            connection.shutdown(1)
            connection.close()

    def _generateUniqueCookie(self, length=20, chars=string.letters + string.digits):
        return ''.join([random.SystemRandom().choice(chars) for i in range(length)])

    def initServer(self):
        if(self._port  >= 30020):
            raise Exception("Unable to reserve a valid port")

        try:
            self.acceptor = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.acceptor.bind ( ( str(self._host), int(self._port) ) )
            self.acceptor.listen(10)
        except socket.error, msg:
            self._port += 1
            self.initServer()
        else:
            self.start()


    def closeServer(self):
        self.acceptor.close()
        self._running = False






    def run(self):
        while self._running:
            try:
                connection, addr = self.acceptor.accept()
            except socket.error, (errno, msg):
                if errno == 4: # Interrupted system call
                    continue
            else:
                thread.start_new_thread(self.connectionHandler, (connection, ))


    def processReceivedCommand(self, command):
        commandParser = CommandParser(self.setupWindowManager, command)
        if(commandParser.getCookie() != self.getCookie()):
            raise Exception("Bad cookie!")
        else:
            return commandParser.executeCommand()

    def setHealthChecker(self, healthChecker):
        self.healthChecker = healthChecker

    def getPort(self):
        return self._port

    def getCookie(self):
        return self._cookie