package tests;

import pages.LoanCalculatorPage;
import pages.LoansHubPage;
import steps.LoanCalculatorSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoanCalculatorTest extends BaseTest {

    @Test(description = "KAN-T17 | Calculate monthly loan payment with the loan calculator")
    public void changingLoanAmountRecalculatesMonthlyPayment() {
        LoanCalculatorSteps steps = new LoanCalculatorSteps(page);
        LoansHubPage loansHub = steps.navigateToLoansHubViaHomepage();
        assertVisible(loansHub.getALoanButton());

        LoanCalculatorPage calculator = steps.openCalculator(loansHub);
        String paymentBefore = calculator.getMonthlyContributionText();

        String paymentAfter = steps.changeLoanAmount(calculator, "120000");

        Assert.assertNotEquals(paymentAfter, paymentBefore,
                "Monthly contribution should be recalculated when the loan amount changes");
    }
}
