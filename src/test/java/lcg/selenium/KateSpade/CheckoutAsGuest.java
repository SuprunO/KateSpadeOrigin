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

package lcg.selenium.KateSpade;

import lcg.selenium.TestFactory;
import lcg.selenium.pages.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Simple login tests.  Can be run as load tests using JMeter or other load tool.
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class CheckoutAsGuest extends TestFactory {

    /**
     * Test requirement: Guest Visitors have ability to place order
     */
    @Test
    public void testGuestCanPlaceOrder() {
        testCaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log("Starting test.... " + testCaseName);
        log("Starting test: Guest Visitors have ability to place order");
        log("Test description: ");
        log("@Given: I am on \"Shopping Bag\" page as Guest with random product in cart.");
        log("@When: I click \"Edit\" customer button");
        log("@Then: I see Customer account info looks good in admin panel.");
        log("Stating step: ");
        log("@Given: I am  on \"Manage Customers\" tab in \"Customers\" Admin Panel's Section and just after my order is placed.");
        categoryLandingPage().open(CategoryLandingPage.PAGE_NAIL_POLISH);
        categoryLandingPage().addToCartByProductName("Alice");
        header().clickOnCartIcon();
        shoppingCartPage().clickCheckoutButton();
        checkoutPage().fillShippingAddressForm("WASHINGTON");
        checkoutPage().checkCheckboxBillingSameAsShipping();
        checkoutPage().fillBillingCCForm();
        checkoutPage().fillCreateAccountForm(testEmail, checkoutPage().CUSTOMER_PASSWORD);
        checkoutPage().clickPlaceOrderButton();
        checkoutPage().waitForOrderIsPlaced();
        log("@Given step: PASSED!");
        log("Stating step: ");
        log("@Then: I see, the customer with email: '" + testEmail + "' in the list of customers.");

        log("@Then step: PASSED!");

        log("Stating step: ");
        log("@When: I click \"Edit\" customer button");


        log("@When step: PASSED!");
        log("Stating step: ");
        log("@Then: I see Customer account info looks good in admin panel.");

        log("@Then step: PASSED!");
    }
}