package constants;

/**
 * Fixed test inputs and comparison tolerances, kept out of the test classes
 * so those contain only test methods.
 */
public final class TestDataConstants {

    /** Keyword typed into the locations search box in the network-validation test. */
    public static final String LOCATION_SEARCH_KEYWORD = "Rustavi";

    /** Rates are shown with trailing zeros trimmed (API 3.530 -> UI "3.53"), so they are compared numerically. */
    public static final double RATE_TOLERANCE = 0.00001;

    private TestDataConstants() {
    }
}
