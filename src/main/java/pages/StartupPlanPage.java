package pages;

import com.microsoft.playwright.Page;

/**
 * The Startup Plan detail page, showing the plan's benefits. It exposes no
 * page-specific selectors of its own beyond what BasePage already provides
 * for asserting locale-dependent heading text.
 */
public class StartupPlanPage extends BasePage {

    public StartupPlanPage(Page page) {
        super(page);
    }
}
