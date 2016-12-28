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

package com.playonlinux.javafx;

import com.playonlinux.scripts.ui.UIMessageSender;
import javafx.application.Platform;
import org.apache.commons.lang.mutable.MutableObject;

import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class UIMessageSenderJavaFXImplementation implements UIMessageSender {
    @Override
    public <R> R run(Supplier<R> function) {

        // runBackground synchronously on JavaFX thread
        if (Platform.isFxApplicationThread()) {
            return function.get();
        }

        // queue on JavaFX thread and wait for completion
        final CountDownLatch doneLatch = new CountDownLatch(1);
        final MutableObject result = new MutableObject();
        Platform.runLater(() -> {
            try {
                result.setValue(function.get());
            } finally {
                doneLatch.countDown();
            }
        });

        try {
            doneLatch.await();
        } catch (InterruptedException e) {
            // ignore exception
        }

        return (R) result.getValue();
    }
}
