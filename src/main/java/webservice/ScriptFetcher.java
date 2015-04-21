package webservice;

import org.springframework.web.client.RestTemplate;
import webservice.dto.AvailableCategories;

/**
 * This class download scripts from a webservice web service and converts it into DTOs
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
