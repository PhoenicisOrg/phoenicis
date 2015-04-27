package wine;

import utils.Injectable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineInstallation {
    private static final String WINEPREFIXCREATE_COMMAND = "wineboot";
    private static final String WINEPREFIX_ENV = "WINEPREFIX";

    private final File binaryPath;
    private final File libraryPath;

    private WineInstallation(WineInstallation.Builder builder) {
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

    private File fetchWineServerExecutablePath() {
        return new File(binaryPath, "wineserver");
    }

    // FIXME: Maybe it would be great to create a class to handle environment issues
    private void addPathInfoToEnvironment(HashMap<String, String> environment) {
        environment.put("PATH", this.binaryPath.getAbsolutePath());
        environment.put("LD_LIBRARY_PATH", this.libraryPath.getAbsolutePath());
    }

    public Process run(File workingDirectory, String executableToRun, HashMap<String, String> environment, ArrayList<String> arguments) throws IOException {
        ArrayList<String> command = new ArrayList<>();
        command.add(this.fetchWineExecutablePath().getAbsolutePath());
        command.add(executableToRun);
        if(arguments != null) {
            command.addAll(arguments);
        }

        HashMap<String, String> wineEnvironment = new HashMap<>();
        if(environment != null) {
            wineEnvironment.putAll(environment);
        }

        this.addPathInfoToEnvironment(wineEnvironment);

        return new WineProcessBuilder()
                .withCommand(command)
                .withEnvironment(wineEnvironment)
                .withWorkingDirectory(workingDirectory)
                .build();
    }

    public Process run(File workingDirectory, String executableToRun, HashMap<String, String> environment) throws IOException {
        return this.run(workingDirectory, executableToRun, environment, null);
    }

    public Process createPrefix(WinePrefix winePrefix) throws IOException {
        HashMap<String, String> winePrefixEnvironment = new HashMap<>();
        winePrefixEnvironment.put(WINEPREFIX_ENV, winePrefix.getAbsolutePath());

        return this.run(winePrefix.getWinePrefixDirectory(), WINEPREFIXCREATE_COMMAND, winePrefixEnvironment);
    }

    public void killAllProcess(WinePrefix winePrefix) throws IOException {

        HashMap<String, String> environment = new HashMap<>();
        this.addPathInfoToEnvironment(environment);
        environment.put(WINEPREFIX_ENV, winePrefix.getAbsolutePath());

        List<String> command = new ArrayList<>();
        command.add(this.fetchWineServerExecutablePath().getAbsolutePath());
        command.add("-k");

        new WineProcessBuilder()
                .withCommand(command)
                .withEnvironment(environment)
                .build();
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
