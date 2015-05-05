package com.playonlinux.domain;

import com.playonlinux.utils.BackgroundService;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

import java.io.*;

public class Script implements BackgroundService {
    private Thread scriptThread;

    @Override
    public void shutdown() {
        scriptThread.interrupt();
    }

    public enum Type {
        RECENT,
        LEGACY
    }

    private final File script;

    public Script(File script) {
        this.script = script;
    }

    public Type detectScriptType() throws IOException {
        BufferedReader bufferReader = new BufferedReader(new FileReader(this.script));
        String firstLine = bufferReader.readLine();
        if("#!/bin/bash".equals(firstLine)) {
            return Type.LEGACY;
        } else {
            return Type.RECENT;
        }
    }

    @Override
    public void start() {
        scriptThread = new Thread() {
            public void run() {
                try {
                    File pythonPath = new File("src/main/python");
                    System.getProperties().setProperty("python.path", pythonPath.getAbsolutePath());

                    PythonInterpreter pythonInterpreter = new PythonInterpreter();

                    if (detectScriptType() == Type.LEGACY) {
                        File v4wrapper = new File("src/main/python/v4wrapper.py");
                        String filePath = v4wrapper.getAbsolutePath();
                        pythonInterpreter.set("__file__", filePath);
                        pythonInterpreter.set("__scriptToWrap__", script.getAbsolutePath());
                        pythonInterpreter.execfile(filePath);
                    } else {
                        pythonInterpreter.execfile(script.getAbsolutePath());
                    }
                } catch (PyException e) {
                    if (e.getCause() instanceof CancelException || e.getCause() instanceof InterruptedIOException) {
                        System.out.println("The script was canceled! "); // Fixme: better logging system
                    } else {
                        throw e;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        scriptThread.start();

    }
}
