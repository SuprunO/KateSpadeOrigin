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
 * A class, which contains implemented actions and verifications, which can be performed on Product details page
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class ProductDetailsPage extends PageFactory {


    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public Logger logger = Logger.getLogger(ProductDetailsPage.class);

    /** ---------------------------   START: LOCATORS   --------------------------- */

    public static final By BUTTON_ADD_TO_CART = By.xpath("//*[@id='add-to-cart']");
    public static final By TITLE_PRODUCT_NAME = By.xpath("//*[@id='product-content']/h1");
    /** ---------------------------   END: LOCATORS   --------------------------- */




    /** ---------------------------   START: EXPECTED RESULTS   --------------------------- */

    public static final String EXPECTED_PRODUCT_NAME_HEADER = "dunne lane leopard-print small lake";

    /** ---------------------------   END: EXPECTED RESULTS   --------------------------- */



    /**
     * This method will open PDP, creating URL: Sitename + Product SKU + .html.
     *
     * @param productID String,  additional path to the base url, which allows to open PDP by url.
     *  Assertion: Expected Product name with WebElement of Product name locator converted to String
     */
    public void openById(String productID) {
        logger.info("Opening product by id '" + getStartURL() + productID + ".html");
        driver.get(getStartURL() + productID + ".html");
        //TODO: change to assertTrue that current url contains productID + ".html"
        assertEquals(EXPECTED_PRODUCT_NAME_HEADER, getProductNameHeaderText());
    }

    /**
     * Searches WebElement locator on PDP page - Product Name and extracting text from it
     *  Assertion the Product name
     */

    private String getProductNameHeaderText() {
        return driver.findElement(TITLE_PRODUCT_NAME).getText();
    }

    /**
     * Click on the Add to Cart button on PDP once it will be visible
     * Asserts the ButtonAddToCart Size is > 1
     */

    public void clickButtonAddToCart() {
        logger.info("Adding product to cart clicking on \"Add to Bag\" button");
        waitForElementIsVisible(BUTTON_ADD_TO_CART, 200);
        assertTrue(driver.findElements(BUTTON_ADD_TO_CART).size() > 0);
        clickOnElement(BUTTON_ADD_TO_CART, "Add to Cart button");
    }
}
