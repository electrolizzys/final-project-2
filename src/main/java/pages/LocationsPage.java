package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import constants.UrlConstants;
import utils.ConfigReader;

/**
 * The ATM/Branch/CDM locator on /en/atms&branches: a city dropdown, a location
 * search box, a type filter (tab menu: All/ATMs/Branches/CDMs) and a results
 * list they all drive. The list - not the map canvas - is what tests assert against, since
 * it is the reliable, DOM-backed signal that a filter actually took effect.
 */
public class LocationsPage extends BasePage {

    private final Locator cityDropdownTrigger;
    private final Locator searchInput;
    private final Locator resultItems;

    public LocationsPage(Page page) {
        super(page);
        this.cityDropdownTrigger = page.locator("tbcx-dropdown-selector").first();
        this.searchInput = page.getByPlaceholder("Specify the desired location");
        this.resultItems = page.locator("app-atm-branches-section-list-item");
    }

    public LocationsPage open() {
        open(UrlConstants.LOCATIONS_EN);
        return this;
    }

    public int getResultCount() {
        return resultItems.count();
    }

    public Locator results() {
        return resultItems;
    }

    /**
     * Types into the location search box key by key, like a user would; the
     * page debounces the input and sends one search request for the final text.
     */
    public void typeSearchKeyword(String keyword) {
        searchInput.pressSequentially(keyword);
    }

    /**
     * Opens the city dropdown and selects the given city, waiting - via a
     * web-first assertion - for the result count to actually change.
     */
    public int selectCityAndAwaitResultsChange(String city) {
        int countBefore = resultItems.count();
        cityDropdownTrigger.click();
        page.locator("tbcx-dropdown-popover-item").getByText(city, new Locator.GetByTextOptions().setExact(true)).click();
        PlaywrightAssertions.assertThat(resultItems).not().hasCount(countBefore,
                new LocatorAssertions.HasCountOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
        return resultItems.count();
    }

    /**
     * Switches the All/ATMs/Branches/CDMs tab, waiting for the result count
     * to actually change before returning.
     */
    public int filterByTypeAndAwaitResultsChange(String typeLabel) {
        int countBefore = resultItems.count();
        page.locator("tbcx-pw-tab-menu button.tbcx-pw-tab-menu__item", new Page.LocatorOptions().setHasText(typeLabel))
                .first()
                .click();
        PlaywrightAssertions.assertThat(resultItems).not().hasCount(countBefore,
                new LocatorAssertions.HasCountOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
        return resultItems.count();
    }
}
