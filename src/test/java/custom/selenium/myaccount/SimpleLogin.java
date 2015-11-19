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

package custom.selenium.myaccount;

import custom.selenium.TestFactory;
import custom.selenium.pages.Footer;
import custom.selenium.pages.MyAccountPages;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

/**
 * Simple login tests.  Can be run as load tests using JMeter or other load tool.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
//@Category(GlobalTests.class)
public class SimpleLogin extends TestFactory {

    //@Category({SmokeTests.class, GlobalTests.class})
    @Test
    public void createNewAccountTest() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log("Starting test... " + testCaseName);
        logger.info("Test Description: ");
        logger.info("@Given: I am on homepage as guest");
        logger.info("@When I pass new user registration successfully");
        logger.info("@Then I am on My Account page as registered user");
        logger.info("Starting step: ");
        logger.info("@Given: I am on homepage as guest");
        driver.get(baseUrl);
        logger.info("Starting step: ");
        logger.info("@When I pass new user registration successfully");
        testEmail = loginSignUpPage().registerRandomUser();
        logger.info("Starting step: ");
        logger.info("@Then I am on My Account page as registered user");
        assertTrue("I AM NOT ON REGISTER SUCCESS PAGE!", driver.getCurrentUrl().contains(MyAccountPages.PAGE_REGISTRATION_SUCCESS));
    }

    @Test
    public void newslettersSignUpOnFooterTest() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log("Starting test... " + testCaseName);
        logger.info("Test set: Sign up for newsletters from footer");
        logger.info("Test Description: ");
        logger.info("@Given: I am on homepage as guest");
        logger.info("@When I fill field with email");
        logger.info("@And I click on 'Sign Up' button");
        logger.info("@Then I see 'Thank you for singing up!' message");
        logger.info("Starting step: ");
        logger.info("@Given: I am on homepage as guest");
        driver.get(baseUrl);
        logger.info("Starting step: ");
        logger.info("@When I fill field with email");
        logger.info("@And I click on 'Sign Up' button");
        testEmail = getUniqueEmailAddress();
        homePage().fillInInput(By.id("newsletter"), testEmail);
        homePage().clickOnElement(Footer.BUTTON_SIGN_UP, "Button 'Sign Up'");
        logger.info("Starting step: ");
        logger.info("@Then I see 'Thank you for singing up!' message");
        boolean signUpSuccess;
        try {
            homePage().waitForElementIsVisible(By.xpath(".//*[@id='responsive-top-cart']/div"), 10); //move xpath to homepage
            signUpSuccess = true;
        } catch (Exception ex) {
            signUpSuccess = false; //For better exception error message
        }
        assertTrue("Message 'Thank you for singing up!' did not appear", signUpSuccess);
    }

}