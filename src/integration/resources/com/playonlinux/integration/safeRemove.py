from com.playonlinux.framework import Files
from com.playonlinux.framework import Environment

from java.lang import IllegalArgumentException

import unittest

class TestExample(unittest.TestCase):
    def setUp(self):
        pass

    def testRemoveAFileInsidePlayOnLinuxRoot(self):
        Files().mkdir(Environment.getUserRoot() + "/test").remove(Environment.getUserRoot() + "/test")

    def testRemoveAFileOutstidePlayOnLinuxRoot(self):
        def deleteCase():
            Files().mkdir("/tmp/test").remove("/tmp/test")
        self.assertRaises(IllegalArgumentException, deleteCase)