package com.playonlinux.scripts;

import com.playonlinux.api.ProgressStep;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.PlayOnLinuxError;
import com.playonlinux.wine.WineInstallation;

import java.io.IOException;

import static com.playonlinux.utils.Localisation.translate;

@ScriptClass
@SuppressWarnings("unused")
public class WinePrefix {
    static PlayOnLinuxContext playOnLinuxContext;
    private final long NEW_PREFIX_SIZE = 320000000;

    private final SetupWizard setupWizard;
    private com.playonlinux.wine.WinePrefix prefix;
    private String prefixName;

    public WinePrefix(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    public WinePrefix select(String prefixName) {
        this.prefixName = prefixName;
        this.prefix = new com.playonlinux.wine.WinePrefix(playOnLinuxContext.makePrefixPathFromName(prefixName));
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
                double percentage = this.prefix.getSize() * 100. / (double) NEW_PREFIX_SIZE;
                progressStep.setProgressPercentage(percentage);
            } catch (IllegalArgumentException e) {
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                process.destroy();
                wineInstallation.killAllProcess(this.prefix);
                throw new CancelException();
            }
        }
        process.waitFor();

        return this;
    }


    public static void injectPlayOnLinuxContext(PlayOnLinuxContext playOnLinuxContext) {
        WinePrefix.playOnLinuxContext = playOnLinuxContext;
    }
}
