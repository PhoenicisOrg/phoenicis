package remote.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by qparis on 11/04/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Script {
    public int getId() {
        return id;
    }
    int id;
    String name;
    String description;

    ScriptInformations scriptInformations;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ScriptInformations getScriptInformations() {
        return scriptInformations;
    }
}
