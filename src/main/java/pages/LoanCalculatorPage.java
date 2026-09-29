package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import constants.UrlConstants;
import utils.ConfigReader;

/**
 * The mortgage loan calculator on /en/loans/mortgage. The amount field has no
 * accessible label (the design-system component only exposes a placeholder),
 * so it is targeted by that placeholder; the animated result counter renders
 * one <span> per character, so its value is read via textContent rather than
 * innerText to avoid picking up layout-driven line breaks between digits.
 */
public class LoanCalculatorPage extends BasePage {

    private final Locator amountInput;
    private final Locator monthlyContributionValue;

    public LoanCalculatorPage(Page page) {
        super(page);
        this.amountInput = page.locator("input[placeholder='Income']").first();
        this.monthlyContributionValue = page.locator(".tbcx-pw-calculated-info__number--new").first();
        waitUntilCalculatorHydrated();
    }

    public LoanCalculatorPage open() {
        open(UrlConstants.MORTGAGE_LOAN_EN);
        return this;
    }

    /**
     * The calculator renders a skeleton placeholder until its initial figures
     * are fetched, regardless of whether this page was reached by a direct
     * navigate() or by clicking through from the loans hub - so this waits
     * for the real amount value to attach rather than relying on page load events.
     */
    private void waitUntilCalculatorHydrated() {
        PlaywrightAssertions.assertThat(amountInput).not().hasValue("",
                new LocatorAssertions.HasValueOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
    }

    /**
     * Sets a new loan amount and waits - via a web-first assertion, not a sleep -
     * for the recalculated monthly contribution to actually differ from before,
     * then returns the new figure.
     */
    public String enterLoanAmountAndAwaitRecalculation(String amount) {
        String contributionBefore = monthlyContributionValue.textContent().trim();
        amountInput.fill(amount);
        PlaywrightAssertions.assertThat(monthlyContributionValue).not().hasText(contributionBefore,
                new LocatorAssertions.HasTextOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
        return monthlyContributionValue.textContent().trim();
    }

    public String getMonthlyContributionText() {
        return monthlyContributionValue.textContent().trim();
    }
}
