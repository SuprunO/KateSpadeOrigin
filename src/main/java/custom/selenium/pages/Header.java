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

import custom.selenium.PageFactory;
import custom.selenium.TestFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.Assert.assertTrue;

/**
 * A class, which contains implemented actions and verifications, which can be performed for Global elements(header)
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class Header extends PageFactory {

    public Logger logger = Logger.getLogger(TestFactory.class);

    /* Header Elements */
    public static final By FIELD_SEARCH = By.id("search");
    public static final By ICON_SHOPPING_CART = By.id("quick-access-cart-icon");
    public static final By QUICK_ACCESS_ACCOUNT = By.id("quick-access-account");
    public static final By LOGO = By.xpath("//a[@class='logo']");

    /* Header: Top Navigation Menu */
    public static final By TOP_NAV_MENU_SHOP = By.xpath("//ul[@id='nav']/li/a/span[contains(text(),'Shop')]");
    public static final String TOP_NAV_MENU_TITLE = "//ul[@id='nav']/li/a/span[contains(text(),'";
    public static final String TOP_NAV_MENU_ITEM = "//ul[@id='nav']/li/ul/li/a/span[contains(text(),'";

    public Header(WebDriver driver) {
        super(driver);
    }

    /**
     * Method enters specified text into the search field in the header and press ENTER key to run search
     */
    public void searchByText(String searchPhrase) {
        System.out.print("Entering text: " + searchPhrase + "into search field in header");
        driver.findElement(FIELD_SEARCH).sendKeys(searchPhrase);
        Actions action = new Actions(driver);
        System.out.print("Running search by pressing ENTER button");
        action.sendKeys(Keys.ENTER).build().perform();
    }

    /**
     * Check if a user is logged in
     *
     * @return Boolean
     */
    public boolean isLoggedIn() {
        //make sure the quick access menu exists
        if (!isElementPresent(QUICK_ACCESS_ACCOUNT)) {
            return false;
        }
        //check quick access menu to signal logged in
        return driver.findElement(QUICK_ACCESS_ACCOUNT).isDisplayed();
    }

    /**
     * This method perform actions, which user need to do to get the Shopping cart page from any page, which has Cart icon in header .
     */
    public void clickOnCartIcon() {
        clickOnElement(ICON_SHOPPING_CART, "Shopping cart icon");
        //waitForElementIsVisible("SHOPPING CART CONTENT DID NOT APPEAR AFTER 10 SECONDS OF WAITING",ShoppingCartPage.LOCATOR_CART_CONTENT, 10);
    }

    /**
     * Use this function to click an item of the top nav menu
     * @param menuTitle - text of the parent menu to hover over
     * @param menuItem - text of the item to click within the parent menu
     */
    public void findAndClickNavMenuItem(String menuTitle, String menuItem) {
        //make sure the requested menu exist
        assertTrue("MENU TITLE " + menuTitle + " IS NOT PRESENT ON PAGE",isElementPresent(By.xpath(TOP_NAV_MENU_TITLE + menuTitle + "')]")));
        //make sure the requested menu item exists
        assertTrue("MENU ITEM " + menuItem + " IS NOT PRESENT ON PAGE",isElementPresent(By.xpath(TOP_NAV_MENU_ITEM + menuItem + "')]")));
        //get the requested menu and item
        final WebElement weMenu = driver.findElement(By.xpath(TOP_NAV_MENU_TITLE + menuTitle + "')]"));
        WebElement weItem = driver.findElement(By.xpath(TOP_NAV_MENU_ITEM + menuItem + "')]"));
        //make sure the parent menu contains the requested item
        assertTrue("MENU TITLE " + menuTitle + " DO NOT CONTAIN " + menuItem + " MENU ITEM",weMenu.equals(weItem.findElement(By.xpath("../../../../a/span"))));
        //NAVIGATE TO THE MENU ITEM
        driver.navigate().to(weItem.findElement(By.xpath("..")).getAttribute("href"));
    }

    /**
     * This function will return true if Logo in header is displayed
     * @return boolean
     */
    public boolean isLogoDisplayed() {
        return driver.findElement(LOGO).isDisplayed();
    }
}
