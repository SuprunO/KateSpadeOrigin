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
public class CheckoutBillingPage extends PageFactory {

    public Logger logger = Logger.getLogger(CheckoutBillingPage.class);

    public static final By BUTTON_PLACE_ORDER = By.xpath("//button[@class='btn-cart btn-checkout pink-button']");

    /* Billing Address Step */
    public static final String ID_BILLING_ADDRESS_SAME_AS_SHIPPING_CHECKBOX = "shipping:use_for_billing";
    public static final By CHECKBOX_BILLING_ADDRESS_SAME_AS_SHIPPING = By.id(ID_BILLING_ADDRESS_SAME_AS_SHIPPING_CHECKBOX);
    public static final By DROPDOWN_BILLING_INFO = By.id("billing-address-select");
    public static final By TEXT_BILLING_INFO_DISPLAYED_INFO = By.id("current-billing-customer-address");
    public static final By FORM_BILLING_INFO_NEW_ADDRESS = By.id("billing-new-address-form");
    public static final By FIELD_BILLING_ADDRESS_FIRST_NAME = By.id("billing:firstname");
    public static final By FIELD_BILLING_ADDRESS_LAST_NAME = By.id("billing:lastname");
    public static final By FIELD_BILLING_ADDRESS_STREET_1 = By.id("billing:street1");
    public static final By FIELD_BILLING_ADDRESS_STREET_2 = By.id("billing:street2");
    public static final By FIELD_BILLING_ADDRESS_CITY = By.id("billing:city");
    public static final By DROPDOWN_BILLING_ADDRESS_STATE = By.id("billing:region_id");
    public static final By DROPDOWN_BILLING_ADDRESS_COUNTRY = By.id("billing:country_id");
    public static final By FIELD_BILLING_ADDRESS_ZIP_CODE = By.id("billing:postcode");
    public static final By FIELD_BILLING_ADDRESS_TELEPHONE = By.id("billing:telephone");

    /* Billing Credit Card(CC) Step */
    public static final By DROPDOWN_BILLING_CC = By.id("tokens");
    public static final By FORM_BILLING_CC_NEW_CARD = By.id("new_card_fields");
    public static final By CHECKBOX_BILLING_CC_SET_NEW_CC_AS_DEFAULT = By.id("set_as_default");
    public static final By FIELD_BILLING_CC_NUMBER = By.id("creditcard_cc_number");
    public static final By DROPDOWN_BILLING_CC_TYPE = By.id("creditcard_cc_type");     // investigate usage! no such dropdown...
    public static final By DROPDOWN_BILLING_CC_EXPIRATION_MONTH = By.id("creditcard_expiration");
    public static final By DROPDOWN_BILLING_CC_EXPIRATION_YEAR = By.id("creditcard_expiration_yr");


    public CheckoutBillingPage(WebDriver driver) {
        super(driver);
    }


    /**
     * Checks "Billing address same as shipping" checkbox if it is not already checked.
     */
    public void checkCheckboxBillingSameAsShipping() {
        checkCheckbox(CHECKBOX_BILLING_ADDRESS_SAME_AS_SHIPPING, "Billing address same as shipping");
    }

    /**
     * Un-checks "Billing address same as shipping" checkbox if it is not already un-checked.
     */
    public void uncheckSameAsShippingCheckbox() {
        uncheckCheckbox(CHECKBOX_BILLING_ADDRESS_SAME_AS_SHIPPING, "Billing address same as shipping");
    }

    /**
     * Fills in Billing Address Form with default values.
     */
    public void fillBillingAddressForm() {
        fillBillingAddressForm("");
    }

    /**
     * Fills in Billing Address Form with values based on specified state name.
     * Note: If argument is not recognized fills in form with default values.
     *
     * @param stateName String "NEW_JERSEY"|"NEW_YORK"|"WASHINGTON"|"COLORADO"|any string
     */
    public void fillBillingAddressForm(String stateName) {
        // Get all fields and dropdowns to fill by specified state
        ArrayList<Field> billingAddressFields = getBillingFieldSet(stateName);
        assertThat("FIELD SET IS BLANK!", billingAddressFields.size(), not(0));
        logger.info("Fill out Billing Address Form");
        fillFieldsSet(billingAddressFields);
    }

    /**
     * fills in Billing Credit Card Form
     */
    public void fillBillingCCForm() {
        logger.info("Fill out Billing Credit Card Form");
        waitForElementIsVisible(FIELD_BILLING_CC_NUMBER);
        fillInInput(FIELD_BILLING_CC_NUMBER, TEST_CARD_NUMBER);
        selectValueInDropDown(DROPDOWN_BILLING_CC_EXPIRATION_MONTH, TEST_CARD_MONTH);
        selectValueInDropDown(DROPDOWN_BILLING_CC_EXPIRATION_YEAR, TEST_CARD_YEAR);
    }

     /**
     * Creates and returns Billing address fields to fill on checkout with default values.
     *
     * @return ArrayList<Field> Fields set which is used in fillFieldsSet() method as argument
     */
    private ArrayList<Field> getBillingFieldSet() {
        return getBillingFieldSet("");
    }

    /**
     * Creates and returns Billing address fields to fill on checkout with values based on specified state name.
     * Note: If argument is not recognized fields with default values will be returned
     *
     * @param stateName String "NEW_JERSEY"|"NEW_YORK"|"WASHINGTON"|"COLORADO"|any string
     * @return ArrayList<Field> Fields set which is used in fillFieldsSet() method as argument
     */
    private ArrayList<Field> getBillingFieldSet(String stateName) {
        ArrayList<Field> resultFieldSet = new ArrayList<Field>();
        // create billing field sets for every declared state
        resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_FIRST_NAME, TEST_FIRST_NAME));
        resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_LAST_NAME, TEST_LAST_NAME));
        if (stateName.equals("NEW_JERSEY")) {
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_1, NJ_GUEST_STREET_1));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_2, NJ_GUEST_STREET_2));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_CITY, NJ_GUEST_CITY));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_STATE, NJ_GUEST_STATE));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_ZIP_CODE, NJ_GUEST_ZIP));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_COUNTRY, NJ_GUEST_COUNTRY));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_TELEPHONE, NJ_GUEST_PHONE));
        } else if (stateName.equals("NEW_YORK")) {
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_1, NY_GUEST_STREET_1));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_2, NY_GUEST_STREET_2));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_CITY, NY_GUEST_CITY));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_STATE, NY_GUEST_STATE));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_ZIP_CODE, NY_GUEST_ZIP));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_COUNTRY, NY_GUEST_COUNTRY));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_TELEPHONE, NY_GUEST_PHONE));
        } else if (stateName.equals("WASHINGTON")) {
            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_1, WA_GUEST_STREET_1));
            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_2, WA_GUEST_STREET_2));
            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_CITY, WA_GUEST_CITY));
            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_STATE, WA_GUEST_STATE));
            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_ZIP_CODE, WA_GUEST_ZIP));
            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_COUNTRY, WA_GUEST_COUNTRY));
            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_TELEPHONE, WA_GUEST_PHONE));
        } else if (stateName.equals("COLORADO")) {
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_1, COL_GUEST_STREET_1));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_2, COL_GUEST_STREET_2));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_CITY, COL_GUEST_CITY));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_STATE, COL_GUEST_STATE));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_ZIP_CODE, COL_GUEST_ZIP));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_COUNTRY, COL_GUEST_COUNTRY));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_TELEPHONE, COL_GUEST_PHONE));
        } else if (stateName.equals("OHIO")) {
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_1, OH_GUEST_STREET_1));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_2, OH_GUEST_STREET_2));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_CITY, OH_GUEST_CITY));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_STATE, OH_GUEST_STATE));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_ZIP_CODE, OH_GUEST_ZIP));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_COUNTRY, OH_GUEST_COUNTRY));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_TELEPHONE, OH_GUEST_PHONE));
        } else {
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_1, GUEST_STREET_1));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_STREET_2, GUEST_STREET_2));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_CITY, GUEST_CITY));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_STATE, GUEST_STATE));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_ZIP_CODE, GUEST_ZIP));
//            resultFieldSet.add(new Field("dropdown", DROPDOWN_BILLING_ADDRESS_COUNTRY, GUEST_COUNTRY));
//            resultFieldSet.add(new Field("input", FIELD_BILLING_ADDRESS_TELEPHONE, GUEST_PHONE));
        }
        return resultFieldSet;
    }

}
