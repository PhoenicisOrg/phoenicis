package org.phoenicis.javafx.components.application.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.repository.dto.ScriptDTO;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

/**
 * A filter implementation for {@link ScriptDTO} objects
 */
public interface ScriptFilter {
    /**
     * The operating system of the user
     */
    ObjectProperty<OperatingSystem> operatingSystemProperty();

    /**
     * Whether commercial applications should be shown
     */
    BooleanProperty containCommercialApplicationsProperty();

    /**
     * Whether applications requiring a patch should be shown
     */
    BooleanProperty containRequiresPatchApplicationsProperty();

    /**
     * Whether testing applications should be shown
     */
    BooleanProperty containTestingApplicationsProperty();

    /**
     * Whether applications not explicitly compatible with the OS of the user should be shown
     */
    BooleanProperty containAllOSCompatibleApplicationsProperty();

    /**
     * Filter function for {@link ScriptDTO} objects
     *
     * @param script The script which should be checked
     * @return True if the given <code>script</code> fulfills the filterCategory conditions, false otherwise
     */
    default boolean filterScript(ScriptDTO script) {
        boolean result = true;

        /*
         * If "commercial" is not selected, don't show commercial games
         */
        if (!containCommercialApplicationsProperty().get()) {
            result &= script.isFree();
        }

        /*
         * If "Requires patch" is selected, show show games that require a patch to run (e.g. no CD)
         */
        if (containRequiresPatchApplicationsProperty().get()) {
            result &= !script.isRequiresPatch();
        }

        /*
         * If "Testing" is not selected, don't show games that are currently in a testing stage
         */
        if (!containTestingApplicationsProperty().get()) {
            result &= CollectionUtils.isEmpty(script.getTestingOperatingSystems());
        }

        /*
         * If "All Operating Systems" is not selected, show only applications that fit to the used operating system
         */
        if (!containAllOSCompatibleApplicationsProperty().get()) {
            result &= Optional.ofNullable(script.getCompatibleOperatingSystems())
                    .map(compatibleOperatingSystems -> compatibleOperatingSystems
                            .contains(operatingSystemProperty().get()))
                    .orElse(false);
        }

        return result;
    }
}
