package com.playonlinux.engines.wine;


import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.OperatingSystem;
import org.apache.commons.lang.builder.ToStringBuilder;

import static java.lang.String.format;

public class WineDistribution {
    private final OperatingSystem operatingSystem;
    private final Architecture architecture;
    private final String distributionCode;

    public WineDistribution(OperatingSystem operatingSystem, Architecture architecture, String distributionCode) {
        this.operatingSystem = operatingSystem;
        this.architecture = architecture;
        this.distributionCode = distributionCode;
    }

    /**
     * Construct from a short code string with the current operating system.
     * @param shortCodeName the short code name (e.g. staging-x86)
     */
    public WineDistribution(String shortCodeName) {
        this(OperatingSystem.fetchCurrentOperationSystem(),
                Architecture.fromWinePackageName(shortCodeName.split("-")[1]),
                shortCodeName.split("-")[0]
        );
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public String getDistributionCode() {
        return distributionCode;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public String asName() {
        return format("%s-%s", distributionCode, architecture.getNameForWinePackages());
    }

    public String asNameWithCurrentOperatingSystem() {
        return format("%s-%s-%s", distributionCode,
                OperatingSystem.fetchCurrentOperationSystem().getNameForWinePackages(),
                architecture.getNameForWinePackages()
        );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(WineDistribution.class)
                .append(operatingSystem)
                .append(architecture)
                .append(distributionCode)
                .toString();
    }


}
