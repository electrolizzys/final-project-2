package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import utils.ConfigReader;
import utils.PlaywrightFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Gives every test method its own browser context via PlaywrightFactory's
 * per-thread instances, so TestNG can run test classes in parallel safely.
 */
public abstract class BaseTest {

    protected Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        page = PlaywrightFactory.createPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        PlaywrightFactory.closePage();
    }

    /**
     * Playwright's assertThat(...) has its own 5s default timeout, separate
     * from the context's configured action timeout - too short under parallel
     * load. This routes visibility assertions through the same configured budget.
     */
    protected void assertVisible(Locator locator) {
        PlaywrightAssertions.assertThat(locator)
                .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
    }
}
