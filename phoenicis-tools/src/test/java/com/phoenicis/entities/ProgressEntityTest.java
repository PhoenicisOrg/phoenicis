package com.phoenicis.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgressEntityTest {
    @Test
    public void testProgressEntity_testBuilder() {
        final ProgressEntity progressEntity =
                new ProgressEntity.Builder()
                    .withProgressText("Progress text")
                    .withPercent(42.)
                    .withState(ProgressState.FAILED)
                    .build();

        assertEquals(42., progressEntity.getPercent(), 0.01);
        assertEquals(ProgressState.FAILED, progressEntity.getState());
        assertEquals("Progress text", progressEntity.getProgressText());
    }
}