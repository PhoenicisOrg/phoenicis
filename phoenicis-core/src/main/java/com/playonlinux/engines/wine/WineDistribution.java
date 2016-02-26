package com.playonlinux.engines.wine;


import static java.lang.String.format;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.playonlinux.core.utils.Architecture;
import com.playonlinux.core.utils.OperatingSystem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WineDistribution {
    @Getter
    private final OperatingSystem operatingSystem;
    @Getter
    private final Architecture architecture;
    @Getter
    private final String distributionCode;

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

    public String asNameWithCurrentOperatingSystem() {
        return format("%s-%s-%s", distributionCode,
                OperatingSystem.fetchCurrentOperationSystem().getWinePackage(),
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
