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
 * A class, which contains implemented actions and verifications, which can be performed for Global elements(header)
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class Header extends PageFactory {

    private static final Logger logger = Logger.getLogger(Header.class);

    public Header(WebDriver driver) {
        super(driver);
    }

    /** ---------------------------   START: LOCATORS   --------------------------- */
    public static final By ICON_SHOPPING_CART = By.cssSelector(".minicart-icon");

    public static final By POPUP_MINICART = By.xpath("//div[@class='mini-cart-content']");
    public static final By BUTTON_CHECKOUT = By.cssSelector(".button.mini-cart-link-cart");
    /** ---------------------------   END: LOCATORS   --------------------------- */



    /** ---------------------------   START: EXECUTION METHODS   --------------------------- */
    /**
     * Click on Checkout Button InMini Cart to get to the Shopping cart page.
     */
    public void clickCheckoutButtonInMiniCart() {
        logger.info("Clicking on checkout button on Shopping cart page");
        waitForElementIsVisible(POPUP_MINICART, 120);
        waitForElementIsVisible(BUTTON_CHECKOUT);
        clickOnElement(BUTTON_CHECKOUT, "CHECKOUT button");
        logger.info("Waiting till pop-up with addons will open");
    }

    /** ---------------------------   END: EXECUTION METHODS   --------------------------- */





    /** ---------------------------   START: EXPECTED RESULTS   --------------------------- */


    /** ---------------------------   END: EXPECTED RESULTS   --------------------------- */





    /** ---------------------------   START: VERIFICATION METHODS   --------------------------- */


    /** ---------------------------   END: VERIFICATION METHODS   --------------------------- */









    /** ---------------------------   START: METHODS THAT CAN BE USEFUL   --------------------------- */

    /**
     * Click on Cart Icon located in Header to get the Shopping cart page (if needed).
     */
    public void clickOnCartIcon() {
        waitForElementIsVisible("SHOPPING CART CONTENT DID NOT APPEAR AFTER 10 SECONDS OF WAITING", ShoppingCartPage.LOCATOR_CART_CONTENT, 10);
        clickOnElement(ICON_SHOPPING_CART, "Shopping cart icon");
    }

    /** ---------------------------   END: METHODS THAT CAN BE USEFUL   --------------------------- */
}
