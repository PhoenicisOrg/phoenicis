package com.playonlinux.scripts.ui;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface UIMessageSender {
    <R> R run(Supplier<R> function);

    default <R> R runAndWait(Consumer<Message<R>> function) {
        final Message<R> message = new Message<>();
        run(() -> {
            function.accept(message);
            return null;
        });
        message.block();

        return message.get();
    }

}
