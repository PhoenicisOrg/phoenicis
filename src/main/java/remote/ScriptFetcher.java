package remote;

import org.springframework.web.client.RestTemplate;
import remote.dto.AvailableCategories;
import remote.dto.Category;

import java.util.ArrayList;

/**
 * This class download scripts from a remote web service and converts it into DTOs
 */
public class ScriptFetcher {
    private final String url;

    ScriptFetcher(String url) {
        this.url = url;
    }

    AvailableCategories fetchCategories() {
        RestTemplate restTemplate = new RestTemplate();
        AvailableCategories categories = restTemplate.getForObject(this.url, AvailableCategories.class);

        return categories;
    }
}
