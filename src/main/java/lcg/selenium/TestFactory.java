/*
 * Copyright (c) 2015, Speroteck Inc. (www.speroteck.com)
 * and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Speroteck or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package lcg.selenium;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.gargoylesoftware.htmlunit.WebClient;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.thoughtworks.selenium.SeleniumLogLevels;
import lcg.selenium.pages.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Base test behaviour @before, @after, @rules method tor Test cases and log test results. Web driver initialization.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public abstract class TestFactory implements SauceOnDemandSessionIdProvider {

    public static final Logger logger = Logger.getLogger(TestFactory.class);
    private static final String SAUCE_LABS_URL_ENDING = "@ondemand.saucelabs.com:80/wd/hub";
    private static final String BROWSER_TYPE_IE = "ie";

    protected static SauceOnDemandAuthentication authentication;

    /* Config: received from command arguments */
    private static boolean enableJavascript = false;
    private static boolean sslEnabled = false;
    private static String baseUrl = "https://www.katespade.com/";
    private static String secureBaseUrl;
    private static String[] sauceLabsParameters;
    protected static String sauceLabsSession;
    private static String sessionId;
    private static String browser;
    private static String buildDir;
    private static String testResultsDir;

    private static ExtentReports extent;
    private static ExtentTest test;


    protected WebDriver driver;
    protected static WebClient htmlUnitClient;

    /* Test Case output required Details */
    public static final String FAILED = "FAILED";
    public static final String PASSED = "PASSED";

    protected static String testCaseStatus;
    protected static String testResults;
    protected static String testEmail;
    protected static String testOutput;
    protected static String testCaseName;

    private static Timestamp testStartTimestamp;
    private static long testStartTime;
    private static long testCaseExecutionTime;

    /* Page Object: Pages declaration */
    private Header header;
    private Footer footer;
    private HomePage homePage;
    private LoginSignUpPage loginSignUpPage;
    private MyAccountPages myAccountPages;
    private ProductDetailsPage productDetailsPage;
    private ShoppingCartPage shoppingCartPage;
    private CheckoutLoginPage checkoutLoginPage;
    private CheckoutShippingPage checkoutShippingPage;
    private CheckoutBillingPage checkoutBillingPage;
    private CheckoutOrderReviewPage checkoutOrderReviewPage;
    private CheckoutSuccessPage checkoutSuccessPage;

    /**
     * JUnit Rule which will record the test name of the current test.
     * This is referenced when creating the {@link DesiredCapabilities},
     * so that the Sauce Job is created with the test name.
     */
    @Rule
    public TestName testName = new TestName();

//    @Rule
//    public ScreenShotOnFailure failure = new ScreenShotOnFailure(driver);


    /**
     * Custom test Rule to create single {@link TestWatcher} Rule based on
     * parameters in command line. this have to be passed for the
     * {@link SauceOnDemandTestWatcher} correct construction.
     */
    @Rule
    public TestWatcherMaker watcherMaker = new TestWatcherMaker(this);

    /**
     * {@link TestWatcher} Rule that handles test passed/failed actions.
     * {@link SauceOnDemandTestWatcher} or {@link TestWatcher} Instance
     */
    @Rule
    public TestWatcher watcher = watcherMaker.getWatcher();

    protected static void failActions(Throwable e) {
        testCaseStatus = FAILED;
        log(e.getMessage());
        logTestResultError("Exception! " + e);
        logTestResultError("Test: " + testCaseName + " failed!");
        logTestOutputToXML();
        logger.info("=====================================================================");

        test.pass(FAILED);
    }

    protected static void successActions() {
        testCaseStatus = PASSED;
        logTestResult("Test: " + testCaseName + " passed!");
        logTestOutputToXML();
        //TODO: move to the line decorator method with automatic length calculation
        logger.info("=====================================================================");
        test.pass(PASSED);
    }

    /**
     * Read and Setup Parameters Once for every class with @Tests.
     */
    @BeforeClass
    public static void setUpClass() {
        setupParamsFromCmd();
        extent = ExtentManager.GetExtent();
    }

    /**
     * WebDriver initialization method. Called before every @Test.
     */
    @Before
    public void setUp() {
        cleanTestCaseResults(); // for new test
        long start = System.currentTimeMillis();
        logger.info("Start Browser...");
        if (sauceLabsSession != null){
            createSauceLabsSession();
        } else {
            openChosenBrowser();
        }
        createHtmlUnitClient();
        setSessionId(((RemoteWebDriver)driver).getSessionId().toString());
        long duration = System.currentTimeMillis() - start;
        logger.info("Start Browser... (done) | time=" + duration + "ms");
        setTestCaseName(testName.getMethodName());
        setStartTimeMark(); //for new test
        test = extent.createTest(testCaseName, "Some Hardcoded value");
        log("Starting test... " + testCaseName);
    }

    /**
     * Static Field setter, to fix non-static access
     *
     * @param value  Current(executing) test name
     * TODO: Investigate possible collisions on multi-tests runs. And multi-thread running.
     */
    private static void setTestCaseName(String value){
        testCaseName = value;
    }

    /**
     * Read all Properties placed in the command line. And set values to class variables.
     */
    private static void setupParamsFromCmd() {
        // Moving here:  get and set all properties
        long start = System.currentTimeMillis();
        setLoggingLevel();
        logger.info("Load settings...");
        setBrowser();
        setEnabledJavascript();
        setBaseUrls();
        setSSLProperty();
        setSauceLabsConfigs();
        setFolders();
        long setupDuration = System.currentTimeMillis() - start;
        logger.info("Load settings... (done) | time=" + setupDuration + "ms");
    }

    /**
     * Method will set specified in command line level for logging
     */
    private static void setLoggingLevel() {
        String logLevel = System.getProperty("logLevel");
        List<String> validLevels = Arrays.asList(SeleniumLogLevels.INFO, SeleniumLogLevels.WARN, SeleniumLogLevels.ERROR);
        if (logLevel == null || logLevel.isEmpty() || !isInArrayIgnoreCase(logLevel, validLevels)) {
            PropertyConfigurator.configure(SeleniumLogLevels.INFO.toUpperCase() + ".properties");
        } else {
            PropertyConfigurator.configure(logLevel.toUpperCase() + ".properties");
        }
    }

    /**
     * Handles Supported Browsers name list for checks
     *
     * @return List of Browser which could be launched by current Framework
     */
    private static List<String> getSupportedBrowsers(){
        return Arrays.asList(BrowserType.FIREFOX, BrowserType.CHROME, BrowserType.HTMLUNIT, BROWSER_TYPE_IE);
    }

    /**
     * {@link #browser} filed setter. Sets browser form command line
     * to one of accepted values: 'chrome' | 'firefox' | 'htmlunit' | 'ie'
     * if browser not recognized it will be set to "chrome"
     */
    private static void setBrowser() {
        String flag = System.getProperty("browser");
        List<String> supportedBrowsers = getSupportedBrowsers();
        if (flag != null && isInArrayIgnoreCase(flag, supportedBrowsers)){
            browser = flag.toLowerCase();
            logger.info("Specified Browser is <" + flag + ">");
        } else {
            browser = BrowserType.CHROME;
            logger.warn("Browser is not specified. Using default: " + browser);
        }
    }

    /**
     * Enables JS for HTMLUnit driver.
     */
    private static void setEnabledJavascript() {
        enableJavascript = System.getProperty("enableJS") != null;
    }

    /**
     * Separated function to keep the correct order setup
     * {@link #setBaseUrl()} must precede {@link #setSecureBaseUrl()}
     */
    private static void setBaseUrls() {
        setBaseUrl();
        setSecureBaseUrl();
    }

    /**
     * Unsecured protocol URL {@link #baseUrl} setter.
     * Leaves default URL if protocol check fails.
     * Also adds slash character to the end.
     */
    private static void setBaseUrl() {
        String commandLineUrl = System.getProperty("baseUrl");
        if ( commandLineUrl != null && commandLineUrl.contains("http://")){
            logger.info("baseUrl was specified in command line. Using value: " + commandLineUrl);
            baseUrl = commandLineUrl.replace("https", "http"); //always cut off 's' - base URL should be https
        } else {
            logger.warn("baseUrl wasn't specified. Using Default: " + baseUrl);
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
    }

    /**
     * Setter. Secured URL must have for checkout and my account pages
     */
    private static void setSecureBaseUrl() {
        secureBaseUrl = baseUrl.replace("http", "https");
    }

    /**
     * Set trigger for universal URL getter mehtod: {@link #getStartURL()}
     */
    private static void setSSLProperty() {
        sslEnabled = System.getProperty("sslEnabled") != null && "yes".equals(System.getProperty("sslEnabled"));
    }

    /**
     * Cleaning all stored data before execute another test.
     */
    private static void cleanTestCaseResults() {
        testCaseStatus = "";
        testResults = "";
        testEmail ="";
        testOutput = "";
        testCaseExecutionTime = 0;
    }

    private static void setStartTimeMark() {
        java.util.Date date = new java.util.Date();
        testStartTimestamp = new Timestamp(date.getTime());
        testStartTime = System.currentTimeMillis();
    }

    /**
     * Triggers SauceLabs configs setup based on "-DsauceLabsSession" presence
     * which actually switches tests to run in remote SauceLabs Browser.
     */
    private static void setSauceLabsConfigs() {
        sauceLabsSession = System.getProperty("sauceLabsSession");
        if (sauceLabsSession != null){
            setSauceLabsSessionParameters();
            setSauceLabsApiParameters();
        }
    }

    /**
     * Parser for "sauceLabsSession" command line parameter.
     * Sets {@link #sauceLabsParameters} based on parsed details
     */
    private static void setSauceLabsSessionParameters() {
        assertFalse("Command line parameter: '-DsauceLabsSession' cannot be empty!",
                sauceLabsSession.isEmpty());
        if (sauceLabsSession.contains("IE")){
            sauceLabsSession = sauceLabsSession.replace("IE", "internet explorer");
        }
        if (sauceLabsSession.contains("OSX")){
            sauceLabsSession = sauceLabsSession.replace("OSX", "OS X ");
        }
        sauceLabsParameters = sauceLabsSession.split("\\*");
        assertTrue("'-DsauceLabsSession' format error! Should have the following syntax: " +
                "\"OS*version*browser\"", sauceLabsParameters.length >= 3);
    }

    /**
     * Constructs a new instance, first attempting to populate the
     * username/access key from system properties/environment variables.
     * If none are found, then attempt to parse a ~/.sauce-ondemand file.
     */
    private static void setSauceLabsApiParameters() {
        authentication = new SauceOnDemandAuthentication();
    }

    /**
     * setFolders() method can cause asynchronous 'buildDir' location
     * if a separate 'buildDir' was defined in build.gradle for some Job/Test
     */
    private static void setFolders() {
        if(System.getProperty("buildDir") != null){
            buildDir = "./" + System.getProperty("buildDir");
        } else {
            buildDir = "./target";
            logger.warn("Using Default Build Directory: "+ buildDir);
        }
        if (testResultsDir == null) {
            String folderPath = "/logs/Run" + new Date().getTime() + "/";
            testResultsDir = buildDir + folderPath;
        }
        logger.info("Test results directory is: "+ testResultsDir);
    }

    /**
     * Creates connection to SauceLabs REST API
     * and launches specified remote browser on the SauceLabs Service.
     */
    private void createSauceLabsSession() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, sauceLabsParameters[2]);
        capabilities.setCapability(CapabilityType.VERSION, sauceLabsParameters[1]);
        capabilities.setCapability(CapabilityType.PLATFORM, sauceLabsParameters[0]);
        capabilities.setCapability("name", testName.getMethodName());
        driver = new RemoteWebDriver(getSauceLabsAPIAddress(), capabilities);
    }

    /**
     * Creates SauceLabs API URL to be used by {@link RemoteWebDriver}
     *
     * @return  <code>URL</code> object
     *
     * @throws  TestFrameworkRuntimeException
     *          Encapsulated {@link MalformedURLException} if no protocol
     *          is specified, or an unknown protocol is found or
     *          {@code spec} is {@code null}.
     */
    private static URL getSauceLabsAPIAddress() {
        try {
            return new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey()
                    + SAUCE_LABS_URL_ENDING);
        } catch (MalformedURLException e){
            throw new TestFrameworkRuntimeException(e);
        }
    }

    /**
     * Open specified browser to run tests in.
     * Browser selection based on {@link #browser} variable.
     */
    private void openChosenBrowser() {
        switch (browser){
            case BrowserType.FIREFOX:
                launchFirefox();
                break;
            case BrowserType.HTMLUNIT:
                launchHtmlUnit();
                break;
            case BROWSER_TYPE_IE:
                launchInternetExplorer();
                break;
            case BrowserType.CHROME:
            default:
                launchChrome();
                break;
       }
    }

    /**
     * Creates a Chrome driver with Maximized window and removed warning.
     * ChromeDriver must be listed in "path" system environment
     * https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    private void launchChrome() {
        setChromeDriverSystemProperty(); //set path to driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // It has issues with resizing.
        /* Chromedriver 2.10 starts with message:
        "You are using an unsupported command-line flag: --ignore-certificate-errors. Stability and security will suffer."
        More details by link: https://code.google.com/p/chromedriver/issues/detail?id=799 */
        options.addArguments("test-type"); // Fix for warning above
        driver = new ChromeDriver(options);
    }

    /**
     * Method will set automatically path to chromedriver according to the system which used
     *
     */
    private void setChromeDriverSystemProperty() {
        Platform currentPlatform = Platform.getCurrent();
        if(currentPlatform.is(Platform.WINDOWS)){ //TODO: some old hack... need to check is it still needed
            currentPlatform = Platform.WINDOWS;
        }
        switch (currentPlatform) {
            //assume chromedriver from http://code.google.com/p/chromedriver/downloads/list is one above project directory (where pom.xml is)
            case WINDOWS:
                System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe"); //FOR WINDOWS
                break;
            case WIN8_1:
                System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe"); //FOR WINDOWS
                break;
            case MAC:
                System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver"); //FOR MAC
                break;
            case UNIX:
                System.setProperty("webdriver.chrome.driver", "../chromedriver"); //FOR UNIX
                break;
            default:
                logger.error("CHROME IS UNSUPPORTED ON THIS OS:" + currentPlatform);
        }
    }

    /**
     * Creates a Firefox driver with Maximized window.
     * FFDriver must be listed in "path" system environment
     */
    private void launchFirefox() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    /**
     * Creates a headless HTML Unit driver with javascript enabled or disabled
     * based on {@link #enableJavascript}.
     */
    private void launchHtmlUnit() {
        driver = new HtmlUnitDriver(enableJavascript);
        //fix to remove odd warnings in log
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        if (!enableJavascript){
            logger.warn("Note: HTML Unit launched with disabled JS by default");
        }
    }

    /**
     * Creates iedriverserver with Maximized window.
     * One of http://code.google.com/p/selenium/downloads/list
     * is in one above project directory (where build.gradle is)
     */
    private void launchInternetExplorer() {
        //TODO: refactor to use system environment
        System.setProperty("webdriver.ie.driver", "../IEDriverServer.exe");
        driver = new InternetExplorerDriver();
        //TODO: automate http auth enable depending on presence of credentials in URL. And restore settings after.
        /*NOTE: To allow http auth:
        Had to update add iexplore.exe and explorer.exe DWORD values to the following regedit location, and set their values to 0
        HKEY_LOCAL_MACHINE\Software\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_HTTP_USERNAME_PASSWORD_DISABLE */
        driver.manage().window().maximize(); //maximize the browser window
    }

    /**
     * Additional headless client just for StatusCode validation
     * before open any URL.
     */
    private static void createHtmlUnitClient() {
        //TODO: implement initialization based on flag and StatusCode Verification based on htmlUnitClient presence
        htmlUnitClient = new WebClient();
    }

    /**
     * Main actions needed after every test.
     */
    @After
    public void tearDown() {
        long endTime = System.currentTimeMillis();
        setTestCaseExecutionTime(endTime - testStartTime); //TODO: counter should be moved out to "new Stopwatch()" Rule.
        logger.info("Finishing test.... " + testCaseName);
        if(htmlUnitClient != null) {
            htmlUnitClient.close();
        }
        driver.quit();
    }

    @AfterClass
    public static void tearDownClass() {
        //extent.endTest(test);//earlier version
        extent.flush();
        //extent.close();
    }

    /**
     * Log to test output and logger.info
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public static void log(String msg) {
        logger.info(msg);
        test.log(Status.INFO, msg); //extent
        testOutput += msg + "\n";

    }

    /**
     * Log to testResults and System.out.println
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public static void logTestResult(String msg) {
        logger.info(msg);
        test.log(Status.INFO, msg); //extent
        testResults += msg + "\n";
    }

    /**
     * Log to testResults and logger.info
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public static void logTestResultError(String msg) {
        logger.error(msg);
        test.log(Status.FAIL, msg); //extent
        testResults += msg + "\n";
    }


    /**
     * Method for returning page by request
     *
     * @return LoginSignUpPage
     */
    public LoginSignUpPage loginSignUpPage() {
        if (loginSignUpPage == null) {
            loginSignUpPage = new LoginSignUpPage(driver);
        }
        return loginSignUpPage;
    }

    /**
     * Method for returning page by request
     *
     * @return Header
     */
    public Header header() {
        if (header == null) {
            header = new Header(driver);
        }
        return header;
    }

    /**
     * Method for returning page by request
     *
     * @return Header
     */
    public Footer footer() {
        if (footer == null) {
            footer = new Footer(driver);
        }
        return footer;
    }

    /**
     * Method for returning page by request
     *
     * @return MyAccountPages
     */
    public MyAccountPages myAccountPages() {
        if (myAccountPages == null) {
            myAccountPages = new MyAccountPages(driver);
        }
        return myAccountPages;
    }

    /**
     * Method for returning page by request
     *
     * @return HomePage
     */
    public HomePage homePage() {
        if (homePage == null) {
            homePage = new HomePage(driver);
        }
        return homePage;
    }

    /**
     * Method for returning page by request
     *
     * @return HomePage
     */
    public ProductDetailsPage productDetailsPage() {
        if (productDetailsPage == null) {
            productDetailsPage = new ProductDetailsPage(driver);
        }
        return productDetailsPage;
    }

    /**
     * Method for returning page by request
     *
     * @return ShoppingCartPage
     */
    public ShoppingCartPage shoppingCartPage() {
        if (shoppingCartPage == null) {
            shoppingCartPage = new ShoppingCartPage(driver);
        }
        return shoppingCartPage;
    }

    /**
     * Method for returning page by request
     *
     * @return CheckoutLoginPage
     */
    public CheckoutLoginPage checkoutLoginPage() {
        if (checkoutLoginPage == null) {
            checkoutLoginPage = new CheckoutLoginPage(driver);
        }
        return checkoutLoginPage;
    }

    /**
     * Method for returning page by request
     *
     * @return CheckoutShippingPage
     */
    public CheckoutShippingPage checkoutShippingPage() {
        if (checkoutShippingPage == null) {
            checkoutShippingPage = new CheckoutShippingPage(driver);
        }
        return checkoutShippingPage;
    }

    /**
     * Method for returning page by request
     *
     * @return CheckoutBillingPage
     */
    public CheckoutBillingPage checkoutBillingPage() {
        if (checkoutBillingPage == null) {
            checkoutBillingPage = new CheckoutBillingPage(driver);
        }
        return checkoutBillingPage;
    }

    /**
     * Method for returning page by request
     *
     * @return CheckoutOrderReviewPage
     */
    public CheckoutOrderReviewPage checkoutOrderReviewPage() {
        if (checkoutOrderReviewPage == null) {
            checkoutOrderReviewPage = new CheckoutOrderReviewPage(driver);
        }
        return checkoutOrderReviewPage;
    }



    /**
     * Method for returning page by request
     *
     * @return CheckoutSuccessPage
     */
    public CheckoutSuccessPage checkoutSuccessPage() {
        if (checkoutSuccessPage == null) {
            checkoutSuccessPage = new CheckoutSuccessPage(driver);
        }
        return checkoutSuccessPage;
    }

    /**
     * Log all parameters to the XML log file.  This method creates a unique
     * filename by appending the current time in milliseconds to the name
     * of the file.  For example: testCaseName1381859201591.json
     *
     * timeStamp             time the test started
     * testCaseName          class name of the test
     * Status                one of pass | fail | test error
     * results               passing or failing validation results.
     * userName              the robot's email address
     * testCaseExecutionTime total execution time of the test
     * browser               which browser or selenium driver (i.e., IOs)
     * stdout_output         the output from the test itself
     * tags                  any tags that might identify the test (e.g., Smoke)
     * sessionId             the test_run sessionId for grouping tests together
     * TODO: Refactor!!
     * TODO: Implement connection between executed tests per single run to create reports.
     */
    public static void logTestOutputToXML() {
        HandleXML testResultXML = new HandleXML();

        testResultXML.addParentNode("test");
        testResultXML.appendChildNodeWithText("test", "timestamp", testStartTimestamp.toString());
        testResultXML.appendChildNodeWithText("test", "session_id", sessionId);
        testResultXML.appendChildNodeWithText("test", "browser", browser);
        testResultXML.appendChildNodeWithText("test", "testcase", testCaseName);
        testResultXML.appendChildNodeWithText("test", "status", testCaseStatus);
        testResultXML.appendChildNodeWithText("test", "results", testResults);
        testResultXML.appendChildNodeWithText("test", "user", testEmail);
        testResultXML.appendChildNodeWithText("test", "duration", String.valueOf(testCaseExecutionTime));
        testResultXML.appendChildNodeWithText("test", "stdout", testOutput);
        testResultXML.writeXMLtoFile(testResultsDir + testCaseName + String.valueOf(new Date().getTime()));
    }

    /**
     * Parametrised setter.
     *
     * @param  value
     *         long number to set as {@link #testCaseExecutionTime}
     */
    public static void setTestCaseExecutionTime(long value) {
        testCaseExecutionTime = value;
    }

    /**
     * Parametrised setter.
     *
     * @param  value
     *         String to set as {@link #sessionId}
     */
    public static void setSessionId(String value) {
        sessionId = value;
        logger.info("sessionID is " + sessionId);
    }

    /**
     * @return  String current session ID
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Selects what baseURL to use secured on not depending on environment prefix. Direct access should be avoided.
     *
     * @return String baseURL||BaseURLSecure
     */
    public static String getStartURL() {
        if (sslEnabled) {
            return secureBaseUrl;
        } else {
            return baseUrl;
        }
    }

    /**
     * Method to generate a random email user. This returns a domain address.
     *
     * @return String random email address
     */
    public static String getUniqueEmail() {
        return getUniqueEmail("testy_testy");
    }

    /**
     * Method to generate a random email user. This returns a julep address.
     *
     * @param login String first part of email address
     * @return String random email address
     */
    public static String getUniqueEmail(String login) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDateAndTime = new Date();
        String dateTime = dateFormat.format(currentDateAndTime);
        return login.concat(dateTime.concat("@domain.com"));
    }

    /**
     * @return  {@link WebClient} instance or null if was not created
     */
    public static WebClient getHtmlUnitClient(){
        return htmlUnitClient;
    }

    /**
     * Case insensitive {@link List#contains(Object)} implementation.
     *
     * @param   search
     *          The {@code String} to compare against List items
     * @param   list
     *          The {@code List<String>} to be searched in
     * @return  True if string was found in given array otherwise False
     */
    public static boolean isInArrayIgnoreCase(String search, List<String> list){
        for (String string : list){
            if (string.equalsIgnoreCase(search)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a random integer in the range 0 to n-1 inclusive.
     * The lowest possible value is 0 + 1000000000 = 1000000000
     * The highest possible value is 9000000000-1 + 1000000000 = 9999999999
     *
     * @return  Generated number converted to <code>String</code>
     */
    public static String generate10DigitString() {
        return String.valueOf((long) Math.floor(Math.random() * 9000000000L) + 1000000000L);
    }

    /**
     * Set a 10-digit to sessionId for tracking test sessions.
     */
    //TODO: Investigate current implementation and remove redundant code
    public static void setSessionId() {
        setSessionId(generate10DigitString());
    }
}
