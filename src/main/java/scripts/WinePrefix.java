package scripts;

import api.ProgressStep;
import app.PlayOnLinuxContext;
import utils.Architecture;
import utils.PlayOnLinuxError;
import wine.WineInstallation;

import java.io.IOException;

import static utils.Localisation.translate;

public class WinePrefix {
    static PlayOnLinuxContext playOnLinuxContext;
    private final long NEW_PREFIX_SIZE = 320000000;

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

    public WinePrefix create(String version) throws PlayOnLinuxError, IOException, InterruptedException, CancelException {
        return this.create(version, Architecture.fetchCurrentArchitecture().name());
    }

    public WinePrefix create(String version, String architecture) throws IOException, PlayOnLinuxError,
            InterruptedException, CancelException {
        if(prefix == null) {
            throw new PlayOnLinuxError("Prefix must be selected!");
        }
        WineInstallation wineInstallation = new WineInstallation.Builder()
                .withPath(playOnLinuxContext.makeWinePathFromVersionAndArchitecture(
                        version,
                        Architecture.valueOf(architecture))
                ).build();

        Process process = wineInstallation.createPrefix(this.prefix);

        /* Maybe it needs to be better implemented */
        ProgressStep progressStep =
                this.setupWizard.progressBar(String.format(translate("Please wait while the prefix %s is created"), prefixName));

        while(process.isAlive()) {
            try {
                double percentage = this.prefix.getSize() * 100. / 320000000.;
                progressStep.setProgressPercentage(percentage);
            } catch (IllegalArgumentException e) {

            }
            Thread.sleep(10);
        }
        process.waitFor();

        return this;
    }


    public static void injectPlayOnLinuxContext(PlayOnLinuxContext playOnLinuxContext) {
        WinePrefix.playOnLinuxContext = playOnLinuxContext;
    }
}
