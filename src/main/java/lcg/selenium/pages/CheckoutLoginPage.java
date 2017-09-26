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


package lcg.selenium.pages;

import lcg.selenium.PageFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * A class, which contains implemented actions and verifications, which can be performed on Checkout page
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class CheckoutLoginPage extends PageFactory {

    public Logger logger = Logger.getLogger(CheckoutLoginPage.class);

    public CheckoutLoginPage(WebDriver driver) {
        super(driver);
    }

    /** ---------------------------   START: LOCATORS   --------------------------- */

    //FOR GUEST

    public static final By BUTTON_CONTINUE_AS_GUEST = By.cssSelector(".form-row.form-row-button:first-child");


    /** ---------------------------   END: LOCATORS   --------------------------- */



    /** ---------------------------   START: EXECUTION METHODS   --------------------------- */
    public void clickButtonContinueAsGuest() {
        waitForElementIsVisible(BUTTON_CONTINUE_AS_GUEST);
        clickOnElement(BUTTON_CONTINUE_AS_GUEST, "Guest Checkout button");
    }

    /** ---------------------------   END: EXECUTION METHODS   --------------------------- */



    /** ---------------------------   START: EXPECTED RESULTS   --------------------------- */


    /** ---------------------------   END: EXPECTED RESULTS   --------------------------- */



    /** ---------------------------   START: Verifications methods    --------------------------- */

    /** ---------------------------   END: Verifications methods    --------------------------- */







    /** ---------------------------   START: LOCATORS UNUSED   --------------------------- */
    //FOR REGISTERED USER/
    public static final By FIELD_LOGIN_FORM_CHECKOUT_EMAIL = By.id("login-email");
    public static final By FIELD_LOGIN_FORM_CHECKOUT_PASSWORD = By.id("login-password");
    public static final By BUTTON_LOGIN_FORM_CHECKOUT_LOGIN = By.id("jcheckout-login-button");
    /** ---------------------------   END: LOCATORS UNUSED   --------------------------- */


    /** ---------------------------   START: METHODS THAT CAN BE USEFUL   --------------------------- */

    /**METHODS FOR REGISTERED USER

     /**
     * Makes customer logged in on checkout page with specified Email and Password.
     * Emulates actions on frontend which user have to perform to log in on checkout page.
     *
     * @param email    String, email address that will be used as login/username to log in.
     * @param password String, password that will be used as password to log in.
     */
    public void loginOnCheckoutPage(String email, String password) {
        logger.info("Fill out Email and Password fields and clicking \"Login\" button");
        fillInInput(FIELD_LOGIN_FORM_CHECKOUT_EMAIL, email);
        fillInInput(FIELD_LOGIN_FORM_CHECKOUT_PASSWORD, password);
        clickOnElement(BUTTON_LOGIN_FORM_CHECKOUT_LOGIN, "Login(on the login Pop-up) button");
        //logger.info("Waiting for success message");
        //waitForElementIsVisible(WELCOME_ELEMENT);
    }

    /** ---------------------------   END: METHODS THAT CAN BE USEFUL   --------------------------- */

}
