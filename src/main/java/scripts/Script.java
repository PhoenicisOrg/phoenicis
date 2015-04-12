package scripts;

import org.python.util.PythonInterpreter;

import java.io.File;

public class Script {
    private final File script;

    public Script(File script) {
        this.script = script;
    }

    public void run() {
        new Thread()
        {
            public void run() {
                PythonInterpreter pythonInterpreter = new PythonInterpreter();
                pythonInterpreter.execfile(script.getAbsolutePath());
            }
        }.start();

    }
}
