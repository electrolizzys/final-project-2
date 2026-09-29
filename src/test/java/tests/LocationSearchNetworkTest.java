package tests;

import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import api.models.BankLocation;
import pages.LocationsPage;
import steps.LocationSearchNetworkSteps;
import steps.LocationSearchNetworkSteps.CapturedSearch;
import steps.LocationsSteps;
import utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Network validation: typing in the locations search box must send the
 * expected search request, get a successful response for that keyword, and
 * the results list on the page must reflect exactly what the response returned.
 */
public class LocationSearchNetworkTest extends BaseTest {

    private static final String KEYWORD = "Rustavi";

    @Test(description = "KAN-T23 | Verify the location search request, response and resulting list")
    public void locationSearchSendsKeywordAndListMatchesResponse() {
        LocationsPage locations = new LocationsSteps(page).openLocationsPage();
        assertVisible(locations.results().first());

        CapturedSearch search = new LocationSearchNetworkSteps(page).searchAndCaptureRequest(locations, KEYWORD);

        Assert.assertTrue(search.url().contains(LocationSearchNetworkSteps.LOCATION_SEARCH_PATH),
                "Search should call the atmsAndBranches/list endpoint, got: " + search.url());
        Assert.assertEquals(search.method(), "POST", "Search should be sent as a POST");
        Assert.assertEquals(search.status(), 200, "Search response status");

        Assert.assertEquals(search.requestBody().getKeyword(), KEYWORD, "Request should carry the typed keyword");
        Assert.assertTrue(search.requestBody().getFilter().isEmpty(),
                "No type filter was selected, so the request filter should be empty");

        Assert.assertFalse(search.locations().isEmpty(), "Search should return at least one location");
        for (BankLocation location : search.locations()) {
            Assert.assertTrue(location.mentions(KEYWORD), "Returned location does not relate to the keyword: " + location);
        }

        PlaywrightAssertions.assertThat(locations.results()).hasCount(search.locations().size(),
                new LocatorAssertions.HasCountOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
        PlaywrightAssertions.assertThat(locations.results().first()).containsText(KEYWORD,
                new LocatorAssertions.ContainsTextOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
    }
}
