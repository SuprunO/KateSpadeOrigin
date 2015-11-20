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

import custom.selenium.pages.*;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
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

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import java.io.File;

import static org.junit.Assert.*;
import static org.openqa.selenium.Platform.WINDOWS;


/**
 * Base test behaviour @before, @after, @rules method tor Test cases and log test results. Web driver initialization.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public abstract class TestFactory implements SauceOnDemandSessionIdProvider {

    public WebDriver driver;
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("azakowski", "52368b71-d5b9-4df3-8ef1-5fbb27c6f780");
    public static Logger logger = Logger.getLogger(TestFactory.class);

    /* Config: received from command arguments */

    public static boolean SSLEnabled = false;
    public static String baseUrl = "http://lea-magedev.lcgosc.com/";
    public static String secureBaseUrl;
    private static String[] sauceLabsParameters;
    private static String sauceLabsSession;
    private String browser;

    /* Page Object: Pages declaration */
    public HomePage homePage;
    public LoginSignUpPage loginSignUpPage;
    public MyAccountPages myAccountPages;
    public Header header;
    public Footer footer;

    /* Test Case required Details */
    public static final String FAIL = "FAIL";
    public static final String PASS = "PASS";
    private static String sessionId;

    static Timestamp testStartTimestamp;
    public static String testCaseName;
    static String testCaseStatus;
    static String testResults = "";
    public static String testEmail = "";
    static String testOutput = "";
    public static String tags = "";
    public static String orderTime = "";
    public static long testStartTime;

    public long testCaseExecutionTime;

    //TODO: remove the following warnings:
    //log4j:WARN No appenders could be found for logger (custom.selenium.TestFactory).
    //log4j:WARN Please initialize the log4j system properly.
    //log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

    @Before
    public void setUp() throws Exception {
        setupConfigFromCmd(); //TODO: get and set all properties here
        cleanTestCaseResults(); //for new test
        // timestamp
        setStartTimeMark();
        if (sauceLabsSession != null){
            createSauceLabsSession();
        } else {
            setSessionId();
            openChosenBrowser();
        }
        new URL(baseUrl); //TODO: why it here?
    }

    /**
     * Read all Properties placed in the command line. And set values to class variables.
     */
    private void setupConfigFromCmd() {
        // Moving here:  get and set all properties
        logger.info("Test results directory is: target"); //no idea why it's needed
        setBaseUrl(); //must be always before setSecureBaseUrl();
        setSecureBaseUrl();
        setSSLProperty();
        setLogsLevel();
        setSauceLabsConfigs();
    }

    private void setSSLProperty() {
        SSLEnabled = System.getProperty("sslEnabled") != null && System.getProperty("sslEnabled").equals("yes");
    }

    private void setBaseUrl() {
        String commandLineUrl = System.getProperty("baseUrl");
        if ( commandLineUrl != null){
            logger.info("baseUrl was specified in command line. Using value: " + commandLineUrl);
            baseUrl = commandLineUrl;
        } else {
            logger.warn("baseUrl NOT SPECIFIED. USING DEFAULT:" + baseUrl);
        }
        if (!baseUrl.substring(baseUrl.length() - 1).equals("/")) { baseUrl = baseUrl + "/"; }
    }

    private void setSecureBaseUrl() {
        baseUrl = baseUrl.replace("http", "https");
    }

    /**
     * Cleaning all stored data before execute another test.
     */
    private void cleanTestCaseResults() {
        testEmail = "";
        testOutput = "";
        testResults = "";
        orderTime = "";
    }

    private void setStartTimeMark() {
        java.util.Date date = new java.util.Date();
        testStartTimestamp = new Timestamp(date.getTime());
        testStartTime = System.currentTimeMillis();
    }

    private void setSauceLabsConfigs() {
        sauceLabsSession = System.getProperty("sauceLabsSession");
        if (sauceLabsSession != null){
            setSauceLabsSessionParameters();
        }
    }

    /**
     * Method which parses command line parameter "sauceLabsSession" and splits it for next usage
     */
    public static void setSauceLabsSessionParameters() {
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
            setChromeDriverSystemProperty();
            // Chromedriver does not play well with resizing.
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            /* Chromedriver 2.10 starts with message:
            "You are using an unsupported command-line flag: --ignore-certificate-errors. Stability and security will suffer."
            More details by link: https://code.google.com/p/chromedriver/issues/detail?id=799 */
            options.addArguments("test-type");
            //get the chrome driver/start chrome
            driver = new ChromeDriver(options);
        } else if (browser.equals("FIREFOX")) {//get the firefox driver/start firefox
            driver = new FirefoxDriver();
            //maximize the browser window
            driver.manage().window().maximize();
        } else if (browser.equals("HTMLUNIT")) {//instantiate a headless driver with javascript enabled
            driver = new HtmlUnitDriver();
        } else if (browser.equals("HTMLUNITNOJS")) {//instantiate a headless driver with javascript disabled
            driver = new HtmlUnitDriver(false);
            //fix to remove odd warnings it log
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        } else if (browser.equals("INTERNETEXPLORER")) {//assume iedriverserver from http://code.google.com/p/selenium/downloads/list is in one above project directory (where pom.xml is)
            System.setProperty("webdriver.ie.driver", "../IEDriverServer.exe");
            //get the ie driver/start ie
            driver = new InternetExplorerDriver();
            //NOTE: To allow specifying credentials in the url:
            //Had to update add iexplore.exe and explorer.exe DWORD values to the following regedit location, and set their values to 0
            //HKEY_LOCAL_MACHINE\Software\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_HTTP_USERNAME_PASSWORD_DISABLE
            //maximize the browser window
            // driver.manage().window().maximize();
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
    private void setLogsLevel() {
        String logLevel = System.getProperty("logLevel");
        if (logLevel == null || logLevel.isEmpty()) {
            PropertyConfigurator.configure("INFO.properties");
        } else {
            PropertyConfigurator.configure(logLevel + ".properties");
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
    public @Rule TestName testName = new TestName();

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
            logTestOutputToJSON(testStartTimestamp.toString(), testName.getMethodName(), testCaseStatus, testResults,
                    testEmail, testCaseExecutionTime, browser, orderTime, testOutput,
                    tags, sessionId);
            System.out.println("================================================================================");
        }
        @Override
        protected void succeeded(Description d) {
            if (sauceLabsSession != null && !sauceLabsSession.isEmpty()){
                super.succeeded(d);
            }
            testCaseStatus = PASS;
            logTestResult("Test requirements in: " + getTestCaseName() + " successfully passed!");
            logTestOutputToJSON(testStartTimestamp.toString(), testName.getMethodName(), testCaseStatus, testResults,
                    testEmail, testCaseExecutionTime, browser, orderTime, testOutput,
                    tags, sessionId);
            System.out.println("================================================================================");
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
    public void log(String msg) {
        logger.info(msg);
        testOutput += msg + "\n";
    }

    /**
     * Log to testResults and System.out.println
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public void logTestResult(String msg) {
        System.out.println(msg);
        testResults += msg + "\n";
    }

    /**
     * Log to testResults and logger.info
     *
     * @param msg String, a message to be printed to sys out and logged
     */
    public void logTestResultError(String msg) {
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
     * @param timeStamp             time the test started
     * @param testCaseName          class name of the test
     * @param testCaseStatus        one of pass | fail | test error
     * @param validationResults     passing or failing validation results.
     * @param userName              the robot's email address
     * @param testCaseExecutionTime total execution time of the test
     * @param browser               which browser or selenium driver (i.e., IOs)
     * @param stdout_output         the output from the test itself
     * @param tags                  any tags that might identify the test (e.g., Smoke)
     * @param orderTime             if applicable, exact time from pushing the Order button
     * @param sessionId             the test_run sessionId for grouping tests together
     * TODO: get rid of this shit! Refactor and implement XML output.
     * TODO: Implement connection between executed tests per single run to create reports.
     */
    public void logTestOutputToJSON(String timeStamp, String testCaseName, String testCaseStatus, String validationResults,
                                    String userName, long testCaseExecutionTime, String browser,
                                    String orderTime, String stdout_output, String tags, String sessionId) {
        // Create a JSON object
        final String jsonExt = ".json";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", timeStamp);
        jsonObject.put("testcase", testCaseName);
        jsonObject.put("status", testCaseStatus);
        jsonObject.put("validationstring", validationResults);
        jsonObject.put("user", userName);
        jsonObject.put("executiontime", testCaseExecutionTime);
        jsonObject.put("browser", browser);
        jsonObject.put("order_time", orderTime);
        jsonObject.put("stdout", stdout_output);
        jsonObject.put("tags", tags);
        jsonObject.put("session_id", sessionId);
        long current_time = new Date().getTime();
        File logDir = new File("./target/logs/");
        if( !logDir.exists()) {
            logDir.mkdir();
        }
        try {
            FileWriter jsonFile = new FileWriter("./target/logs/" + testCaseName + String.valueOf(current_time) + jsonExt);
            logger.info("Logging to file: " + testCaseName + String.valueOf(current_time) + jsonExt);
            jsonFile.write(jsonObject.toJSONString());
            jsonFile.flush();
            jsonFile.close();
        } catch (IOException e) {
            logger.error("Exception setting up file to write!" + e);
        }
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

    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Selects what baseURL to use secured on not depending on environment prefix.
     *
     * @return String baseURL||BaseURLSecure
     */
    public static String getSecureBaseURL() {
        if (SSLEnabled) {
            return secureBaseUrl;
        } else {
            return baseUrl;
        }
    }


    /**
     * Registers a user with a random email address, formed using getUniqueEmailAddress()
     *
     * @return String email address of registered user.
     */
    public String registerRandomUser() {
       return registerRandomUser("");
    }

    /**
     * Registers a user with a random email address, formed using getUniqueEmailAddress(emailSpecifier)
     *
     * @param emailSpecifier String first part of email address
     * @return String email address of registered user.
     */
    public String registerRandomUser(String emailSpecifier) {
        String randomUserEmail = getUniqueEmailAddress(emailSpecifier);
        loginSignUpPage().createAccountIfNotExist(randomUserEmail, LoginSignUpPage.CUSTOMER_PASSWORD);
        return randomUserEmail;
    }
    /**
     * Method that makes customer not logged in status
     * It use the approach directly opening URL, which send request to log out the customer
     */
    public void logout() {
        logger.info("Making log out...");
        driver.get(getSecureBaseURL() + "customer/account/logout/");
    }

    /**
     * Method to generate a random email user. This returns a domain address.
     *
     * @return String random email address
     */
    public String getUniqueEmailAddress() {
        return getUniqueEmailAddress("testy_testy");
    }

    /**
     * Method to generate a random email user. This returns a julep address.
     *
     * @param login String first part of email address
     * @return String random email address
     */
    public String getUniqueEmailAddress(String login) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDateAndTime = new Date();
        String dateTime = dateFormat.format(currentDateAndTime);
        return login.concat(dateTime.concat("@domain.com"));
    }
}
