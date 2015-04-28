package com.playonlinux.webservice;

import org.springframework.web.client.RestTemplate;
import com.playonlinux.webservice.dto.AvailableCategories;

/**
 * This class download com.playonlinux.scripts from a com.playonlinux.webservice web service and converts it into DTOs
 */
public class ScriptFetcher {
    private final String url;

    ScriptFetcher(String url) {
        this.url = url;
    }

    AvailableCategories fetchCategories() {
        return new RestTemplate().getForObject(this.url, AvailableCategories.class);
    }
}
