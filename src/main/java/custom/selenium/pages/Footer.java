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
import org.openqa.selenium.WebDriver;

/**
 * A class, which contains implemented actions and verifications, which can be performed for Global elements(footer)
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class Footer extends PageFactory {

    public Logger logger = Logger.getLogger(TestFactory.class);

    /* Footer Elements */
    public static final By LINK_CONTACT_US = By.xpath("//div[@id='footer-links']/ul/li/a[contains(text(),'Contact Us')]");
    public static final By LINK_SHIPPING_AND_DELIVERY = By.xpath("//div[@id='footer-links']/ul/li/a[contains(text(),'Shipping & Delivery')]");
    public static final By LINK_RETURNS_AND_EXCHANGES = By.xpath("//div[@id='footer-links']/ul/li/a[contains(text(),'Returns & Exchanges')]");
    public static final By LINK_REWARD_PROGRAM = By.xpath("//div[@id='footer-links']/ul/li/a[contains(text(),'Reward Program')]");
    public static final By LINK_FACEBOOK = By.xpath("//div[@id='footer-social']//a[contains(text(),'Facebook')]");
    public static final By LINK_PINTEREST = By.xpath("//div[@id='footer-social']//a[contains(text(),'Pinterest')]");
    public static final By LINK_INSTAGRAM = By.xpath("//div[@id='footer-social']//a[contains(text(),'Instagram')]");
    public static final By LINK_TWITTER = By.xpath("//div[@id='footer-social']//a[contains(text(),'Twitter')]");
    public static final By LINK_BLOG = By.xpath("//div[@id='footer-social']//a[contains(text(),'Blog')]");
    public static final By LINK_YOUTUBE = By.xpath("//div[@id='footer-social']//a[contains(text(),'YouTube')]");
    public static final By BUTTON_SIGN_UP = By.xpath("//button[@title='Sign Up']");
    public static final By LINK_VISIT_THE_PAROLS = By.xpath("//ul[@id='navfooter']//span[contains(text(),'Visit the Parlors')]/../a");
    public static final By LINK_PARLOR_GIFT_CARDS = By.xpath("//ul[@id='navfooter']//span[contains(text(),'Parlor Gift Cards')]/..");
    public static final By LINK_HOW_IT_WORKS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'How it Works')]");
    public static final By LINK_STYLE_PROFILES = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Style Profiles')]");
    public static final By LINK_MAVEN_FAQS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Maven FAQs')]");
    public static final By LINK_GIFT_OF_MAVEN = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Gift of Maven')]");
    public static final By LINK_TAKE_THE_QUIZ_AND_JOIN = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Take the Quiz & Join')]");
    public static final By LINK_HOLIDAY_2014 = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Holiday 2014')]");
    public static final By LINK_NEW_ARRIVALS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'New Arrivals')]");
    public static final By LINK_NAIL_POLISH = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Nail Polish')]");
    public static final By LINK_NAIL_HAND_FOOT_CARE = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Nail, Hand & Foot Care')]");
    public static final By LINK_BODY = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Body')]");
    public static final By LINK_ABOUT_JULEP = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'About Julep')]");
    public static final By LINK_CAREERS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Careers')]");
    public static final By LINK_PRESS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Press')]");
    public static final By LINK_CONTACT_US_PARLORS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Contact Us')]");
    public static final By LINK_JULEP_ON_TV = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Julep on TV')]");
    public static final By LINK_LOCATIONS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Locations')]");
    public static final By LINK_SERVICES_MENU = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Services Menu')]");
    public static final By LINK_PARLOR_MEMBERSHIPS = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Parlor Memberships')]");
    public static final By LINK_BOOKING_AND_EVENT = By.xpath("//ul[@id='navfooter']//a/span[contains(text(),'Booking an Event')]");

    public Footer(WebDriver driver) {
        super(driver);
    }


    /**
     * use this function to find and click a link in the customer service section in footer
     * @param item  "String" name of link
     */
    public void findAndClickCustomerServiceLink(String item) throws Exception {
        clickOnElement(By.xpath("//div[@id='footer-links-container']//span[contains(text(),'" + item + "')]"), item);
    }
}
