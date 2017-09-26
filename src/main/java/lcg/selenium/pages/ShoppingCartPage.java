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

import static lcg.selenium.TestFactory.getStartURL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A class, which contains implemented actions and verifications, which can be performed on Shopping Cart Page
 *
 * @author Speroteck QA Team (qa@speroteck.com))
 */

public class ShoppingCartPage extends PageFactory {

    public static final Logger logger = Logger.getLogger(ShoppingCartPage.class);

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }


    public static final String PAGE_SHOPPING_CART = "checkout/cart";

    /** ---------------------------   START: LOCATORS   --------------------------- */

    public static final By PROCEED_TO_CHECKOUT = By.xpath(".//div/fieldset/button");

    /** ---------------------------   END: LOCATORS   --------------------------- */




    /** ---------------------------   START: EXECUTION METHODS   --------------------------- */
    public void clickProceedToCheckoutButton() {
        waitForElementIsVisible(PROCEED_TO_CHECKOUT);
        clickOnElement(PROCEED_TO_CHECKOUT, "Proceed To Checkout button");
    }
    /** ---------------------------   END: EXECUTION METHODS   --------------------------- */


    /** ---------------------------   START: EXPECTED RESULTS   --------------------------- */


    /** ---------------------------   END: EXPECTED RESULTS   --------------------------- */



    /** ---------------------------   START: Verifications methods    --------------------------- */

    /** ---------------------------   END: Verifications methods    --------------------------- */










    /** ---------------------------   START: LOCATORS UNUSED   --------------------------- */

    public static final By LINK_REMOVE = By.linkText("Remove");
    public static final By CHECKBOX_GIFT_IN_ORDER = By.id("order-contains-gift");

    public static final By LOCATOR_EMPTY_CART_CONTENT = By.xpath("//div[@class='cart-empty']");
    public static final By LOCATOR_CART_CONTENT = By.xpath("//div[@class='cart']");
    public static final By FIELD_GIFT_CARD = By.name("giftcard_code");
    public static final By BUTTON_APPLY_GIFT_CARD = By.id("giftcard_code_button");
    public static final By SUCCESS_MESSAGE = By.xpath("//li[@class='success-msg']");

    public static final By TEXT_SUBTOTAL = By.xpath("//span[@class='subtotal']/span");

    /** ---------------------------   END: LOCATORS UNUSED   --------------------------- */



    /** ---------------------------   START: METHODS THAT CAN BE USEFUL   --------------------------- */


    /**
     * This method opens Shopping Cart Page.
     */
    public void open() {
        logger.info("Opening URL: " + getStartURL() + PAGE_SHOPPING_CART);
        driver.get(getStartURL() + PAGE_SHOPPING_CART);
        assertEquals("SHOPPING CART PAGE WAS NOT OPENED", getStartURL() + PAGE_SHOPPING_CART, driver.getCurrentUrl());
    }

    /**
     * Verifies that current page is Shopping Cart Page and returns "true" if so.
     *
     * @return Bool
     */
    public boolean isOpened() {
        return driver.getCurrentUrl().equals(getStartURL() + PAGE_SHOPPING_CART);
    }


    /**
     * Verifies is Shopping cart empty or not, by presence of LOCATOR_EMPTY_CART_CONTENT block and returns "true" if empty.
     *
     * @return Boolean
     */
    public boolean isShoppingCartEmpty() {
        return isElementPresent(LOCATOR_EMPTY_CART_CONTENT);
    }


    /**
     * This method perform actions, which user need to do to delete all products from Shopping cart.
     */
    public void clearShoppingCart() {
        assertTrue("SHOPPING CART PAGE IS NOT OPENED!", isOpened());
        while (!isShoppingCartEmpty()) {
            logger.info("Clicking on Remove link until it is disappeared");
            clickOnElement(LINK_REMOVE, "Remove link");
        }
        logger.info("Shopping Cart is Empty now");
    }

    /**
     * Checks "There's a gift in this order" Checkbox if it's not already checked.
     */
    public void checkCheckboxGiftInOrder() {
        checkCheckbox(CHECKBOX_GIFT_IN_ORDER, "There's a gift in this order");
    }

    /**
     * This method perform actions, which user need to do to get the Checkout page from Shopping cart page.
     */


    /**
     * This method will apply specified Gift Card.
     *
     * @param giftCardNumber String value
     */
    public void applyGiftCard(String giftCardNumber) {
        fillInInput(FIELD_GIFT_CARD, giftCardNumber);
        clickOnElement(BUTTON_APPLY_GIFT_CARD, "Button Apply Gift Card");
        waitForElementIsVisible(SUCCESS_MESSAGE);
    }

    /**
     * Searches and returns Subtotal value.
     *
     * @return float
     */
    public float getSubtotal() {
        assertTrue("COULD NOT FIND SUBTOTAL BLOCK!", driver.findElement(TEXT_SUBTOTAL).isDisplayed());
        float subtotal;
        subtotal = convertPriceTextToFloatNumber(driver.findElement(TEXT_SUBTOTAL).getText());
        return subtotal;
    }

    /**
     * Method will change Qty for product with specified Name
     *
     * @param productName String name of product
     * @param qty         String  new Qty for product
     */
    public void changeQtyForProduct(String productName, String qty) {
        fillInInput(By.xpath("//span[contains(text(), " +
                productName + ")]/../../../../td[@class='a-right cart-fifth']//input"), qty);
    }

    /** ---------------------------   END: METHODS THAT CAN BE USEFUL   --------------------------- */
}
