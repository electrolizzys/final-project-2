package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import api.models.BankLocation;
import api.models.LocationSearchRequest;
import pages.LocationsPage;

import java.util.List;

/**
 * Network-level flow for the location search: types a keyword into the search
 * box and captures the atmsAndBranches/list call that typing triggers.
 */
public class LocationSearchNetworkSteps {

    public static final String LOCATION_SEARCH_PATH = "/api/v1/atmsAndBranches/list";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Page page;

    public LocationSearchNetworkSteps(Page page) {
        this.page = page;
    }

    public record CapturedSearch(String url, String method, int status,
                                 LocationSearchRequest requestBody, List<BankLocation> locations) {
    }

    /**
     * Synchronizes on the network event itself: waits for the search response
     * whose request body carries this exact keyword. The page also calls the
     * same endpoint on load (empty keyword), and a fast typist could trigger a
     * request for a partial keyword - matching on the keyword skips both.
     */
    public CapturedSearch searchAndCaptureRequest(LocationsPage locations, String keyword) {
        Response response = page.waitForResponse(
                candidate -> candidate.url().contains(LOCATION_SEARCH_PATH)
                        && keyword.equals(keywordOf(candidate.request())),
                () -> locations.typeSearchKeyword(keyword));

        Request request = response.request();
        return new CapturedSearch(
                response.url(),
                request.method(),
                response.status(),
                parse(request.postData(), new TypeReference<>() {
                }),
                parse(response.text(), new TypeReference<>() {
                }));
    }

    private static String keywordOf(Request request) {
        String body = request.postData();
        if (body == null) {
            return null;
        }
        try {
            return MAPPER.readValue(body, LocationSearchRequest.class).getKeyword();
        } catch (JsonProcessingException notASearchBody) {
            return null;
        }
    }

    private static <T> T parse(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not parse captured location search JSON: " + json, e);
        }
    }
}
