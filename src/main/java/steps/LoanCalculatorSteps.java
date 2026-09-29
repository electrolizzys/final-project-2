package steps;

import com.microsoft.playwright.Page;
import pages.HomePage;
import pages.LoanCalculatorPage;
import pages.LoansHubPage;

/**
 * Business flow for the loan-calculator scenario: homepage card -> loans hub
 * -> "GET A LOAN" -> mortgage calculator, mirroring the documented user journey.
 */
public class LoanCalculatorSteps {

    private final Page page;

    public LoanCalculatorSteps(Page page) {
        this.page = page;
    }

    public LoansHubPage navigateToLoansHubViaHomepage() {
        new HomePage(page).open().clickLoanCalculatorCard();
        return new LoansHubPage(page);
    }

    public LoanCalculatorPage openCalculator(LoansHubPage loansHub) {
        return loansHub.clickGetALoan();
    }

    public String changeLoanAmount(LoanCalculatorPage calculator, String amount) {
        return calculator.enterLoanAmountAndAwaitRecalculation(amount);
    }
}
