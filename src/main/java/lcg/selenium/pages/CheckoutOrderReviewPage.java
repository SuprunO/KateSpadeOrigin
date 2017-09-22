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

import lcg.selenium.Field;
import lcg.selenium.PageFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * A class, which contains implemented actions and verifications, which can be performed on Checkout page
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class CheckoutOrderReviewPage extends PageFactory {

    public Logger logger = Logger.getLogger(CheckoutOrderReviewPage.class);

    public static final By BUTTON_PLACE_ORDER_DISABLED = By.xpath("//button[@class='btn-cart btn-checkout pink-button disabled']");
    public static final By BUTTON_PLACE_ORDER = By.xpath("//button[@class='btn-cart btn-checkout pink-button']");
    public static final By LOADING_MASK_PLACING_ORDER = By.xpath("//div[@class='jcheckout-checkout-loading']");
    public static final By LOADING_MASK_LOADING_STEP_DATA = By.xpath("//div[@id='checkout-step-shipping_method']/div[@class='step-loading']");


    /* Order Summary Block */
    public static final By TITLE_GIFT_BOX_PRODUCT_NAME = By.xpath("//h2[contains(@class, 'product-name') and contains(., 'Gift Box')]");
    public static final By PRODUCT_LINE_ORDER_SUMMURY = By.xpath("//table[@id='checkout-review-table']/tbody/tr");
    public static final By SUBTOTAL_AMOUNT = By.xpath("//span[@class='subtotal']/span");
    public static final By GRAND_TOTAL_AMOUNT = By.xpath("//span[@class='grand-total']/span[@class = 'price']");
    public static final By DROPDOWN_SHIPPING_TYPE = By.xpath("//td[@class='shipping_method_column']/../td[2]/span");
    //select[@name="shipping_method"]
    public static final By DROPDOWN_SHIPPING_METHODS = By.id("rate-table");
    public static final By SHIPPING_AMOUNT = By.xpath("//td[@class='shipping_method_column']/../td[2]/span");
    public static final By TITLE_TAX = By.xpath("//td[contains(text(),'Tax')]");
    public static final By TAX_AMOUNT = By.xpath("//td[contains(text(),'Tax')]/../td[2]/span");
    public static final By REWARDS_REFERRAL_INPUT_ON_CHECKOUT = By.id("rewards_referral");
    public static final By REFERRAL_CODE_LINK_ON_CHECKOUT = By.className("referral_code");
    public static final By EDIT_CART_LINK_ON_CHECKOUT = By.className("edit-cart-link");
    public static final By TITLE_PROMO_CODE = By.className("promocode");

    private static final double DELTA = 1e-2;

    /*
    * expected list of options in the country dropdown
    * see testCountryDropDown method in this class
    */
    private final static String[] COUNTRY_LIST = new String[]{"Canada", "United States", "U.S. Minor Outlying Islands", "U.S. Virgin Islands"};

    public CheckoutOrderReviewPage(WebDriver driver) {
        super(driver);
    }

    /**
     * This method waits for "Place Order" button get disabled and enabled again and click it.
     * The checkout page must be already opened.
     */
    public void clickPlaceOrderButton() {
        logger.info("Waiting till the \"Place Order button\" will be disabled because of ajax calls");
        // Hack to prevent test framework click on Place order button before the ajax starts(ajax has a delay sometime)
        try {
            waitForElementIsVisible(BUTTON_PLACE_ORDER_DISABLED, 10);
        } catch (Exception ex) {
            //do nothing, just moving on
        }
        logger.info("Waiting till the \"Place Order button\" will be enabled - ajax calls finished");
        waitForElementIsVisible(BUTTON_PLACE_ORDER);
        clickOnElement(BUTTON_PLACE_ORDER, "Place Order button");
    }

    /**
     * Waits until Ajax mask is disappeared and Success page opens after submitting order. No Checks just waiting.
     */
    public void waitForOrderIsPlaced() {
        waitForElementNotVisible("PLACING ORDER MASK DID NOT DISAPPEAR AFTER 120 SECONDS", LOADING_MASK_PLACING_ORDER, 120);
    }

    /**
     * Waits till loading step data mask will appear and disappear
     */
    public void waitForLoadStepData() {
        try {
            waitForElementIsVisible(LOADING_MASK_LOADING_STEP_DATA, 30);
            waitForElementNotVisible(LOADING_MASK_LOADING_STEP_DATA, 30);
        } catch (Exception ex) {
            logger.warn("Looks like shipping already loaded");
        }
    }

    /**
     * This method checks  Placing Order Loading(indication that order is processing at present moment) is present.
     */
    public void verifyPlacingOrderLoadingMaskIsPresent() {
        logger.info("Verifying Placing Order Loading(indication that order is processing at present moment) is present.");
        waitForElementIsVisible("PLACING ORDER LOADING MASK DID NOT APPEAR AFTER 10 SECONDS", LOADING_MASK_PLACING_ORDER, 10);
    }

}
