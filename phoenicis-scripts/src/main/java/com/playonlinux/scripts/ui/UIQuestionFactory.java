package com.playonlinux.scripts.ui;

public interface UIQuestionFactory {
    void create(String questionText, Runnable yesCallback, Runnable noCallback);

    default void create(String questionText, Runnable yesCallback) {
        create(questionText, yesCallback, () -> {});
    }
}
