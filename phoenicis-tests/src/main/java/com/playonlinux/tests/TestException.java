package com.playonlinux.tests;

class TestException extends RuntimeException {
    TestException(Exception e) {
        super(e);
    }
}
