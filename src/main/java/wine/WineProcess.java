package wine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qparis on 10/04/15.
 */
public class WineProcess {
    private final File binaryPath;
    private final File libraryPath;

    private WineProcess(WineProcess.Builder builder) {
        this.binaryPath = new File(builder.path, "bin");
        this.libraryPath = new File(builder.path, "lib");
    }

    // TODO
    public String fetchVersion() {
        return null;
    }

    private File fetchWineExecutablePath() {
        return new File(binaryPath, "wine");
    }

    public Process run(File workingDirectory, String executableToRun, HashMap<String, String> environement, ArrayList<String> arguments) throws IOException {
        ArrayList<String> command = new ArrayList<String>();
        command.add(this.fetchWineExecutablePath().getAbsolutePath());
        command.add(executableToRun);

        if(arguments != null) {
            command.addAll(arguments);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command)
                .directory(workingDirectory);


        Map<String, String> processEnvironement = processBuilder.environment();
        if(environement != null) {
            for (String environementVariable : environement.keySet()) {
                processEnvironement.put(environementVariable, environement.get(environementVariable));
            }
        }
        processEnvironement.put("PATH", this.binaryPath+":"+processEnvironement.get("PATH"));
        processEnvironement.put("LD_LIBRARY_PATH", this.binaryPath+":"+processEnvironement.get("LD_LIBRARY_PATH"));

        Process process = processBuilder.start();

        return process;
    }

    public Process run(File workingDirectory, String executableToRun, HashMap<String, String> environement) throws IOException {
        return this.run(workingDirectory, executableToRun, environement, null);
    }

    static class Builder {
        private File path;

        public WineProcess.Builder withLibraryPath(File path) {
            this.path = path;
            return this;
        }

        public WineProcess build() {
            return new WineProcess(this);
        }
    }
}
