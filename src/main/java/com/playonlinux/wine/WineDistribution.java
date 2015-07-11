package com.playonlinux.wine;


import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.OperatingSystem;

public class WineDistribution {
    private final OperatingSystem operatingSystem;
    private final Architecture architecture;
    private final String distributionCode;

    public WineDistribution(OperatingSystem operatingSystem, Architecture architecture, String distributionCode) {
        this.operatingSystem = operatingSystem;
        this.architecture = architecture;
        this.distributionCode = distributionCode;
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


}
