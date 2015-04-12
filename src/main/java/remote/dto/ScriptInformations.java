package remote.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wine.OperatingSystem;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptInformations {
    public ArrayList<OperatingSystem> getCompatiblesOperatingSystems() {
        return compatiblesOperatingSystems;
    }

    public ArrayList<OperatingSystem> getTestingOperatingSystems() {
        return testingOperatingSystems;
    }

    public Boolean isFree() {
        return free;
    }

    public Boolean isRequiresNoCD() {
        return requiresNoCD;
    }

    public Boolean isHasIcon() {
        return hasIcon;
    }

    private ArrayList<OperatingSystem> compatiblesOperatingSystems;
    private ArrayList<OperatingSystem> testingOperatingSystems;

    private Boolean free;
    private Boolean requiresNoCD;
    private Boolean hasIcon;

}
