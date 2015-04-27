package wine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WineInstallation {
    private static final String WINEPREFIXCREATE_COMMAND = "wineboot";
    private static final String WINEPREFIX_ENV = "WINEPREFIX";

    private static HashMap<String, String> systemEnvironment;

    private final File binaryPath;
    private final File libraryPath;

    private WineInstallation(WineInstallation.Builder builder) {
        this.binaryPath = new File(builder.path, "bin");
        this.libraryPath = new File(builder.path, "lib");
    }

    public static void setSystemEnvironment(HashMap<String, String> systemEnvironment) {
        WineInstallation.systemEnvironment = systemEnvironment;
    }

    // TODO
    public String fetchVersion() {
        return null;
    }

    private File fetchWineExecutablePath() {
        return new File(binaryPath, "wine");
    }

    public Process run(File workingDirectory, String executableToRun, HashMap<String, String> environment, ArrayList<String> arguments) throws IOException {
        ArrayList<String> command = new ArrayList<String>();
        command.add(this.fetchWineExecutablePath().getAbsolutePath());
        command.add(executableToRun);

        if(arguments != null) {
            command.addAll(arguments);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command)
                .directory(workingDirectory);


        Map<String, String> processEnvironement = processBuilder.environment();
        if(environment != null) {
            for (String environementVariable : environment.keySet()) {
                processEnvironement.put(environementVariable, environment.get(environementVariable));
            }
        }

        processEnvironement.put("PATH", this.binaryPath+":"+processEnvironement.get("PATH")+":"+systemEnvironment.get("PATH"));
        processEnvironement.put("LD_LIBRARY_PATH", this.libraryPath+":"+processEnvironement.get("LD_LIBRARY_PATH")+":"
                +systemEnvironment.get("LD_LIBRARY_PATH"));
        processEnvironement.put("DYLD_LIBRARY_PATH", systemEnvironment.get("DYLD_LIBRARY_PATH"));

        Process process = processBuilder.start();

        return process;
    }

    public Process run(File workingDirectory, String executableToRun, HashMap<String, String> environment) throws IOException {
        return this.run(workingDirectory, executableToRun, environment, null);
    }

    public Process createPrefix(WinePrefix winePrefix) throws IOException {
        HashMap<String, String> winePrefixEnvironment = new HashMap<>();
        winePrefixEnvironment.put(WINEPREFIX_ENV, winePrefix.getAbsolutePath());

        return this.run(winePrefix.getWinePrefixDirectory(), WINEPREFIXCREATE_COMMAND, winePrefixEnvironment);
    }

    public static class Builder {
        private File path;

        public WineInstallation.Builder withPath(File path) {
            this.path = path;
            return this;
        }

        public WineInstallation build() {
            return new WineInstallation(this);
        }
    }



}
