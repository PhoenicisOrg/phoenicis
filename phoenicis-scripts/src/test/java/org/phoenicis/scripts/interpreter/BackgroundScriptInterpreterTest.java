package org.phoenicis.scripts.interpreter;

import org.junit.Test;
import org.phoenicis.scripts.session.InteractiveScriptSession;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BackgroundScriptInterpreterTest {
    @Test
    public void testRunScriptPropagatesThrowableToErrorCallback() throws InterruptedException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final ScriptInterpreter delegated = new ThrowingScriptInterpreter(new AssertionError("boom"));
        final BackgroundScriptInterpreter interpreter = new BackgroundScriptInterpreter(delegated, executorService);
        final AtomicReference<Exception> receivedError = new AtomicReference<>();

        interpreter.runScript("ignored", () -> {
        }, receivedError::set);

        executorService.shutdown();
        executorService.awaitTermination(2, TimeUnit.SECONDS);

        assertNotNull(receivedError.get());
        assertEquals(AssertionError.class, receivedError.get().getCause().getClass());
    }

    @Test
    public void testInteractiveSessionPropagatesThrowableToErrorCallback() throws InterruptedException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final ScriptInterpreter delegated = new ThrowingInteractiveScriptInterpreter(new AssertionError("boom"));
        final BackgroundScriptInterpreter interpreter = new BackgroundScriptInterpreter(delegated, executorService);
        final AtomicReference<Exception> receivedError = new AtomicReference<>();

        interpreter.createInteractiveSession().eval("ignored", ignored -> {
        }, receivedError::set);

        executorService.shutdown();
        executorService.awaitTermination(2, TimeUnit.SECONDS);

        assertNotNull(receivedError.get());
        assertEquals(AssertionError.class, receivedError.get().getCause().getClass());
    }

    private static final class ThrowingScriptInterpreter implements ScriptInterpreter {
        private final Throwable throwable;

        private ThrowingScriptInterpreter(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void runScript(String scriptContent, Runnable doneCallback,
                java.util.function.Consumer<Exception> errorCallback) {
            throwAsUnchecked(throwable);
        }

        @Override
        public InteractiveScriptSession createInteractiveSession() {
            return (evaluation, responseCallback, errorCallback) -> {
            };
        }
    }

    private static final class ThrowingInteractiveScriptInterpreter implements ScriptInterpreter {
        private final Throwable throwable;

        private ThrowingInteractiveScriptInterpreter(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void runScript(String scriptContent, Runnable doneCallback,
                java.util.function.Consumer<Exception> errorCallback) {
        }

        @Override
        public InteractiveScriptSession createInteractiveSession() {
            return (evaluation, responseCallback, errorCallback) -> throwAsUnchecked(throwable);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwAsUnchecked(Throwable throwable) throws T {
        throw (T) throwable;
    }
}
