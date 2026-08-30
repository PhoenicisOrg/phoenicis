package org.phoenicis.engines;

import org.junit.Test;
import org.phoenicis.repository.dto.*;
import org.phoenicis.scripts.engine.PhoenicisScriptEngineFactory;
import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.phoenicis.scripts.exceptions.IncludeException;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class EngineSettingsManagerTest {
    @Test
    public void shouldIgnoreMissingSettingScript() {
        final AtomicReference<String> includeCall = new AtomicReference<>();
        final AtomicReference<Boolean> errorCallbackCalled = new AtomicReference<>(false);

        final PhoenicisScriptEngine scriptEngine = new PhoenicisScriptEngine() {
            @Override
            public void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
                // not used in this test
            }

            @Override
            public void eval(String script, Runnable doneCallback, Consumer<Exception> errorCallback) {
                // not used in this test
            }

            @Override
            public Object evalAndReturn(String line, Consumer<Exception> errorCallback) {
                includeCall.set(line);
                errorCallback.accept(new IncludeException("engines.wine.settings.retina", new RuntimeException()));
                return "";
            }

            @Override
            public void put(String name, Object object, Consumer<Exception> errorCallback) {
                // not used in this test
            }

            @Override
            public void addErrorHandler(Consumer<Exception> errorHandler) {
                // not used in this test
            }
        };

        final PhoenicisScriptEngineFactory scriptEngineFactory = new PhoenicisScriptEngineFactory(null, null) {
            @Override
            public PhoenicisScriptEngine createEngine() {
                return scriptEngine;
            }
        };

        final ExecutorService directExecutor = new AbstractExecutorService() {
            @Override
            public void shutdown() {
                // no-op
            }

            @Override
            public List<Runnable> shutdownNow() {
                return Collections.emptyList();
            }

            @Override
            public boolean isShutdown() {
                return false;
            }

            @Override
            public boolean isTerminated() {
                return false;
            }

            @Override
            public boolean awaitTermination(long timeout, java.util.concurrent.TimeUnit unit) {
                return true;
            }

            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        final EngineSettingsManager manager = new EngineSettingsManager(scriptEngineFactory, directExecutor);

        final RepositoryDTO repository = new RepositoryDTO.Builder()
                .withTypes(Collections.singletonList(new TypeDTO.Builder()
                        .withId("engines")
                        .withCategories(Collections.singletonList(new CategoryDTO.Builder()
                                .withId("engines.wine")
                                .withApplications(Collections.singletonList(new ApplicationDTO.Builder()
                                        .withId("engines.wine.settings")
                                        .withScripts(Collections.singletonList(new ScriptDTO.Builder()
                                                .withId("engines.wine.settings.retina")
                                                .build()))
                                        .build()))
                                .build()))
                        .build()))
                .build();

        final AtomicReference<Map<String, List<EngineSetting>>> result = new AtomicReference<>();

        manager.fetchAvailableEngineSettings(repository, result::set, e -> errorCallbackCalled.set(true));

        assertEquals("include(\"engines.wine.settings.retina\");", includeCall.get());
        assertTrue(result.get().containsKey("wine"));
        assertTrue(result.get().get("wine").isEmpty());
        assertFalse(errorCallbackCalled.get());
    }

    @Test
    public void shouldForwardNonIncludeErrorsToErrorCallback() {
        final AtomicReference<Boolean> errorCallbackCalled = new AtomicReference<>(false);

        final PhoenicisScriptEngine scriptEngine = new PhoenicisScriptEngine() {
            @Override
            public void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
                // not used in this test
            }

            @Override
            public void eval(String script, Runnable doneCallback, Consumer<Exception> errorCallback) {
                // not used in this test
            }

            @Override
            public Object evalAndReturn(String line, Consumer<Exception> errorCallback) {
                errorCallback.accept(new RuntimeException("boom"));
                return "";
            }

            @Override
            public void put(String name, Object object, Consumer<Exception> errorCallback) {
                // not used in this test
            }

            @Override
            public void addErrorHandler(Consumer<Exception> errorHandler) {
                // not used in this test
            }
        };

        final PhoenicisScriptEngineFactory scriptEngineFactory = new PhoenicisScriptEngineFactory(null, null) {
            @Override
            public PhoenicisScriptEngine createEngine() {
                return scriptEngine;
            }
        };

        final ExecutorService directExecutor = new AbstractExecutorService() {
            @Override
            public void shutdown() {
                // no-op
            }

            @Override
            public List<Runnable> shutdownNow() {
                return Collections.emptyList();
            }

            @Override
            public boolean isShutdown() {
                return false;
            }

            @Override
            public boolean isTerminated() {
                return false;
            }

            @Override
            public boolean awaitTermination(long timeout, java.util.concurrent.TimeUnit unit) {
                return true;
            }

            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        final EngineSettingsManager manager = new EngineSettingsManager(scriptEngineFactory, directExecutor);

        final RepositoryDTO repository = new RepositoryDTO.Builder()
                .withTypes(Collections.singletonList(new TypeDTO.Builder()
                        .withId("engines")
                        .withCategories(Collections.singletonList(new CategoryDTO.Builder()
                                .withId("engines.wine")
                                .withApplications(Collections.singletonList(new ApplicationDTO.Builder()
                                        .withId("engines.wine.settings")
                                        .withScripts(Collections.singletonList(new ScriptDTO.Builder()
                                                .withId("engines.wine.settings.retina")
                                                .build()))
                                        .build()))
                                .build()))
                        .build()))
                .build();

        manager.fetchAvailableEngineSettings(repository, ignored -> {
            // ignored in this scenario
        }, e -> errorCallbackCalled.set(true));

        assertTrue(errorCallbackCalled.get());
    }

}
