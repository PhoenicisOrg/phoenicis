package org.phoenicis.javafx.views.common;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * A delayed filter text consumer for the {@link org.phoenicis.javafx.views.mainwindow.ui.SearchBox}.
 *
 * @see org.phoenicis.javafx.views.mainwindow.ui.SearchBox
 * @author Marc Arndt
 */
public class DelayedFilterTextConsumer implements Consumer<String> {
    private PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

    private final Consumer<String> internalTextConsumer;

    /**
     * Constructor
     *
     * @param internalTextConsumer Internal text consumer
     */
    public DelayedFilterTextConsumer(Consumer<String> internalTextConsumer) {
        super();

        this.internalTextConsumer = internalTextConsumer;
    }

    @Override
    public void accept(String filterText) {
        this.pause.setOnFinished(event -> {
            String text = filterText.toLowerCase();

            if (text != null && text.length() >= 3) {
                internalTextConsumer.accept(text);
            } else {
                internalTextConsumer.accept("");
            }
        });

        this.pause.playFromStart();
    }
}
