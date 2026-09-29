package steps;

import com.microsoft.playwright.Page;
import pages.HomePage;
import pages.LocationsPage;

/**
 * Business actions for the ATM/Branch/CDM locator: reaching the page (via the
 * homepage's quick-action card, or directly), picking a city and filtering by type.
 */
public class LocationsSteps {

    private final Page page;

    public LocationsSteps(Page page) {
        this.page = page;
    }

    public LocationsPage navigateToLocationsViaHomepage() {
        new HomePage(page).open().clickLocationsCard();
        return new LocationsPage(page);
    }

    public LocationsPage openLocationsPage() {
        return new LocationsPage(page).open();
    }

    public int selectCity(LocationsPage locations, String city) {
        return locations.selectCityAndAwaitResultsChange(city);
    }

    public int filterByType(LocationsPage locations, String typeLabel) {
        return locations.filterByTypeAndAwaitResultsChange(typeLabel);
    }
}
