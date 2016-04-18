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
import custom.selenium.TestFrameworkRuntimeException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static custom.selenium.TestFactory.getStartURL;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A class, which contains implemented actions and verifications, which can be performed on My Account pages
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class MyAccountPages extends PageFactory{

    private static final Logger logger = Logger.getLogger(MyAccountPages.class);

    public static final String PAGE_REGISTRATION_SUCCESS = "/account/index/";
    public static final By SUCCESS_MESSAGE = By.xpath("//li[@class='success-msg']");

    /*My Account Pages (Left Menu) */
    public static final String PAGE_MY_INFO = "customer/account/";
    public static final String PAGE_BILLING_INFO = "palorus/vault/index/";
    public static final String PAGE_ORDER_HISTORY = "sales/order/history/";
    public static final String PAGE_MY_WISHLIST = "wishlist/";
    public static final String PAGE_MY_BEAUTY_BIO = "profile/beautybio/";
    public static final String PAGE_CHECK_GIFT_CARD_BALANCE = "giftcard/customer/";
    public static final String PAGE_EDIT_ACCOUNT_INFORMATION = "customer/account/edit/";
    public static final String PAGE_CHANGE_PASSWORD = "customer/account/edit/changepass/1/";
    public static final String PAGE_ADD_NEW_CARD = "palorus/vault/add/";
    public static final String PAGE_NEWSLETTER_SUBSCRIPTIONS = "newsletter/manage/";
    public static final String PAGE_MY_JULES_REWARDS_PAGE = "rewards/customer/";
    public static final String PAGE_INVITE_FRIENDS = "rewardsref/customer/";
    public static final String PAGE_MY_MAVEN_STYLE_PROFILE = "maven/account/changestyleprofile/";
    public static final String PAGE_MY_MONTHLY_BOX = "maven/account/current/";
    public static final String PAGE_EDIT_MY_BOX = "maven/account/";

    /*My Account Pages elements*/
    public static final By LINK_UPDATE_ADDRESS = By.xpath("//div[@class='col2-set']//h3[@class='box-title']/a");
    public static final By LINK_ORDER_VIEW_FIRST = By.xpath("//td[@class='a-center last']//a");
    public static final By BUTTON_SHARE_WISHLIST = By.name("save_and_share");
    public static final By BLOCK_SUCCESS_MSG = By.xpath("//li[@class='success-msg']");

    /* My Information Page */
    public static final By MY_INFO_LINK_UPDATE_SHIPPING = By.xpath("//h3[contains(text(),'Default Shipping Address')]//a");
    public static final By MY_INFO_LINK_LOGOUT = By.xpath("//a[contains(@href, 'customer/account/logout')]");

    /* My Information: Update Shipping Address Fieldset */
    public static final By FIELD_SHIPPING_FIRST_NAME = By.id("firstname");
    public static final By FIELD_SHIPPING_LAST_NAME = By.id("lastname");
    public static final By FIELD_SHIPPING_TELEPHONE = By.id("telephone");
    public static final By FIELD_SHIPPING_STREET_ADDRESS1 = By.id("street_1");
    public static final By FIELD_SHIPPING_STREET_ADDRESS2 = By.id("street_2");
    public static final By FIELD_SHIPPING_CITY = By.id("city");
    public static final By FIELD_SHIPPING_STATE = By.id("region_id");
    public static final By FIELD_SHIPPING_ZIP = By.id("zip");
    public static final By FIELD_SHIPPING_COUNTRY = By.id("country");

    /* My Information: Update Shipping Address */
    public static final By MY_ACC_SHIPPING_FIELD_FIRST_NAME = By.id("firstname");
    public static final By MY_ACC_SHIPPING_FIELD_LAST_NAME = By.id("lastname");
    public static final By MY_ACC_SHIPPING_FIELD_TELEPHONE = By.id("telephone");
    public static final By MY_ACC_SHIPPING_FIELD_STREET_ADDRESS_1 = By.id("street_1");
    public static final By MY_ACC_SHIPPING_FIELD_STREET_ADDRESS_2 = By.id("street_2");
    public static final By MY_ACC_SHIPPING_FIELD_CITY = By.id("city");
    public static final By MY_ACC_SHIPPING_DROPDOWN_STATE = By.id("region_id");
    public static final By MY_ACC_SHIPPING_FIELD_ZIP = By.id("zip");
    public static final By MY_ACC_SHIPPING_DROPDOWN_COUNTRY = By.id("country");
    public static final By MY_ACC_SHIPPING_BUTTON_UPDATE_ADDRESS = By.xpath("//button[@title='Update Address']");

    /* Billing Information Page */
    public static final By BILLING_INFO_BUTTON_EDIT_CARD = By.xpath("//p[@class='edit-payment-information-link']/a");

    /* Billing information: Add A Credit Card Page */
    public static final By ADD_CARD_DROPDOWN_CREDIT_CARD_TYPE = By.id("cc_type");
    public static final By ADD_CARD_FIELD_CREDIT_CARD_NUMBER = By.id("cc_number");
    public static final By ADD_CARD_DROPDOWN_EXPIRATION_MONTH = By.id("cc_expiration");
    public static final By ADD_CARD_DROPDOWN_EXPIRATION_YEAR = By.id("cc_expiration_yr");
    public static final By ADD_CARD_FIELD_BILLING_FIRST_NAME = By.id("firstname");
    public static final By ADD_CARD_FIELD_BILLING_LAST_NAME = By.id("lastname");
    public static final By ADD_CARD_FIELD_BILLING_STREET_ADDRESS1 = By.id("street1");
    public static final By ADD_CARD_FIELD_BILLING_STREET_ADDRESS2 = By.id("street2");
    public static final By ADD_CARD_FIELD_BILLING_CITY = By.id("city");
    public static final By ADD_CARD_FIELD_BILLING_STATE = By.id("region_id");
    public static final By ADD_CARD_FIELD_BILLING_ZIP = By.id("postcode");
    public static final By ADD_CARD_FIELD_BILLING_COUNTRY = By.id("country_id");
    public static final By ADD_CARD_FIELD_BILLING_TELEPHONE = By.id("telephone");
    public static final By ADD_CARD_BUTTON_ADD_NEW_CARD = By.xpath("//div[@class='cardsbmtholder']//button");

    /* Order History Page */
    public static final String MY_ORDERS_TABLE_LINES_LOCATOR = "//table[@id='my-orders-table']//tbody//tr";

    /* Order History: Order Details Page */
    public static final By ORDER_DETAILS_ORDER_STATUS_LOCATOR = By.xpath("//div[@class='page-title title-buttons']/h1");
    public static final By ORDER_DETAILS_ORDER_TOTALS_LOCATOR = By.xpath("//table[@id='my-orders-table']");
    public static final By ORDER_DETAILS_ORDER_SHIPPING_CONTENT_LOCATOR = By.xpath("//h2[contains(.,'Shipping Address:')]/../address[@class='box-content']");
    public static final By ORDER_DETAILS_ORDER_SHIPPING_METHOD_CONTENT_LOCATOR = By.xpath("//h2[contains(.,'Shipping Method:')]/../div[@class='box-content']");
    public static final By ORDER_DETAILS_ORDER_BILLING_CONTENT_LOCATOR = By.xpath("//h2[contains(.,'Billing Address:')]/../address[@class='box-content']");
    public static final By ORDER_DETAILS_ORDER_PAYMENT_CONTENT_LOCATOR = By.xpath("//h2[contains(.,'Payment Method:')]/../div[@class='box-content']");

    /* My Jules page */
    public static final By MAVEN_WINDOW = By.xpath("//div[@class='responsive-tabs-maven-label']");
    public static final String JULES_AWARDED_BUYING_FULL_PRICE_SUBSCRIPTION = "300 Jules";
    public static final String JULES_NOT_AVAILABLE = "0 Jules";
    public static final String JULES_FOR_FREE_MAVEN_BOX = "2000 Jules";
    public static final By JULES_SUMMARY = By.xpath("//span[@class = 'points-summary-emphasize']");

    /* Invite Friends Page */
    public static final By FIELD_INVITE_FRIENDS_REFERAL_URL = By.id("referral_url");

    /* Maven Window(Open): My Monthly Box Page */
    public static final By MY_MONTHLY_BOX_BUTTON_I_M_IN = By.xpath("//a[contains(text(),\"I'm In!\")]");
    public static final By MY_MONTHLY_BOX_SUBSCRIPTION_NAME = By.xpath("//h3[@class='product-name']");
    public static final By SHIPPING_ADDRESS_BLOCK = By.xpath("//div[@class='box-content']/p[1]");

    /* Maven Window(Open): Edit My Box Page */
    public static final By EDIT_MY_BOX_DROPDOWN_SHIP_SKIP = By.id("ship-skip-dropdown");
    public static final By EDIT_MY_BOX_RADIO_SKIP_REASON_NO_MONEY = By.id("reason-1");
    public static final By EDIT_MY_BOX_LINK_I_M_OUT_THIS_MONTH = By.xpath("//a[@class='maven-skip-submit']");
    public static final By EDIT_MY_BOX_LINK_TRY_NEW_STYLE = By.xpath("//a[contains(text(), 'Try a new style')]");
    public static final By EDIT_MY_BOX_LINK_VIEW_ALL_BOXES = By.xpath("//a[contains(text(), 'view all boxes')]");
    public static final By LINK_EDIT_ADDONS = By.xpath("//a[contains(text(), 'Edit Add-Ons')]");
    public static final By SELECTED_COUNT = By.xpath("//div[@class='selectedCount']");
    public static final String MAXIMUM_ADDONS = "6 of 6 selected";
    public static final By LINK_DONE_WITH_ADDONS = By.xpath("//a[contains(text(), 'DONE WITH ADD-ONS')]");
    public static final By LINK_SAVE_YOUR_SELECTIONS = By.xpath("//a[contains(text(), 'Save Your Selections')]");
    public static final By EDIT_MY_BOX_BUTTON_VIEW_ALL_BOXES = By.xpath("//a[@class='show-all button new-style-button ']");
    public static final By BUTTON_SWAP_OUT = By.xpath("//div[@class='swap-out-button']/a");
    public static final String ITEM_SWAP_IN = "//div[@class='swap-in-area']//div[@class='swap-item']";
    public static final By ITEM_SWAP_OUT = By.xpath("//div[@class='current-box-swap']//div[@class='swap-item']");
    public static final By UPGRADE_BANNER = By.xpath("//div[@class='upgrade-maven']/a");

    public static final By SEND_TO_FRIEND_FORM = By.xpath("//ul[@class='form-list']");
    public static final By PRODUCT_GRID_ADDONS = By.xpath("//div[@class='addon-mini-view']/ul/li/div");
    public static final By LINK_REMOVE_ADDONS = By.xpath("//ul[@class='products']//a[@class='button remove']");
    public static final By GRID_MAVEN_ADDONS = By.id("maven-addons");
    public static final By GRID_ADDONS_IN_STOCK = By.xpath("//a[@class='button add']/../../..");

    public static final By CURRENT_STYLE_BOX_TEXT = By.xpath("//span[@class='name']");
    public static final By BUTTON_KEEP_BOX = By.xpath("//button[@class='select button box left selected-button']");
    public static final By BUTTON_SELECT_BOX_ART_WALK_POLISH_UPGRADE = By.xpath("//h2[contains(text(), 'Polish Lover Upgrade')]/../button");
    public static final By BUTTON_SELECT_BOX_CLASSIC_TWIST = By.xpath("//h2[contains(text(), 'Classic with a Twist')]/../button");
    public static final By BUTTON_SELECT_BOX_BOMBSHELL = By.xpath("//h2[contains(text(), 'Bombshell')]/../button");
    public static final By BUTTON_SELECT_BOX_BOHO = By.xpath("//h2[contains(text(), 'Boho Glam')]/../button");
    public static final By BUTTON_SELECT_BOX_MODERN_BEAUTY = By.xpath("//h2[contains(text(), 'Modern Beauty')]/../button");
    public static final By BUTTON_KEEP_MY_CURRENT_BOX = By.xpath("//a[@class='keep-box button new-style-button']");
    public static final By BUTTON_TRY_THIS_BOX_ART_WALK_POLISH_UPGRADE = By.xpath("//h2[contains(text(), 'Upgrade (+$50)')]/../button");
    public static final By CHECKBOX_APPLY_JULES = By.id("apply_jules");
    public static final By BOXES_JS_LOADING_MARKER = By.xpath("//ol[@class='products-list dotted-list']//li[contains(@style, 'overflow: hidden;')]");

    public static final By FIELD_SEND_TO_FRIEND_FIRSTNAME = By.id("firstname");
    public static final By FIELD_SEND_TO_FRIEND_LASTNAME = By.id("lastname");
    public static final By FIELD_SEND_TO_FRIEND_STREET_1 = By.id("street_1");
    public static final By FIELD_SEND_TO_FRIEND_STREET_2 = By.id("street_2");
    public static final By FIELD_SEND_TO_FRIEND_CITY = By.id("city");
    public static final By DROPDOWN_SEND_TO_FRIEND_STATE = By.id("region_id");
    public static final By FIELD_SEND_TO_FRIEND_ZIP = By.id("zip");
    public static final By DROPDOWN_SEND_TO_FRIEND_COUNTRY = By.id("country");
    public static final By FIELD_SEND_TO_FRIEND_PHONE = By.id("telephone");

   public MyAccountPages(WebDriver driver) {
        super(driver);
    }

    /**
     * This method will open My Account page, My account information section.
     */
    public void open() {
        logger.info("Opening URL: " + getStartURL() + PAGE_MY_INFO);
        driver.get(getStartURL() + PAGE_MY_INFO);
        assertEquals("MY ACCOUNT PAGE WAS NOT OPENED!", getStartURL() + PAGE_MY_INFO, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_MY_INFO, is404Page());
    }

    /**
     * This method will open My Account page, Order History section.
     */
    public void openOrderHistory() {
        logger.info("Opening URL: " + getStartURL() + PAGE_ORDER_HISTORY);
        driver.get(getStartURL() + PAGE_ORDER_HISTORY);
        assertEquals("ADD NEW CREDIT CARD PAGE WAS NOT OPENED!", getStartURL() + PAGE_ORDER_HISTORY, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_ORDER_HISTORY, is404Page());
    }

    /**
     * This method will open My Account page, Add new credit card section.
     */
    private void openMyAccountInviteFriendsSection() {
        logger.info("Opening URL: " + getStartURL() + PAGE_INVITE_FRIENDS);
        driver.get(getStartURL() + PAGE_INVITE_FRIENDS);
        assertEquals("INVITE FRIENDS PAGE WAS NOT OPENED!", getStartURL() + PAGE_INVITE_FRIENDS, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_INVITE_FRIENDS, is404Page());
    }

    /**
     * This method will open My Account page, Add new credit card section.
     */
    private void openMyAccountAddNewCC() {
        logger.info("Opening URL: " + getStartURL() + PAGE_ADD_NEW_CARD);
        driver.get(getStartURL() + PAGE_ADD_NEW_CARD);
        assertEquals("ADD NEW CREDIT CARD PAGE WAS NOT OPENED!", getStartURL() + PAGE_ADD_NEW_CARD, driver.getCurrentUrl());// TODO: tests fails on this. Check it please
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_ADD_NEW_CARD, is404Page());
    }

    /**
     * Opens My Account page, Edit My Box section.
     */
    public void openMyAccountEditMyBox() {
        logger.info("Opening URL: " + getStartURL() + PAGE_EDIT_MY_BOX);
        driver.get(getStartURL() + PAGE_EDIT_MY_BOX);
        assertEquals("EDIT MY BOX PAGE WAS NOT OPENED!", getStartURL() + PAGE_EDIT_MY_BOX, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_EDIT_MY_BOX, is404Page());
    }

    /**
     * Opens My Account page, My Monthly Box section.
     */
    public void openMyAccountMyMonthlyBox() {
        logger.info("Opening URL: " + getStartURL() + PAGE_MY_MONTHLY_BOX);
        driver.get(getStartURL() + PAGE_MY_MONTHLY_BOX);
        assertEquals("EDIT MY BOX PAGE WAS NOT OPENED!", getStartURL() + PAGE_MY_MONTHLY_BOX, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_MY_MONTHLY_BOX, is404Page());
    }

    /**
     * Adds new Credit Card in my account for logged in user.
     */
    //TODO: Add amex and master card as argument. Add another addresses as second argument.
    public void addNewCC() {
        logger.info("Starting to Adds new Credit Card in My Account.");
        assertTrue("CUSTOMER IS NOT LOGGED IN!", isLoggedIn());
        logger.info("Clicking on \"ADD CARD\" button");
        openMyAccountAddNewCC();
                /* List of input fields and DropDowns which needed to be filled in. */
        ArrayList<Field> myAccNewCardFields = new ArrayList<Field>();
                /* type, key is input field locator(class By) , value is String filling in this input */
        myAccNewCardFields.add(new Field(Field.SELECT, ADD_CARD_DROPDOWN_CREDIT_CARD_TYPE, TEST_CARD_TYPE));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_CREDIT_CARD_NUMBER, TEST_CARD_NUMBER));
        myAccNewCardFields.add(new Field(Field.SELECT, ADD_CARD_DROPDOWN_EXPIRATION_MONTH, TEST_CARD_MONTH));
        myAccNewCardFields.add(new Field(Field.SELECT, ADD_CARD_DROPDOWN_EXPIRATION_YEAR, TEST_CARD_YEAR));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_FIRST_NAME, TEST_FIRST_NAME));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_LAST_NAME, TEST_LAST_NAME));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_STREET_ADDRESS1, WA_GUEST_STREET_1));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_STREET_ADDRESS2, WA_GUEST_STREET_2));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_CITY, WA_GUEST_CITY));
        myAccNewCardFields.add(new Field(Field.SELECT, ADD_CARD_FIELD_BILLING_STATE, WA_GUEST_STATE));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_ZIP, WA_GUEST_ZIP));
        myAccNewCardFields.add(new Field(Field.SELECT, ADD_CARD_FIELD_BILLING_COUNTRY, WA_GUEST_COUNTRY));
        myAccNewCardFields.add(new Field(Field.INPUT, ADD_CARD_FIELD_BILLING_TELEPHONE, WA_GUEST_PHONE));
        // fill all fields and dropdowns
        fillFieldsSet(myAccNewCardFields);
        clickOnElement(ADD_CARD_BUTTON_ADD_NEW_CARD, "Button: Add New Card");
        waitForElementIsVisible(MyAccountPages.SUCCESS_MESSAGE);
        //Verify Credit card is added by the changing of the page
//       assertFalse("CREDIT CARD WAS NOT UPDATED!", driver.getCurrentUrl().endsWith(MY_ACC_ADD_NEW_CARD_PAGE));
        logger.info("Credit Card was successfully added!");
    }

    /**
     * Adds new Shipping Address(or updates existing) in my account for logged in user.
     */
    public void addNewShippingAddress() {
        logger.info("Starting to Update Shipping Address in My Account.");
        assertTrue("CUSTOMER IS LOGGED IN!", isLoggedIn());
        clickOnElement(MY_INFO_LINK_UPDATE_SHIPPING, "Update Sipping Link/Button");
                /* List of input fields which needed to be filled in. */
        ArrayList<Field> myAccUpdateShippingInputs = new ArrayList<Field>();
                /* type, key is input field locator(class By) , value is String filling in this input */
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_FIRST_NAME, TEST_FIRST_NAME));
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_LAST_NAME, TEST_LAST_NAME));
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_TELEPHONE, WA_GUEST_PHONE));
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_STREET_ADDRESS_1, WA_GUEST_STREET_1));
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_STREET_ADDRESS_2, WA_GUEST_STREET_2));
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_CITY, WA_GUEST_CITY));
        myAccUpdateShippingInputs.add(new Field(Field.SELECT, MY_ACC_SHIPPING_DROPDOWN_STATE, WA_GUEST_STATE));
        myAccUpdateShippingInputs.add(new Field(Field.INPUT, MY_ACC_SHIPPING_FIELD_ZIP, WA_GUEST_ZIP));
        myAccUpdateShippingInputs.add(new Field(Field.SELECT, MY_ACC_SHIPPING_DROPDOWN_COUNTRY, WA_GUEST_COUNTRY));
        // fill all fields and dropdowns
        fillFieldsSet(myAccUpdateShippingInputs);
        // save
        waitForElementToBeClickable(MY_ACC_SHIPPING_BUTTON_UPDATE_ADDRESS);
        driver.findElement(MY_ACC_SHIPPING_BUTTON_UPDATE_ADDRESS).submit();
        waitForElementIsVisible(MyAccountPages.SUCCESS_MESSAGE);
        //Verify Sipping Address was updated by the changing of the page
        assertTrue("SHIPPING ADDRESS WAS NOT UPDATED!", driver.getCurrentUrl().endsWith(PAGE_MY_INFO));
        logger.info("Shipping Address was successfully updated!");
    }

    /**
     * Searches for the last(first in table) Order in the Order History table and returns object with order details.
     *
     * @return Object[5]: String "Order #", String "Date", String "Ship To", String "Total", String "Order Details URL"
     */
    public List<String> getLastOrderFromOrderHistoryTable() {
        List<WebElement> myOrdersList = driver.findElements(By.xpath(MY_ORDERS_TABLE_LINES_LOCATOR));
        assertFalse("NO ORDERS FOUND IN ODER HISTORY TABLE!", myOrdersList.isEmpty());
        logger.info("Got last order, extracting details...");
        List<WebElement> lastOrderDetails = driver.findElements(By.xpath(MY_ORDERS_TABLE_LINES_LOCATOR + "/td"));
        ArrayList<String> orderDetails = new ArrayList<>();
        orderDetails.add(lastOrderDetails.get(0).getText());
        orderDetails.add(lastOrderDetails.get(1).getText());
        orderDetails.add(lastOrderDetails.get(2).getText());
        orderDetails.add(lastOrderDetails.get(3).getText());
        orderDetails.add(lastOrderDetails.get(4).findElement(By.tagName("a")).getAttribute("href"));
        return orderDetails;
    }

    /**
     * Checks all order details blocks(Status, Shipping,Shipping Method, Billing, Payment Method and Totals)
     * are visible on the Order details Page. And throws exception is something is missed or not visible.
     */
    public void verifyOrderDetailsAreDisplayed() {
        assertTrue(driver.findElement(ORDER_DETAILS_ORDER_STATUS_LOCATOR).isDisplayed());
        assertTrue(driver.findElement(ORDER_DETAILS_ORDER_SHIPPING_CONTENT_LOCATOR).isDisplayed());
        assertTrue(driver.findElement(ORDER_DETAILS_ORDER_SHIPPING_METHOD_CONTENT_LOCATOR).isDisplayed());
        assertTrue(driver.findElement(ORDER_DETAILS_ORDER_BILLING_CONTENT_LOCATOR).isDisplayed());
        assertTrue(driver.findElement(ORDER_DETAILS_ORDER_PAYMENT_CONTENT_LOCATOR).isDisplayed());
        assertTrue(driver.findElement(ORDER_DETAILS_ORDER_TOTALS_LOCATOR).isDisplayed());
    }

    /**
     * takes a referral url and referal code from it
     *
     * @return referral code as a String
     */
    public String getReferralUrl() {
        openMyAccountInviteFriendsSection();
        assertTrue("REFERAL URL IS ABSENT", driver.findElement(FIELD_INVITE_FRIENDS_REFERAL_URL).isDisplayed());
        return driver.findElement(FIELD_INVITE_FRIENDS_REFERAL_URL).getAttribute("value");
    }


    /**
     * This method will open My Account page, Order Details page for first Order in the list.
     */
    public void openOrderStatusPage() {
        openOrderHistoryPage();
        if (isElementPresent(LINK_ORDER_VIEW_FIRST)) {
            driver.findElement(LINK_ORDER_VIEW_FIRST).click();
        } else {
            throw new TestFrameworkRuntimeException("COULD NOT FIND ELEMENT: LINK ORDER VIEW");
        }
    }

    /**
     * This method will open My Account page, Order Details page for first Order in the list.
     */
    public void openOrderHistoryPage() {
        logger.info("Opening URL: " + getStartURL() + PAGE_ORDER_HISTORY);
        driver.get(getStartURL() + PAGE_ORDER_HISTORY);
        assertEquals("ADD NEW CREDIT CARD PAGE WAS NOT OPENED!", getStartURL() + PAGE_ORDER_HISTORY, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_ORDER_HISTORY, is404Page());
    }

    /**
     * This method will open My Account page, My Wishlist section.
     */
    public void openMyWishlistPage() {
        logger.info("Opening URL: " + getStartURL() + PAGE_MY_WISHLIST);
        driver.get(getStartURL() + PAGE_MY_WISHLIST);
        assertEquals("ADD NEW CREDIT CARD PAGE WAS NOT OPENED!", getStartURL() + PAGE_MY_WISHLIST, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_MY_WISHLIST, is404Page());
    }

    /**
     * This method will open My Account page, My Wishlist section.
     */
    public void clickButtonShareWishlist() {
        if (isElementPresent(BUTTON_SHARE_WISHLIST)) {
            driver.findElement(BUTTON_SHARE_WISHLIST).click();
        } else {
            throw new TestFrameworkRuntimeException("COULD NOT FIND ELEMENT: BUTTON SHARE WISHLIST");
        }
    }

    /**
     * This method will open My Account page, Edi Credit Card section.
     */
    public void openEditCardSection() {
        openBillingInformationSection();
        if (isElementPresent(BILLING_INFO_BUTTON_EDIT_CARD)) {
            driver.findElement(BILLING_INFO_BUTTON_EDIT_CARD).click();
        } else {
            throw new TestFrameworkRuntimeException("COULD NOT FIND ELEMENT: EDIT CARD BUTTON");
        }
    }

    /**
     * This method will open My Account page, Billing Information section.
     */
    public void openBillingInformationSection() {
        logger.info("Opening URL: " + getStartURL() + PAGE_BILLING_INFO);
        driver.get(getStartURL() + PAGE_BILLING_INFO);
        assertEquals("BILLING INFO PAGE WAS NOT OPENED!", getStartURL() + PAGE_BILLING_INFO, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL() + PAGE_BILLING_INFO, is404Page());
    }


    /**
     * This method will open My Account page, Update Address section.
     */
    public void openUpdateAddressSection() {
        open();
        if (isElementPresent(LINK_UPDATE_ADDRESS)) {
            driver.findElement(LINK_UPDATE_ADDRESS).click();
        } else {
            throw new TestFrameworkRuntimeException("COULD NOT FIND ELEMENT: LINK FOR UPDATE AN ADDRESS");
        }
    }

}
