package scripts;

import org.python.core.PyException;
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
                try {
                    PythonInterpreter pythonInterpreter = new PythonInterpreter();
                    pythonInterpreter.execfile(script.getAbsolutePath());

                } catch (PyException e) {
                    if(e.getCause() instanceof CancelException) {
                        System.out.println("The script was canceled! "); // Fixme: better logging system
                    } else {
                        throw e;
                    }
                }
            }
        }.start();

    }
}
