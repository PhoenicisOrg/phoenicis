package scripts;

import app.PlayOnLinuxContext;
import utils.Architecture;
import utils.PlayOnLinuxError;
import wine.WineInstallation;

import java.io.IOException;

import static utils.Localisation.translate;

public class WinePrefix {
    static PlayOnLinuxContext playOnLinuxContext;

    private final SetupWizard setupWizard;
    private wine.WinePrefix prefix;
    private String prefixName;

    public WinePrefix(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;

    }

    public WinePrefix select(String prefixName) {
        this.prefixName = prefixName;
        this.prefix = new wine.WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));
        return this;
    }

    public WinePrefix create(String version) throws PlayOnLinuxError, IOException, InterruptedException {
        return this.create(version, Architecture.fetchCurrentArchitecture().name());
    }

    public WinePrefix create(String version, String architecture) throws IOException, PlayOnLinuxError, InterruptedException {
        if(prefix == null) {
            throw new PlayOnLinuxError("Prefix must be selected!");
        }
        WineInstallation wineInstallation = new WineInstallation.Builder()
                .withPath(playOnLinuxContext.makeWinePathFromVersionAndArchitecture(
                        version,
                        Architecture.valueOf(architecture))
                ).build();

        this.setupWizard.wait(String.format(translate("Please wait while the prefix %s is created"), prefixName));
        Process process = wineInstallation.createPrefix(this.prefix);
        process.waitFor();

        return this;
    }


    public static void injectPlayOnLinuxContext(PlayOnLinuxContext playOnLinuxContext) {
        WinePrefix.playOnLinuxContext = playOnLinuxContext;
    }
}
