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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A class, which contains implemented actions and verifications, which can be performed on Product details page
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class ProductDetailsPage extends PageFactory {

    public Logger logger = Logger.getLogger(ProductDetailsPage.class);

    public static final String PATH_VIEW_PRODUCT_BY_ID = "products/";

    /* Product Detail Page elements */
    public static final By BUTTON_ADD_TO_CART = By.xpath("//button[@class='button btn-cart']");
    public static final By IMAGE_PRODUCT_SMALL_IMAGE = By.xpath("//a[@class='MagicZoomPlus']/img");
    public static final By TITLE_PRODUCT_NAME = By.xpath("//div[@class='product-name']//h1");
    public static final By TITLE_PRODUCT_STYLE_PROFILE = By.xpath("//div[@class='product-name']//span[@itemprop='name/styleProfile']");
    public static final By FIELD_QTY = By.id("qty");
    public static final String PATH_TO_PRICE_BOX = "//div[@class='product-main-info']//div[@class='price-box']";
    public static final String PATH_TO_REGULAR_PRICE = "//span[contains(@class,'regular-price')]";
    public static final String PATH_TO_SPECIAL_PRICE = "//span[contains(@class,'special-price')]/span[@class='price']";
    public static final String PATH_TO_SPECIAL_OLD_PRICE = "//span[contains(@class,'regular-price') and contains(@class,'has-special-price')]";
    public static final String PATH_TO_MAVEN_PRICE = "//span[contains(@class,'maven-price')]/span[@class='price']";
    public static final By LINK_ADD_TO_WISHLIST = By.xpath("//a[@class='link-wishlist']");
    public static final By LINK_TAB_DETAILS = By.xpath("//li[@id='product_tabs_description']/a");
    public static final By SECTION_TAB_DETAILS_CONTENT = By.xpath("//div[@id='product_tabs_description_contents']/div[@class='std']");
    public static final By SECTION_SEE_IT_IN_ACTION = By.xpath("//div[@class='pixlee-wrapper']");


    private String currentProductName;

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * This method will open Category page, url of which ends with categoryPath.
     *
     * @param productID String, categoryPath additional path to the base url, which allows to open category by url.
     */
    public void openById(String productID) {
        logger.info("Opening product by id '" + productID + "' URL: " + getStartURL() +PATH_VIEW_PRODUCT_BY_ID+productID+".html");
        driver.get(getStartURL()+PATH_VIEW_PRODUCT_BY_ID+productID+".html");
        //TODO: change to assertTrue that current url contains productID + ".html"
        assertEquals(productID.toUpperCase() + " PAGE WAS NOT OPENED", productID, driver.getCurrentUrl());

        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PATH_VIEW_PRODUCT_BY_ID + productID +".html", is404Page());
    }

    /**
     * Searches and assigns Product Name to class variable from current page
     */
    private void setCurrentProductName(){
        assertTrue("COULD NOT FIND PRODUCT NAME!", isElementPresent(TITLE_PRODUCT_NAME));
        currentProductName = driver.findElement(TITLE_PRODUCT_NAME).getText();
    }

    /**
     * Returns value of Product name from current page.
     * @return  String: current Product name
     */
    public String getCurrentProductName(){
        setCurrentProductName();
        return currentProductName;
    }

    /**
     * Performs click on the Add to Cart button once it will be visible
     */
    public void clickButtonAddToCart() {
        setCurrentProductName();
        assertTrue("Product " + TITLE_PRODUCT_NAME + " suppose out of stock.",
                driver.findElements(BUTTON_ADD_TO_CART).size() > 0);
        waitForElementIsVisible(BUTTON_ADD_TO_CART);
        waitForElementToBeClickable(BUTTON_ADD_TO_CART);
        logger.info("Adding product to cart clicking on \"Add to Bag\" button");
        clickOnElement(BUTTON_ADD_TO_CART, "Add to Cart button");
        waitForElementIsVisible(Header.POPUP_MINICART, 120);
    }

}
