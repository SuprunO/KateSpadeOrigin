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

package custom.selenium.pages;

import custom.selenium.Field;
import custom.selenium.PageFactory;
import custom.selenium.TestFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

import static custom.selenium.TestFactory.getStartURL;
import static org.junit.Assert.*;


/**
 * A class, which contains implemented actions and verifications, which can be performed on Login/Sign Up page
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class LoginSignUpPage extends PageFactory {

    public Logger logger = Logger.getLogger(TestFactory.class);

    /* Facebook locators */
    public static final By FIELD_EMAIL_FACEBOOK = By.id("email");
    public static final By FIELD_PASSWORD_FACEBOOK = By.id("pass");
    public static final By BUTTON_LOGIN_FACEBOOK = By.id("loginbutton");

    /* Login/Sign Up page Elements */
    public static final By LOGIN_EMAIL = By.id("email");
    public static final By LOGIN_PASSWORD = By.id("pass");
    public static final By BUTTON_LOGIN = By.id("send2");
    public static final By BUTTON_CONNECT_WITH_FACEBOOK_LOGIN = By.xpath("//div[@id='resp-tabpage_1']/a");

    public static final By FIELD_SIGN_UP_FIRST_NAME = By.id("firstname");
    public static final By FIELD_SIGN_UP_LAST_NAME = By.id("lastname");
    public static final By FIELD_SIGN_UP_EMAIL = By.id("email_address");
    public static final By FIELD_SIGN_UP_PASSWORD = By.id("password");
    public static final By FIELD_SIGN_UP_CONFIRM_PASSWORD = By.id("confirmation");
    public static final By BUTTON_CREATE_ACCOUNT = By.xpath("//div[4]/button");
    public static final By BLOCK_SUCCESS_MSG = By.xpath("//li[@class='success-msg']");
    public static final String PAGE_LOGIN_SIGN_UP = "customer/account/login";

    public LoginSignUpPage(WebDriver driver) {
        super(driver);
    }

    /**
     * This method will open Login/Sign Up page.
     */
    public void open() {
        logger.info("Opening URL: " + getStartURL() + PAGE_LOGIN_SIGN_UP);
        driver.get(getStartURL() + PAGE_LOGIN_SIGN_UP);
        assertEquals("LOGIN/SIGN UP PAGE WAS NOT OPENED", getStartURL() + PAGE_LOGIN_SIGN_UP, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_LOGIN_SIGN_UP, is404Page());
    }

    /**
     * Login to the front-end as a registered customer with specified credentials.
     *
     * @param email    String customer's email address
     * @param password String customer's password
     */
    public void login(String email, String password) {
        //logout if already logged in
        if (isLoggedIn()) {
            logout();
        }
        open();
        fillInInput(LOGIN_EMAIL, email);
        fillInInput(LOGIN_PASSWORD, password);
        assertTrue("LOGIN BUTTON NOT FOUND!", isElementPresent(BUTTON_LOGIN));
        logger.info("Clicking \"Login\" button");
        driver.findElement(BUTTON_LOGIN).click();
        //wait for login to complete
        waitForElementIsVisible("COULD NOT LOGIN: QUICK ACCESS DID NOT APPEAR!", Header.QUICK_ACCESS_ACCOUNT);
    }

    /**
     * This method creates account with specified Email and Password  used for creating.
     * Emulates actions on frontend which user have to perform to create account.
     *
     * @param email String, email address that will be used as login/username to create account
     * @param pass  String, password that will be used as password/confirmation of password to create account
     */
    public void createAccountIfNotExist(String email, String pass) {
        logger.info("Verifying we're logged out");
        if (isLoggedIn()) {
            logout();
        }
        logger.info("Navigating to the registration page");
        open();

        /* List of input fields which needed to be filled in. */
        ArrayList<Field> inputsList = new ArrayList<Field>();

        /* type of input(String), key is input field locator(class By) , value is String filling in this input */
        inputsList.add(new Field("input", FIELD_SIGN_UP_FIRST_NAME, TEST_FIRST_NAME));
        inputsList.add(new Field("input", FIELD_SIGN_UP_LAST_NAME, TEST_LAST_NAME));
        inputsList.add(new Field("input", FIELD_SIGN_UP_EMAIL, email));
        inputsList.add(new Field("input", FIELD_SIGN_UP_PASSWORD, pass));
        inputsList.add(new Field("input", FIELD_SIGN_UP_CONFIRM_PASSWORD, pass));

        /* Filling in input fields which are present in inputsList. */
        fillFieldsSet(inputsList);
        //verify registration button exists, and click if so
        assertTrue("COULD NOT FIND CREATE ACCOUNT BUTTON!", isElementPresent(BUTTON_CREATE_ACCOUNT));
        waitForElementIsVisible(BUTTON_CREATE_ACCOUNT);             // Make sure the button is visible to the driver
        logger.info("CREATE ACCOUNT button is found and visible!");
        logger.info("Clicking CREATE ACCOUNT button");
        driver.findElement(BUTTON_CREATE_ACCOUNT).click();
        waitForElementIsVisible(BLOCK_SUCCESS_MSG);
        // Result handle
        if (driver.getCurrentUrl().contains(MyAccountPages.PAGE_REGISTRATION_SUCCESS)) {
            logger.info("CUSTOMER WITH EMAIL:" + email + " NAME:" + TEST_FIRST_NAME + " " + TEST_LAST_NAME + " WAS REGISTERED");
        } else {
            logger.info("CUSTOMER WITH EMAIL:" + email + " NAME:" + TEST_FIRST_NAME + " " + TEST_LAST_NAME + " ALREADY EXISTS");
        }
    }

    /**
     * Registers a user with a random email address, formed using getUniqueEmail()
     *
     * @return String email address of registered user.
     */
    public String registerRandomUser() {
        String randomUserEmail = TestFactory.getUniqueEmail();
        createAccountIfNotExist(randomUserEmail, CUSTOMER_PASSWORD);
        return randomUserEmail;
    }

    /**
     * Registers a user with a random email address, formed using getUniqueEmail(emailSpecifier)
     *
     * @param emailSpecifier String first part of email address
     * @return String email address of registered user.
     */
    public String registerRandomUser(String emailSpecifier) {
        String randomUserEmail = TestFactory.getUniqueEmail(emailSpecifier);
        createAccountIfNotExist(randomUserEmail, CUSTOMER_PASSWORD);
        return randomUserEmail;
    }

}
