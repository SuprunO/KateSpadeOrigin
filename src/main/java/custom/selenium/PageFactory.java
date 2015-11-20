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


import custom.selenium.pages.Header;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;


/**
 * A class, which contains implemented common methods for all pages.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public abstract class PageFactory {

    //the main web driver
    public WebDriver driver;

    public static Logger logger = Logger.getLogger(PageFactory.class);
    public static String baseUrl = TestFactory.getBaseUrl(); //TODO: investigate to remove it and replace with TestFactory.getSecureBaseUrl();

    public static final int DEFAULT_WAIT_TIME = 60; //default time to patiently wait for finding elements
    public static final String PASS = "PASS";


    /* Static Pages Paths */
    public static final String PAGE_ABOUT_US = "about.html";
    public static final String PAGE_CAREERS = "careers.html";
    public static final String PAGE_PRESS = "presspage/";
    public static final String PAGE_CONTACT_US = "contacts/";
    public static final String SITE_MAP_PAGE = "sitemap.xml";
    public static final String GOOGLE_TAG_MANAGER = "www.googletagmanager.com";

    //common unique web element identifiers
    public static final By PAGE_404_INDICATOR = By.xpath("//h2[@class='system-error']");
    public static final By WEB_PAGE_TODO_MESSAGE = By.xpath("//div[@class='std' and contains(text(),'todo')]");

    //Test User Information
    public static final String TEST_FIRST_NAME = "AutoFirstNname";
    public static final String TEST_LAST_NAME = "AutoLastNname";
    public static final String CUSTOMER_EMAIL = "test1@speroteck.com";
    public static final String CUSTOMER_PASSWORD = "testthis";

    /* Test credit card information */
    public static final String TEST_CARD_NUMBER = "4111111111111111";

    public static final String TEST_CARD_TYPE = "Visa";
    public static final String TEST_CARD_MONTH = "01 - January";
    public static final String TEST_CARD_YEAR = "2017";

    //Test Address information
    public static final String WA_GUEST_STREET_1 = "111 N QUEEN ANNE AVE";
    public static final String WA_GUEST_STREET_2 = "STE 200";
    public static final String WA_GUEST_CITY = "Seattle";
    public static final String WA_GUEST_STATE = "Washington";
    public static final String WA_GUEST_COUNTRY = "United States";
    public static final String WA_GUEST_ZIP = "98109";
    public static final String WA_GUEST_PHONE = "555.555.5555";



    /**
     * Constructor
     * @param driver
     */
    protected PageFactory(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * use this function to assert the presence and display of an element
     */
    public void AssertPresenceAndDisplay(By by, String strElement) {
        assertTrue(strElement + " MISSING.", isElementPresent(by));
        assertTrue(strElement + " NOT VISIBLE.", driver.findElement(by).isDisplayed());
    }

    /**
     * Selects what baseURL to use secured on not depending on environment prefix.
     *
     * @return String baseURL||BaseURLSecure
     */
    //TODO: it's a total copy of TestFactory.getSecureBaseUrl(); investigate usages to remove it and replace with TestFactory.getSecureBaseUrl();
    public static String getSecureBaseURL() {
        if (TestFactory.SSLEnabled) {
            return TestFactory.secureBaseUrl;
        } else {
            return baseUrl;
        }
    }

    /**
     * Method to generate a random email user. This returns a julep address.
     *
     * @param login String first part of email address
     * @return String random email address
     */
    public String getUniqueEmailAddress(String login) {
        int randomNumber;
        Random randomGenerator = new Random();
        randomNumber = randomGenerator.nextInt(Integer.MAX_VALUE);
        return login.concat(Integer.toString(randomNumber)).concat("@speroteck.com");
    }

    /**
     * Method to generate a random email user. This returns a julep address.
     *
     * @return String random email address
     */
    public String getUniqueEmailAddress() {
        return getUniqueEmailAddress("testy_testy");
    }

    /**
     * This method is waiting during "DEFAULT_WAIT_TIME"(60 seconds) till the element will be present on the current page.
     *
     * @param by Locator of the element on page, which have type By.
     */
    public void waitForElementIsPresent(By by) {
        waitForElementIsPresent(by, DEFAULT_WAIT_TIME);
    }

    /**
     * This method is waiting during specified time till the element will be present on the current page.
     *
     * @param by               Locator of the element on page, which have type By.
     * @param timeOutInSeconds int Time(seconds) for waiting of the element
     */
    public void waitForElementIsPresent(By by, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * Waiting during specified time till the element will be visible for user.
     * If it isn't it throws an {@link AssertionError} with the given message.
     *
     * @param message           String, the identifying message for the {@link AssertionError}.
     * @param by                By, Locator of the element on page, which have type By.
     * @param timeOutInSeconds  int Time(seconds) for waiting of the element
     */
    public void waitForElementIsVisible(String message, By by, int timeOutInSeconds) {
        try {
            waitForElementIsVisible(by, timeOutInSeconds);
        } catch (Exception ex) {
            fail(message);
        }
    }

    /**
     * Waiting during "DEFAULT_WAIT_TIME"(60 seconds) till the element will be visible for user.
     * If it isn't it throws an {@link AssertionError} with the given message.
     *
     * @param message String, the identifying message for the {@link AssertionError}.
     * @param by      By, Locator of the element on page, which have type By.
     */
    public void waitForElementIsVisible(String message, By by) {
        try {
            waitForElementIsVisible(by, DEFAULT_WAIT_TIME);
        } catch (Exception ex) {
            fail(message);
        }
    }

    /**
     * This method is waiting during "DEFAULT_WAIT_TIME"(60 seconds) till the element will be visible for user.
     *
     * @param by Locator of the element on page, which have type By.
     */
    public void waitForElementIsVisible(By by) {
        waitForElementIsVisible(by, DEFAULT_WAIT_TIME);
    }

    /**
     * This method is waiting during "DEFAULT_WAIT_TIME"(60 seconds) till the element will be clickable for user.
     *
     * @param by Locator of the element on page, which have type By.
     */
    public void waitForElementToBeClickable(By by) {
        waitForElementIsClickable(by, DEFAULT_WAIT_TIME);
    }

    /**
     * This method is waiting during specified time till the element will be visible for user.
     *
     * @param by               Locator of the element on page, which have type By.
     * @param timeOutInSeconds int Time(seconds) for waiting of the element
     */
    public void waitForElementIsVisible(By by, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Waiting during specified time till the element will be clickable for user.
     * If it isn't it throws an {@link AssertionError} with the given message.
     *
     * @param message           String, the identifying message for the {@link AssertionError}.
     * @param by                By, Locator of the element on page, which have type By.
     * @param timeOutInSeconds  int Time(seconds) for waiting of the element
     */
    public void waitForElementIsClickable(String message, By by, int timeOutInSeconds) {
        try {
            waitForElementIsClickable(by, timeOutInSeconds);
        } catch (Exception ex) {
            fail(message);
        }
    }

    /**
     * Waiting during specified time till the element will be clickable for user.
     *
     * @param by               Locator of the element on page, which have type By.
     * @param timeOutInSeconds int Time(seconds) for waiting of the element
     */
    public void waitForElementIsClickable(By by, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * Waiting during specified time till the element will be invisible for user.
     * If it isn't it throws an {@link AssertionError} with the given message.
     *
     * @param message           String, the identifying message for the {@link AssertionError}.
     * @param by                By, Locator of the element on page, which have type By.
     * @param timeOutInSeconds  int Time(seconds) for waiting of the element
     */
    public void waitForElementNotVisible(String message, By by, int timeOutInSeconds) {
        try {
            waitForElementNotVisible(by, timeOutInSeconds);
        } catch (Exception ex) {
            fail(message);
        }
    }

    /**
     * Waiting during "DEFAULT_WAIT_TIME" till the element will be invisible for user.
     *
     * @param by Locator of the element on page, which have type By.
     */
    public void waitForElementNotVisible(By by) {
        waitForElementNotVisible(by, DEFAULT_WAIT_TIME);
    }

    /**
     * Waiting during specified time till the element will be invisible for user.
     *
     * @param by               Locator of the element on page, which have type By.
     * @param timeOutInSeconds Time for waiting of the element
     */
    public void waitForElementNotVisible(By by, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.withTimeout(timeOutInSeconds, TimeUnit.SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * Checks if specified an element is present on the current the page.
     * Note: for Ajax or elements with delayed appearing: "wait"|fluent-wait methods sould be used.
     *
     * @param elementLocator By locator of target element
     * @return Boolean
     */
    public boolean isElementPresent(By elementLocator) {
        return driver.findElements(elementLocator).size() > 0;
    }

    /**
     * Method that finds input field by locator with class "By" and fills in specified value.
     *
     * @param locator "By" class locator to search input field.
     * @param value   String value that fills into the input.
     */
    public void fillInInput(By locator, String value) {
        assertTrue("COULD NOT FIND INPUT FIELD: " + locator.toString(), driver.findElement(locator).isDisplayed());
        logger.info("Typing in: \"" + value + "\" into the Field");
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
//        driver.findElement(locator).click();  // Should be moved to separate method: fillInInputAndClick()
    }

    /**
     * Method that finds input field by locator with class "By" and fills in specified value.
     *
     * @param locator "By" class locator to search Dropdown field.
     * @param value   String value that filles into the input.
     */
    public void selectValueInDropDown(By locator, String value) {
        assertTrue("COULD NOT FIND DROPDOWN FIELD: " + locator.toString(), driver.findElement(locator).isDisplayed());
        logger.info("Selecting: \"" + value + "\" in the DropDown: " + locator.toString());
        Select DropDown = new Select(driver.findElement(locator));
        DropDown.selectByVisibleText(value);
    }

    /**
     * * Method that selects all input fields and DropDowns from the specified ArrayList
     * using FillInTextInput() and SelectDropDownValue() methods.
     *
     * @param fields ArrayList<Field> list with fields. Each field have type(String), locator(By) and value(String).
     */
    public void fillFieldsSet(ArrayList<Field> fields) {
        for (Field entry : fields) {
            if (entry.type.equals("input")) {
                fillInInput(entry.locator, entry.value);
            } else if (entry.type.equals("dropdown")) {
                selectValueInDropDown(entry.locator, entry.value);
            }
        }
    }

    /**
     * Checks the target Checkbox by specified locator if it's not already checked.
     *
     * @param locator      By Locator to search the checkbox
     * @param checkboxName String value that describes checkbox(just for pretty logs).
     */
    public void checkCheckbox(By locator, String checkboxName) {
        assertTrue("THE CHECKBOX: \"" + checkboxName + "\" IS NOT VISIBLE ON THE PAGE!", driver.findElement(locator).isDisplayed());
        WebElement targetCheckbox = driver.findElement(locator);
        if (targetCheckbox.isSelected()) {
            logger.info("Checkbox with locator: \"" + checkboxName + "\" is already checked");
        } else {
            targetCheckbox.click();
            logger.info("Checked the checkbox with locator: \"" + checkboxName + "\"");
        }
    }

    /**
     * Un-checks the target Checkbox by specified locator if it's not already un-checked.
     *
     * @param locator      By Locator to search the checkbox
     * @param checkboxName String value that describes checkbox(just for pretty logs).
     */
    public void uncheckCheckbox(By locator, String checkboxName) {
        assertTrue("THE CHECKBOX: \"" + checkboxName + "\" IS NOT VISIBLE ON THE PAGE!", driver.findElement(locator).isDisplayed());
        WebElement targetCheckbox = driver.findElement(locator);
        if (targetCheckbox.isSelected()) {
            targetCheckbox.click();
            logger.info("Un-checked the checkbox with locator: \"" + checkboxName + "\"");
        } else {
            logger.info("Checkbox with locator: \"" + checkboxName + "\" is already un-checked");
        }
    }

    /**
     * Method that finds element by locator with class "By" and clicks on it.
     *
     * @param locator     "By" class locator to search input field.
     * @param elementName String value that describes, which element will be clicked(just for pretty logs).
     */
    public void clickOnElement(By locator, String elementName) {
        assertTrue("COULD NOT FIND ELEMENT: " + elementName, isElementPresent(locator));
        assertTrue("COULD NOT CLICK ON INVISIBLE ELEMENT: " + elementName, driver.findElement(locator).isDisplayed());
        logger.info("Clicking on: " + elementName);
        driver.findElement(locator).click();
    }


    /**
     * Converts text extracted from the page to float number. For further work with cost, taxes, prices calculations.
     *
     * @param extractedText String Price text extracted from the page
     * @return Float
     */
    public float convertPriceTextToFloatNumber(String extractedText) {
        float parsedFloat;
        extractedText = extractedText.replace("$", "");
        parsedFloat = Float.parseFloat(extractedText);
        return parsedFloat;
    }

    /**
     * Moving out nasty try/catch form tests' body.
     * TODO: Method should be removed after all tests will use fluent wait.
     *
     * @param timeInMilliseconds Int Time to wait inMilliseconds
     */
    public void sleep(int timeInMilliseconds) {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch (InterruptedException ex) {
            logger.error("Thread Sleep Exception! " + ex);
        }

    }

    /**
     * This method returns true if 404 page is opened.
     *
     * @return boolean
     */
    public boolean is404Page() {
        return driver.findElements(PAGE_404_INDICATOR).size() != 0;
    }

    /**
     * This method will open specified page using additional part of url with base url.
     * @param pageUrl  additional string to base url
     * @param pageName name of the page
     */
    public void openPage(String pageUrl, String pageName) {
        logger.info("Opening URL: " + baseUrl + pageUrl);
        driver.get(baseUrl + pageUrl);
        assertEquals(pageName + " WAS NOT OPENED!", baseUrl + pageUrl, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + baseUrl + pageUrl, is404Page());
    }

    /**
     * Method that makes customer not logged in status
     * It use the approach directly opening URL, which send request to log out the customer
     */
    public void logout() {
        logger.info("Making log out...");
        driver.get(TestFactory.getSecureBaseURL() + "customer/account/logout/");
    }

    /**
     * Check if a user is logged in
     * @return  Boolean
     */
    public boolean isLoggedIn() {
        //make sure the quick access menu exists
        if (!isElementPresent(Header.QUICK_ACCESS_ACCOUNT)) {
            return false;
        }
        //check quick access menu to signal logged in
        return driver.findElement(Header.QUICK_ACCESS_ACCOUNT).isDisplayed();
    }

    /**
     * Clicking "OK" on JS Alerts with printing Alert's text.
     */
    public void acceptAlert() {
        Alert alert=driver.switchTo().alert();
        logger.info("Alert: '" + alert.getText() + "'. Clicking 'OK'");
        alert.accept();
    }

}
