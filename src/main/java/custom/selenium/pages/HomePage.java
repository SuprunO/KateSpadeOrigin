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
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static custom.selenium.TestFactory.getStartURL;
import static org.junit.Assert.*;

/**
 * A class, which contains implemented actions and verifications, which can be performed on Home page
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class HomePage extends PageFactory {

    public Logger logger = Logger.getLogger(TestFactory.class);

    /* Home Page elements */
    public static final String PATH_TO_BESTSELLERS_LIST = "//ul[contains(@class,'products-grid')]";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens Home Page entering url into the address field.
     */
    public void open() {
        logger.info("Opening URL: " + getStartURL());
        driver.get(getStartURL());
        assertEquals("HOME PAGE WAS NOT OPENED", getStartURL(), driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + getStartURL(), is404Page());
    }

    /**
     * Extracts products' names of "In Stock" product from current page and put it to List for further usage.
     *
     * @return List of Strings
     */
    public List<String> getListOfBestsellersNames() {
        List<String> inStockNames = new ArrayList<String>();
        assertTrue("NO PRODUCTS GRID AVAILABLE.", isElementPresent(By.xpath(PATH_TO_BESTSELLERS_LIST)));
        for (WebElement link : driver.findElements(By.xpath(PATH_TO_BESTSELLERS_LIST +
                "/li/div/a"))) {
            inStockNames.add(link.getAttribute("title"));
        }
        return inStockNames;
    }
}
