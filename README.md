# TBC Digital – Advanced Test Automation

Automation framework for [tbcbank.ge](https://tbcbank.ge) built with **Java 21, Playwright, TestNG, Maven, MyBatis (H2) and Rest Assured**. It covers UI journeys, localization, database-driven tests, API tests, API → UI consistency and browser-network validation. Every automated test is traceable to a Zephyr Scale case.

---

## How to run

**Prerequisites:** JDK 21+ and Maven 3.9+. Playwright downloads its browsers on the first run, and the H2 database is created automatically, so there is nothing else to install.

```bash
mvn test                                   # whole suite, in parallel
mvn test -Dbrowser=firefox -Dheadless=false # override settings for one run
```

| Setting | Default (`resources/config.properties`) | Override |
|---|---|---|
| `browser` | `chromium` (`firefox`, `webkit` also supported) | `-Dbrowser=...` |
| `headless` | `true` | `-Dheadless=false` |
| `baseUrl` | `https://tbcbank.ge` | `-DbaseUrl=...` |
| `defaultTimeoutMs` | `30000` (used by actions **and** assertions) | `-DdefaultTimeoutMs=...` |
| `api.baseUrl` | `https://apigw.tbcbank.ge` | `-Dapi.baseUrl=...` |

Any command-line `-D` value takes precedence over the file (`utils/ConfigReader`).

**Parallel execution:** `testng.xml` runs all 10 test classes in one `<test>` block with `parallel="classes" thread-count="4"`. Each class runs on its own thread, so rows from the same data provider run one after another. Every test gets its own browser via `ThreadLocal` instances in `utils/PlaywrightFactory`, which means tests running at the same time never share browser state. A full run is 17 test executions and takes about 40 seconds, with 4 running at once.

Reports are written to `target/surefire-reports/`.

## Test inventory and Zephyr traceability

| Zephyr | Area | Test (class → method) | Runs |
|---|---|---|---|
| KAN-T15 | UI | `LocationsTest.selectingCityUpdatesResults` | 1 |
| KAN-T16 | UI | `LocationsTest.filteringByTypeUpdatesResults` | 1 |
| KAN-T17 | UI (dynamic calculation) | `LoanCalculatorTest.changingLoanAmountRecalculatesMonthlyPayment` | 1 |
| KAN-T18 | UI (dynamic calculation) | `ExchangeRateTest.changingSellAmountUpdatesConvertedValue` | 1 |
| KAN-T19 | UI | `TransfersTest.localAndInternationalTransferDetailsOpenFromOverview` | 1 |
| KAN-T20 | Localization | `BusinessPlanLocalizationTest.startupPlanOfferIsShownInBothLocales` | 2 (EN, KA) |
| KAN-T21 | SQL + MyBatis + DataProvider | `CurrencyConversionDataDrivenTest.conversionScalesWithAmount` | 4 (one per DB row) |
| KAN-T22 | API (happy path) | `ExchangeRateApiTest.exchangeRateForValidPairReturnsRequestedPairWithValidRates` | 1 |
| – | API (negative) | `ExchangeRateValidationApiTest.exchangeRateWithoutTargetCurrencyReturnsValidationError` | 1 |
| KAN-T23 | Network validation | `LocationSearchNetworkTest.locationSearchSendsKeywordAndListMatchesResponse` | 1 |
| KAN-T24 | API → UI | `ExchangeRateApiToUiTest.rateCardMatchesCommercialRatesApi` | 3 (USD, EUR, GBP) |


---

## 9.1 Framework architecture

```
src/main/java/
  pages/        one Page Object per screen
  components/   UI pieces shared by several pages
  steps/        business actions and flows
  api/          clients/ (Rest Assured calls), models/ (JSON POJOs)
  database/     mappers/ (MyBatis), models/ (row POJOs), DB bootstrap
  utils/        ConfigReader, PlaywrightFactory
  constants/    UrlConstants
src/test/java/tests/   test classes + BaseTest
resources/             config.properties, mybatis/, testdata/
```

- **`pages/`**: each class owns the selectors for one screen and exposes small actions and reads, for example `LocationsPage.typeSearchKeyword(...)` and `ExchangeRatePage.getRateCardBuyRate("USD")`. `BasePage.open(url)` navigates, waits for the header, then dismisses the cookie banner, so every page opens the same way. Pages never contain assertions about business rules. They only use web-first assertions to *wait*, for example until a recalculated value has changed.
- **`components/`**: `HeaderComponent` (the top navigation, present on every page), `CookieBannerComponent` ("ACCEPT ALL") and `LanguageSwitcherComponent` (the header's ქარ/EN toggle). Each one owns its own selectors and behaviour, and `BasePage` creates all three, so every page object gets them without repeating a selector.
- **`steps/`**: the business layer that tests call to *do* things. UI examples are `LocationsSteps.selectCity(...)` and `BusinessLocalizationSteps.openHomepageAndSwitchLanguage(startUrl, locale)`. API examples are `ExchangeRateApiSteps`, which checks the 200 status and deserializes the response, and `ExchangeRateValidationSteps`, which does the same for the 400 case. `LocationSearchNetworkSteps` captures browser traffic. Tests call steps for actions and read page objects or returned POJOs for assertions.
- **`api/`**: `clients/ExchangeRateApiClient` builds the Rest Assured requests (base URI from config). `models/` holds the POJOs the JSON is deserialized into. They are annotated `@JsonIgnoreProperties(ignoreUnknown = true)`, because the API returns fields we don't use and a new field shouldn't break the tests.
- **`database/`**: `DatabaseInitializer` builds the H2 database from the SQL scripts over plain JDBC. `MyBatisSessionFactory` creates MyBatis sessions. `mappers/CurrencyConversionMapper` (and its XML) holds the query, and `models/CurrencyConversionRecord` is the row object.
- **`tests/`**: `BaseTest` opens a fresh browser before and closes it after every test method, and provides `assertVisible(...)` with the configured timeout. Test classes contain only the scenario flow and the assertions.

**Why components are separate from page objects.** The header and the cookie banner appear on every page. If each page object had its own copy of their selectors, one change on the site would mean editing every page. Keeping them in one class means a fix happens in one place.

A concrete case from this project: `HeaderComponent`'s logo selector was first `a[href='/en']`. That broke every page as soon as the localization test opened a Georgian (`/ka`) page, because `BasePage.open()` waits for the logo on every navigation. Changing it once to `a[href='/en'], a[href='/ka']` in `HeaderComponent` fixed all pages at the same time. The same component also provides `clickForBusiness()`, which the localization journey uses in both languages. `CookieBannerComponent` is reused the same way: every `open()` of every page goes through it. `LanguageSwitcherComponent` is exposed on every page through `BasePage.switchLanguageTo(locale)` and `currentLocale()`: the localization test switches language on `HomePage` and then reads the locale back on `StartupPlanPage`, two different page objects using the same component.

## 9.2 Localization strategy

Scenario KAN-T20 (homepage → switch language with the header toggle → *For Business* → *Free Startup Plan* offer → *Startup plan* details) is **one test method** run twice by a TestNG `@DataProvider` (`BusinessPlanLocalizationTest.locales`), once per locale.

**The language is changed through the UI, not only by URL.** Each run starts on the homepage in the *other* language (the English run starts on `/ka`, the Georgian run on `/en`) and clicks the header's language toggle. `LanguageSwitcherComponent.switchTo(locale)` then waits for the URL to carry the new prefix and for the header to re-render in that locale (no fixed wait). The test asserts the locale switched, continues the whole journey, and at the end asserts it is still in the chosen locale. This is what proves the journey keeps working after a locale change.

**Navigation doesn't depend on the language.** The links are found by the *end* of their URL, which is the same in both locales, for example `a[href$='/business']` and `a[href$='/business-subscriptions/startup-business-plan']`. Only the `/en` or `/ka` prefix differs, so the same selectors and steps work in both languages. Clicking by visible text would have needed two versions of every selector.

**Only the expected content is locale-specific.** Each data-provider row holds the start URL, the target locale (`en`/`ka`) and the three texts that must appear:

| | English | Georgian |
|---|---|---|
| Hero heading (business page) | Manage your company's finances remotely | მართეთ კომპანიის ფინანსები დისტანციურად |
| Plan card heading | Startup plan | სტარტაპ ნაკრები |
| Benefit shown on details page | Free internet and mobile banking | უფასო ინტერნეტ და მობაილ ბანკი |

The test asserts each one with `BasePage.headingWithText(exactText)`. Because the journey has to reach each page to check its text, this proves both that the content is translated and that the journey still works after the locale changes.

**Where the data lives:** in the `locales` data provider in `BusinessPlanLocalizationTest`.

**Adding another locale** (e.g. Russian):
1. Add a row with a start URL, the new locale code and the three expected texts.
2. Add its logo link to `HeaderComponent`'s logo selector (currently `/en` and `/ka`).
3. The site's toggle only swaps between two languages, so a third language would need `LanguageSwitcherComponent.switchTo` to pick from a list instead of clicking the toggle. That change stays inside the component; the test and steps don't change.

No test logic changes.

A real finding along the way: on the English details page, some benefit lines (the e-commerce and POS-terminal ones) are still in Georgian. That's why the test checks a line that is translated in both languages.

## 9.3 SQL and test data strategy

**Why SQL/MyBatis.** The currency conversion scenario (KAN-T21) should cover many currency pairs without a new test for each one. Keeping those pairs in a database table separates the test *data* from the test *logic*, and MyBatis turns each row into a Java object without hand-written `ResultSet` code. H2 runs embedded in file mode (`./data/tbc_digital_db`), so it's a real SQL database with no server to install.

**Flow: Database → MyBatis → Java model → DataProvider → test**

1. **Database:** `resources/testdata/schema.sql` creates the `currency_conversions` table (`from_currency`, `to_currency`, `amount`), and `resources/testdata/data.sql` inserts the rows. `DatabaseInitializer` runs both scripts once per test run, so `data.sql` always matches what the tests use.
2. **MyBatis:** `resources/mybatis/mybatis-config.xml` reads the connection settings from `config.properties`. `CurrencyConversionMapper.xml` holds `selectAll` and a `resultMap` that maps `from_currency` to `fromCurrency`, and so on.
3. **Java model:** each row becomes a `database.models.CurrencyConversionRecord`.
4. **DataProvider:** `CurrencyConversionDataDrivenTest.currencyConversions()` opens a session, calls `CurrencyConversionMapper.selectAll()`, and returns one row per record.
5. **Test:** `conversionScalesWithAmount(record)` runs once per record. It opens `/en/valutis-kursi/{from}-to-{to}?amount={amount}`, checks the sell field contains the database amount and the converted amount is positive, then doubles the amount and checks the converted value also doubles (within 1%).

**Adding a new variation** takes one line in `data.sql`:

```sql
INSERT INTO currency_conversions (from_currency, to_currency, amount) VALUES ('USD', 'GEL', 50);
```

The next run executes the test a 5th time for that row, with no Java change and no manual database cleanup.

## 9.4 API → UI strategy

**API:** `GET https://apigw.tbcbank.ge/api/v1/exchangeRates/commercialList?locale=en-US`. This is the request tbcbank.ge itself sends to fill the exchange rates page, found by recording the page's network traffic. It's public, so it needs no API key. The TBC Developer Portal's exchange-rate API was considered, but it returns `401` without a registered key.

**Test (KAN-T24, `ExchangeRateApiToUiTest`)**, run for USD, EUR and GBP (the three currencies the page shows as cards):
1. `CommercialRatesApiSteps` calls the API, checks for `200`, deserializes the response into `CommercialRatesResponse` (a `rates` list of `CommercialRate`) and picks the currency.
2. The page `/en/valutis-kursi` is opened with Playwright.
3. The currency's card is compared with the API, **which is the source of truth**:

| API field | UI | Why this value |
|---|---|---|
| `name` | card title ("1 " + name, e.g. "1 U.S. Dollar") | proves the card shows the right currency |
| `buyRate` | card *Buy* | the rate a customer gets when selling to the bank |
| `sellRate` | card *Sell* | the rate a customer pays when buying |
| `officialCourse` | card *Official rates* | the National Bank reference rate |

Rates are compared as numbers (tolerance `0.00001`), because the page trims trailing zeros (API `3.530`, page `3.53`).

**Inconsistencies this catches:** the page showing stale or cached rates, buy and sell swapped, wrong rounding or truncation, a card showing another currency's data, and a currency missing from either the API or the page.

## 9.5 Network validation

**Scenario (KAN-T23, `LocationSearchNetworkTest`)** on the ATM/branch locator `/en/atms&branches`.

- **UI action that triggers it:** typing "Rustavi" key by key into the "Specify the desired location" search box. The page waits for typing to pause and then sends a single search request.
- **Monitored request:** `POST https://apigw.tbcbank.ge/api/v1/atmsAndBranches/list` with a JSON body such as `{"filter":[],"locale":"en-US","keyword":"Rustavi",...}`.
- **Synchronization:** `LocationSearchNetworkSteps` calls `page.waitForResponse(predicate, () -> type keyword)`. The listener is registered *before* typing starts, and it only accepts the response whose request body contains exactly `keyword = "Rustavi"`. There is no sleep.
- **Validated at network level:**
  - the endpoint path
  - the method is `POST`
  - the status is `200`
  - the request body's `keyword` equals the typed text, and `filter` is empty (no type tab selected)
  - the response list is not empty
  - every returned location mentions Rustavi in its address, region, location or name
- **Validated on the UI afterwards:** the results list shows exactly as many items as the response returned (a web-first `hasCount`), and the first result contains "Rustavi". That ties what the user sees directly to the captured network data.

## 9.6 Test stability

**Scenario analysed: the network validation test (KAN-T23).** It combines typing, an asynchronous request, and a list that re-renders after the response arrives. Sources of instability and how the implementation handles them without fixed waits or retries:

1. **The same endpoint is called on page load.** `/en/atms&branches` requests `atmsAndBranches/list` with an empty keyword as it loads, so a listener that matched on the URL alone could capture that response instead of the search result. *Mitigation:* the `waitForResponse` predicate also parses the request body and requires `keyword` to equal the typed text exactly.
2. **Typing can produce requests for partial words.** A search box can send a request for "Rus" before "Rustavi" is finished. *Mitigation:* the same exact-keyword match ignores any request for a partial word, whether or not one is sent.
3. **The response can arrive before anyone listens.** Starting to listen *after* typing could miss a fast response. *Mitigation:* `waitForResponse(predicate, action)` registers the listener first and then runs the typing inside it.
4. **The list renders after the response.** Reading the item count immediately could return the old list. *Mitigation:* a web-first `assertThat(results).hasCount(n)`, which retries until the list matches or the configured timeout runs out.
5. **The initial list could overwrite the search results.** If typing started while the first list was still loading, the late initial response could replace the search results. *Mitigation:* the test waits for the first result to be visible before typing.
6. **A covering cookie banner.** *Mitigation:* `BasePage.open()` always dismisses it through `CookieBannerComponent`.

**Framework-wide measures that came out of real failures:**
- **Assertion timeout.** Playwright's `assertThat` has its own 5-second default, separate from the context's action timeout, so waits were silently cut short when tests ran in parallel. Every web-first assertion (page waits and `BaseTest.assertVisible`) now passes the configured `defaultTimeoutMs` explicitly.
- **Isolation.** Each test gets its own browser through `ThreadLocal` in `PlaywrightFactory`, so parallel tests share no state.
- **Hidden duplicate links.** The mega-menu keeps hidden copies of many links (for example several `a[href='/en/loans/mortgage']`), so selectors are narrowed to the visible element, or combined with the accessible name, as with "GET A LOAN".
- **Lazy loading.** The homepage adds its quick-action cards only when they're scrolled near. `HomePage` scrolls step by step and checks whether the card exists after each step, instead of sleeping.
- **Calculators that load after the page.** The mortgage and currency calculators show an empty placeholder first, so the page objects wait for a real value before reading.

**Known limitations**
- When four Chromium instances start at the same moment on a slow machine, the first batch of tests occasionally times out waiting for the header. A re-run passes. No automatic retry was added, so that real failures stay visible.
- The API → UI test can in theory fail if TBC updates a rate between the API call and the page load. The two happen seconds apart and rates change rarely.
- `apigw.tbcbank.ge` is the site's own internal API with no published contract, so TBC could change its response fields without notice.
- **Firewall rate limiting.** After many full runs in a short time, TBC's firewall can temporarily block the machine's IP. Both the API and the pages' own data requests then return `403`, and API tests plus data-driven page tests fail together. This is an environment issue, not a test failure: wait and re-run, or run from another network. 
