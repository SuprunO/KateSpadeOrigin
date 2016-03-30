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

package custom.selenium;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import custom.selenium.pages.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;
import static org.openqa.selenium.Platform.WINDOWS;


/**
 * Base test behaviour @before, @after, @rules method tor Test cases and log test results. Web driver initialization.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public abstract class TestFactory implements SauceOnDemandSessionIdProvider {
//TODO: wirte Javadoc for all methods
    protected static WebDriver driver;
    private static SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("azakowski", "52368b71-d5b9-4df3-8ef1-5fbb27c6f780");
    public static Logger logger = Logger.getLogger(TestFactory.class);
    private static final String DEFAULT_LOGGING_LEVEL = "INFO";

    /* Config: received from command arguments */

    private static boolean sslEnabled = false;
    private static String baseUrl = "http://lea-magedev.lcgosc.com/";
    private static String secureBaseUrl;
    private static String[] sauceLabsParameters;
    private static String sauceLabsSession;
    private static String sessionId;
    private String browser;

    private static String buildDir;
    private static String testResultsDir;

    /* Page Object: Pages declaration */
    private HomePage homePage;
    private LoginSignUpPage loginSignUpPage;
    private MyAccountPages myAccountPages;
    private Header header;
    private Footer footer;

    /* Test Case required Details */

    public static final String FAIL = "FAIL";
    public static final String PASS = "PASS";




    private String testCaseStatus;
    static String testResults = "";
    public static String testEmail = "";
    static String testOutput = "";
    private String tags = "";
    protected String testCaseName;

    private static Timestamp testStartTimestamp;
    private static long testStartTime;
    private long testCaseExecutionTime;

    @Before
    public void setUp() throws MalformedURLException {
        setupConfigFromParams();
        cleanTestCaseResults(); // for new test
        setStartTimeMark(); //for new test
        if (sauceLabsSession != null){
            createSauceLabsSession();
        } else {
            setSessionId();
            openChosenBrowser();
        }
    }

    /**
     * Read all Properties placed in the command line. And set values to class variables.
     */
    private void setupConfigFromParams() {
        // Moving here:  get and set all properties
        setLoggingLevel();
        setBaseUrls();
        setSSLProperty();
        setSauceLabsConfigs();
        setFolders();
    }

    private static void setFolders() {
        if(System.getProperty("buildDir") != null){
            buildDir = "./" + System.getProperty("buildDir");
        } else {
            buildDir = "./target";
            logger.warn("Using Default Build Directory: "+ buildDir);
        }
        testResultsDir = buildDir + "/logs/";
        logger.info("Test results directory is: "+ testResultsDir);
   }

    private static void setSSLProperty() {
        sslEnabled = System.getProperty("sslEnabled") != null && "yes".equals(System.getProperty("sslEnabled"));
    }

    /**
     * Separated function to keep the correct order setup:  setBaseUrl() must be always called before setSecureBaseUrl();
     */
    private void setBaseUrls() {
        setBaseUrl();
        setSecureBaseUrl();
    }

    private static void setBaseUrl() {
        String commandLineUrl = System.getProperty("baseUrl");
        if ( commandLineUrl != null){
            logger.info("baseUrl was specified in command line. Using value: " + commandLineUrl);
            baseUrl = commandLineUrl.replace("https", "http"); //always cut off 's' - base URL should be https
        } else {
            logger.warn("baseUrl NOT SPECIFIED. USING DEFAULT:" + baseUrl);
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
    }

    private static void setSecureBaseUrl() {
        baseUrl = baseUrl.replace("http", "https");
    }

    /**
     * Cleaning all stored data before execute another test.
     */
    private static void cleanTestCaseResults() {
        testEmail = "";
        testOutput = "";
        testResults = "";
    }

    private static void setStartTimeMark() {
        java.util.Date date = new java.util.Date();
        testStartTimestamp = new Timestamp(date.getTime());
        testStartTime = System.currentTimeMillis();
    }

    /**
     * Presence of -DsauceLabsSession in commandline means that test are going to be run in SauceLabs
     * and it contains session configuration, which should be parsed and applied.
     */
    private static void setSauceLabsConfigs() {
        sauceLabsSession = System.getProperty("sauceLabsSession");
        if (sauceLabsSession != null){
            setSauceLabsSessionParameters();
        }
    }

    /**
     * Method which parses command line parameter "sauceLabsSession" and splits it for next usage
     */
    private static void setSauceLabsSessionParameters() {
        assertFalse("COMMAND LINE PARAMETER: sauceLabsSession CAN NOT BE EMPTY! BECAUSE YOU CHOSE TO RUN TESTS IN SAUCELABS",
                sauceLabsSession.isEmpty());
        if (sauceLabsSession.contains("IE")){
            sauceLabsSession = sauceLabsSession.replace("IE", "internet explorer");
        }
        if (sauceLabsSession.contains("OSX")){
            sauceLabsSession = sauceLabsSession.replace("OSX", "OS X ");
        }
        sauceLabsParameters = sauceLabsSession.split("\\*");
        assertTrue("INCORRECT FORMAT FOR sauceLabsSessions COMMAND LINE PARAMETER. SHOULD BE LIKE THIS:" +
                " \"OS*version*browser\"", sauceLabsParameters.length >= 3);
    }

    /**
     * Method will connect with sauceLabs and create virtual machine with specified parameters
     */
    private void createSauceLabsSession() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, sauceLabsParameters[2]);
        capabilities.setCapability(CapabilityType.VERSION, sauceLabsParameters[1]);
        capabilities.setCapability(CapabilityType.PLATFORM, sauceLabsParameters[0]);
        capabilities.setCapability("name", getTestCaseName());
        driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
    }

    /**
     * Method will open specified browser, which will be used to run test
     */
    private void openChosenBrowser() {
        browser = System.getProperty("browser"); //Get browser name from the config
        if (browser == null){
            browser = "CHROME";
        }
        if (browser.equals("CHROME")) {
            //setChromeDriverSystemProperty(); //Locked to use WebDriver from system environment.
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized"); // It has issues with resizing.
            /* Chromedriver 2.10 starts with message:
            "You are using an unsupported command-line flag: --ignore-certificate-errors. Stability and security will suffer."
            More details by link: https://code.google.com/p/chromedriver/issues/detail?id=799 */
            options.addArguments("test-type");
            driver = new ChromeDriver(options);
        } else if (browser.equals("FIREFOX")) {
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        } else if (browser.equals("HTMLUNIT")) {//instantiate a headless driver with javascript enabled
            driver = new HtmlUnitDriver();
        } else if (browser.equals("HTMLUNITNOJS")) {//instantiate a headless driver with javascript disabled
            driver = new HtmlUnitDriver(false);
            //fix to remove odd warnings it log
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        } else if (browser.equals("INTERNETEXPLORER")) {//assume iedriverserver from http://code.google.com/p/selenium/downloads/list is in one above project directory (where pom.xml is)
            System.setProperty("webdriver.ie.driver", "../IEDriverServer.exe");
            driver = new InternetExplorerDriver();
            //TODO: automate http auth enable depending on presence of credentials in URL. And restore settings after.
            /*NOTE: To allow http auth:
            Had to update add iexplore.exe and explorer.exe DWORD values to the following regedit location, and set their values to 0
            HKEY_LOCAL_MACHINE\Software\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_HTTP_USERNAME_PASSWORD_DISABLE */
            // driver.manage().window().maximize(); //maximize the browser window
        } else {
            fail("UNSUPPORTED BROWSER:" + browser + " VALID BROWSERS: FIREFOX, CHROME, INTERNETEXPLORER, HTMLUNIT, HTMLUNITNOJS");
        }
    }

    /**
     * Method will set hardcoded path to chromedriver according to the system which used
     */
    private void setChromeDriverSystemProperty() {
        Platform currentPlatform = Platform.getCurrent();
        if(currentPlatform.is(WINDOWS)){
            currentPlatform = Platform.WINDOWS; //hack to simplify win versions
        }
        switch (currentPlatform) {
            //assume chromedriver from https://sites.google.com/a/chromium.org/chromedriver/downloads
            // is one above project directory (where pom.xml is)
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
     * Method will set specified in command line level for logging
     */
    private void setLoggingLevel() {
        //TODO: refactor using enumerating of existing logging level values and error/typo handling.
        String fileTypeProperties = ".properties";
        String logLevel = System.getProperty("logLevel");
        if (logLevel == null || logLevel.isEmpty()) {
            PropertyConfigurator.configure(DEFAULT_LOGGING_LEVEL + fileTypeProperties);
        } else {
            PropertyConfigurator.configure(logLevel + fileTypeProperties);
        }
    }

    private String getTestCaseName() {
        String testTag = System.getProperty("testTag");
        if(testTag == null || testTag.isEmpty()) {
            return testName.getMethodName();
        }
        return testTag + "\\" + testName.getMethodName();
    }

    /**
     * JUnit Rule which will record the test name of the current test.
     * This is referenced when creating the {@link DesiredCapabilities},
     * so that the Sauce Job is created with the test name.
     */
    @Rule
    public TestName testName = new TestName();

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     * And overriding to keep local framework functionality.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication){

        @Override
        protected void failed(Throwable e, Description d) {
            if (sauceLabsSession != null && !sauceLabsSession.isEmpty()){
                super.failed(e, d);
            }
            testCaseStatus = FAIL;
            log(e.getMessage());
            logTestResultError("Exception! " + e);
            logTestResultError("Test requirements in: " + getTestCaseName() + " failed!");
            logTestOutputToXML();
            logger.info("================================================================================");
        }

        @Override
        protected void succeeded(Description d) {
            if (sauceLabsSession != null && !sauceLabsSession.isEmpty()){
                super.succeeded(d);
            }
            testCaseStatus = PASS;
            logTestResult("Test requirements in: " + getTestCaseName() + " successfully passed!");
            logTestOutputToXML();
            logger.info("================================================================================");
        }
    };


    @After
    public void tearDown() {
        long endTime = System.currentTimeMillis();
        testCaseExecutionTime = endTime - testStartTime;
        logger.info("Finishing test.... " + getTestCaseName());
        driver.quit();
    }

    /**
     * Log to test output and logger.info
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public static void log(String msg) {
        logger.info(msg);
        testOutput += msg + "\n";
    }

    /**
     * Log to testResults and System.out.println
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public static void logTestResult(String msg) {
        logger.info(msg);
        testResults += msg + "\n";
    }

    /**
     * Log to testResults and logger.info
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public static void logTestResultError(String msg) {
        logger.error(msg);
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
     * @return MavenStylePage
     */
    public HomePage homePage() {
        if (homePage == null) {
            homePage = new HomePage(driver);
        }
        return homePage;
    }

    /**
     * Log all parameters to the JSON log file.  This method creates a unique
     * filename by appending the current time in milliseconds to the name
     * of the file.  For example: testCaseName1381859201591.json
     *
     * timeStamp             time the test started
     * testCaseName          class name of the test
     * testCaseStatus        one of pass | fail | test error
     * validationResults     passing or failing validation results.
     * userName              the robot's email address
     * testCaseExecutionTime total execution time of the test
     * browser               which browser or selenium driver (i.e., IOs)
     * stdout_output         the output from the test itself
     * tags                  any tags that might identify the test (e.g., Smoke)
     * sessionId             the test_run sessionId for grouping tests together
     * TODO: Refactor!!
     * TODO: Implement connection between executed tests per single run to create reports.
     */
    public void logTestOutputToXML() {
        HandleXML testResultXML = new HandleXML();

        testResultXML.addParentNode("test");
        testResultXML.appendChildNodeWithText("test", "timestamp", testStartTimestamp.toString());
        testResultXML.appendChildNodeWithText("test", "testcase", testCaseName);
        testResultXML.appendChildNodeWithText("test", "status", testCaseStatus);
        testResultXML.appendChildNodeWithText("test", "validationstring", testResults);
        testResultXML.appendChildNodeWithText("test", "user", testEmail);
        testResultXML.appendChildNodeWithText("test", "executiontime", String.valueOf(testCaseExecutionTime));
        testResultXML.appendChildNodeWithText("test", "browser", browser);
        testResultXML.appendChildNodeWithText("test", "stdout", testOutput);
        testResultXML.appendChildNodeWithText("test", "tags", tags);
        testResultXML.appendChildNodeWithText("test", "session_id", sessionId);
        testResultXML.writeXMLtoFile(testResultsDir + testCaseName + String.valueOf(new Date().getTime()));
    }

    /**
     * Set a 10-digit to sessionId for tracking test sessions.
     */
    public void setSessionId() {
        StringBuilder randomizedString = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            Random randInt = new Random();
            randomizedString.append(randInt.nextInt(10)).toString();
        }
        sessionId = randomizedString.toString();
        logger.info("Got the following sessionID: " + sessionId);
    }

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
    public static String getUniqueEmailAddress() {
        return getUniqueEmailAddress("testy_testy");
    }

    /**
     * Method to generate a random email user. This returns a julep address.
     *
     * @param login String first part of email address
     * @return String random email address
     */
    public static String getUniqueEmailAddress(String login) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDateAndTime = new Date();
        String dateTime = dateFormat.format(currentDateAndTime);
        return login.concat(dateTime.concat("@domain.com"));
    }
}
