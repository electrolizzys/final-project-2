package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * The /en/loans overview screen reached from the homepage's "Calculate the
 * loan" card; its "GET A LOAN" button leads into the mortgage calculator.
 * The href alone is ambiguous (the mega-menu hides many more links to the
 * same /en/loans/mortgage URL) and the accessible name alone is ambiguous
 * too (a mobile-app store deep link shares it), so this targets the
 * intersection of both.
 */
public class LoansHubPage extends BasePage {

    private final Locator getALoanButtonLocator;

    public LoansHubPage(Page page) {
        super(page);
        this.getALoanButtonLocator = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("GET A LOAN"))
                .and(page.locator("a[href='/en/loans/mortgage']"))
                .first();
    }

    public Locator getALoanButton() {
        return getALoanButtonLocator;
    }

    public LoanCalculatorPage clickGetALoan() {
        getALoanButtonLocator.click();
        return new LoanCalculatorPage(page);
    }
}
