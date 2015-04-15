#!/usr/bin/env python
import socket, threading, thread, string, random
from SetupWindowNetcatCommandParser import SetupWindowNetcatCommandParser
from SetupWindowManager import SetupWindowManager

class SetupWindowNetcatServer(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self._host = '127.0.0.1'
        self._port = 30000
        self._cookie = self._generateUniqueCookie()
        self._running = True
        self.setupWindowManager = SetupWindowManager()

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

        result = self.processReceivedCommand(clientCommand)
        connection.send(str(result))

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
        commandParser = SetupWindowNetcatCommandParser(self.setupWindowManager, command)
        if(commandParser.getCookie() != self.getCookie()):
            raise Exception("Bad cookie!")
        else:
            return commandParser.executeCommand()

    def getPort(self):
        return self._port

    def getCookie(self):
        return self._cookie
