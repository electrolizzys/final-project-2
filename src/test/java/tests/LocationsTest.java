package tests;

import pages.LocationsPage;
import steps.LocationsSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LocationsTest extends BaseTest {

    @Test(description = "KAN-T15 | Filter branches and ATMs by city filter")
    public void selectingCityUpdatesResults() {
        LocationsSteps steps = new LocationsSteps(page);
        LocationsPage locations = steps.navigateToLocationsViaHomepage();
        int countBeforeSelection = locations.getResultCount();

        int countAfterBatumi = steps.selectCity(locations, "Batumi");

        Assert.assertTrue(countAfterBatumi > 0, "Expected at least one location in Batumi");
        Assert.assertNotEquals(countAfterBatumi, countBeforeSelection,
                "Selecting a city should change the number of results shown");
    }

    @Test(description = "KAN-T16 | Filter map locations by type")
    public void filteringByTypeUpdatesResults() {
        LocationsSteps steps = new LocationsSteps(page);
        LocationsPage locations = steps.navigateToLocationsViaHomepage();
        int allCount = locations.getResultCount();

        int atmCount = steps.filterByType(locations, "ATMs");
        Assert.assertNotEquals(atmCount, allCount, "ATMs filter should narrow down the unfiltered result count");

        int branchCount = steps.filterByType(locations, "Branches");
        Assert.assertNotEquals(branchCount, atmCount, "Branches filter should show a different result count than ATMs");

        int cdmCount = steps.filterByType(locations, "CDMs");
        Assert.assertNotEquals(cdmCount, branchCount, "CDMs filter should show a different result count than Branches");
    }
}
