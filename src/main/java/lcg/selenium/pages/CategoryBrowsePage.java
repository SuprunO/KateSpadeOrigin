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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;


/**
 * A class, which contains implemented actions and verifications, which can be performed on Category landing pages
 *
 * @author Speroteck QA Team (qa@speroteck.com)
 */
public class CategoryBrowsePage extends PageFactory {

    public Logger logger = Logger.getLogger(CategoryBrowsePage.class);

    /* Category landing pages */
    public static final String PAGE_GIVE_A_GIFT = "shop/gift.html";
    public static final String PAGE_JULE_BOX = "shop/jule-box.html";
    public static final String PAGE_SAVVY_DEALS = "shop/savvy-deals.html";
    public static final String PAGE_CYNTHIA = "shop/cynthia.html";
    public static final String PAGE_SHEILA = "shop/sheila.html";
    public static final String PAGE_GIFT_BIRTHDAY = "shop/gift/birthday.html";
    public static final String PAGE_SHOP = "shop.html";
    public static final String PAGE_NAIL_POLISH = "shop/nail-polish.html";
    public static final String PAGE_HOLIDAY_2014 = "shop/holiday-2014.html";
    public static final String PAGE_JULEP_PLIE_WAND = "shop/plie-wand.html";
    public static final String PAGE_NEW_ARRIVALS = "shop/new-arrivals.html";
    public static final String PAGE_BEST_SELLERS = "shop/best-sellers.html";
    public static final String PAGE_COLLECTIONS = "shop/collections.html";
    public static final String PAGE_NAIL_HAND_FOOT_CARE = "shop/nail-hand-foot-care.html";
    public static final String PAGE_MAKEUP_AND_HAIR = "shop/makeup-hair.html";
    public static final String PAGE_SKINCARE = "shop/skincare.html";
    public static final String PAGE_BODY = "shop/body.html";
    public static final String PAGE_MAVEN_SUBSCRIPTION = "maven-subscription.html";

    /* Category landing pages Elements */
    public static final By META_ROBOTS_LOCATOR = By.xpath("//meta[@name='robots']");
    public static final By NEXT_PAGE_IN_HEAD_LOCATOR = By.xpath("//link[@rel='next']");
    public static final By POP_UP_MINICART = By.xpath("//div[contains(@id,'responsive-top-cart')]");

    public static final String PATH_TO_PRODUCTS_LIST = "//div[contains(@class,'products-grid')]";
    public static final String PATH_TO_PRODUCTS_LIST_SEARCH = "//ul[contains(@class,'products-grid')]";
    public static final String PATH_TO_PRODUCTS_LINKS = "//h2[@class='product-name']/a";
    public static final String PATH_TO_PRODUCTS_LINK = "/h2[@class='product-name']/a";
    public static final String BUTTON_ADD_TO_BAG = "//button[@title='Add to Bag']";

    public static final String BLOCK_OF_FILTERS_NAIL_COLOR_XPATH = "//div[@id='filter-code-nail_color']";
    public static final String BLOCK_OF_FILTERS_NAIL_FINISH_XPATH = "//div[@id='filter-code-nail_finish']";
    public static final String BLOCK_OF_FILTERS_STYLE_PROFILE_XPATH = "//div[@id='filter-code-style_profile']";
    public static final By AJAX_LOADING_MASK = By.xpath("//div[@class='responsive-shop-step-loading']");

    public CategoryLandingPage(WebDriver driver) {
        super(driver);
    }

    /**
     * This method will open Category page, url of which ends with categoryPath.
     *
     * @param categoryPath String, categoryPath additional path to the base url, which allows to open category by url.
     */
    public void open(String categoryPath) {
        logger.info("Opening " + categoryPath + " URL: " + baseUrl + categoryPath);
        driver.get(baseUrl + categoryPath);
        assertEquals(categoryPath.toUpperCase() + " PAGE WAS NOT OPENED", baseUrl + categoryPath, driver.getCurrentUrl());
        assertFalse("404 PAGE IS OPENED! BUT EXPECTED: " + baseUrl + categoryPath, is404Page());
    }

    /**
     * Searches the target area for filters and creates ArrayList of found filters.
     *
     * @param selector By target area to search filters
     * @return ArrayList of SEOColorLink objects contains(String colorId, String colorName, Integer count, String url)
     */
    public ArrayList<SEOColorLink> getFilterListFromBlock(String selector) {
        logger.info("Collecting filters ...");

        /* Nail Color items to parse */
        List<WebElement> listOfFilters = driver.findElements(By.xpath(selector + "//li[@class='responsive-wrapper-narrow-by']//a"));

         /* Result List of color filter elements*/
        ArrayList<SEOColorLink> myColorList = new ArrayList<SEOColorLink>();
        logger.info("Extracting information about filters (count, name, URL, colorId)");
        for (WebElement entry : listOfFilters) {
            String colorId = entry.getAttribute("value");
            String colorName = entry.getText().replaceAll("[^a-zA-Z]", "").trim();
            // Cast is not working in the single line, so splitting extracting and casting to separate variables here
            String counts = entry.getAttribute("count");
            int count = Integer.parseInt(counts);
            String url = entry.getAttribute("href");
            /* Some local urls use port, this will help to add port to the magento generated urls in DOM of pages */
            String port = System.getProperty("port");
            if (port != null) {
                url = url.replace(".com/", ".com:" + port + "/");
                System.out.println(" Changing URL for local testing. Port " + port + " is added " + url);
            }
            logger.info("Adding " + colorName + " filter to ArrayList with the following values: " + colorId + ", " + colorName + ", " + count + ", " + url + ".");
            myColorList.add(new SEOColorLink(colorId, colorName, count, url));
        }
        logger.info("Extracted: " + myColorList.size() + " filters");
        return myColorList;
    }

    /**
     * Counts product on the current page and returns result.
     *
     * @return int number of products that present in the product grid
     */
    public int countProductsInGrid() {
        int count;
        logger.info("Counting products in grid");
        assertTrue("PRODUCT GRID IS NOT DISPLAYED ON THE PAGE", driver.findElement(By.xpath(PATH_TO_PRODUCTS_LIST)).isDisplayed());
        count = driver.findElements(By.xpath(PATH_TO_PRODUCTS_LIST + "//li[contains(@class, 'item')]")).size();
        logger.info("Found " + count + " products in grid");
        return count;
    }

    /**
     * Walks through pages by opening the "next" page if such link is present in the header(). Starts from the current opened page.
     * Finds "robots" meta tag value using getMetaTegRobots() and store it for verification and return it in the object,
     * validates that "robots" meta tag value is not changes on the "next" pages,
     * counts products on the opened pages(inclusive start page) and returns count results in object.
     *
     * @return Object with 2 elements: 1st element is int product count, 2nd element is String "robots" meta tag value
     */
    public Object[] verifyAndSaveCountAndRobotsTagLoadingNextPage() {
        int total = countProductsInGrid();          // store to add count from the next pages is such present
        String metaRobots = getMetaTegRobots();     // Store "robots" meta tag value for return and verification
        logger.info("Searching for the next page..");
        // verify if next page is present to make loop stopper
        boolean hasNext = isElementPresent(NEXT_PAGE_IN_HEAD_LOCATOR);
        while (hasNext) {
            // get and store next page URL for friendly logs
            logger.info("Next page exist, proceeding it");
            String nextPageUrl = driver.findElement(NEXT_PAGE_IN_HEAD_LOCATOR).getAttribute("href");
            logger.info("Opening page: " + nextPageUrl);
            driver.get(nextPageUrl);
            // Verify that meta robots is the same, if not it's a bug, stop test from here
            logger.info("Verifying robots meta tag value is " + metaRobots);
            assertTrue("META TEG ROBOTS HAS ANOTHER VALUE ON" + nextPageUrl, getMetaTegRobots().equals(metaRobots));
            logger.info("Adding found products to total");
            total += countProductsInGrid();     // count, may be should be split into separate variables for better logs
            logger.info("Searching for the next page..");
            hasNext = isElementPresent(NEXT_PAGE_IN_HEAD_LOCATOR);      // search for the next page to trigger loop stop
        }
        logger.info("Found " + total + " products in total");
        logger.info("Found robots meta tag value is " + metaRobots);
        Object[] results = new Object[2];
        results[0] = total;
        results[1] = metaRobots;
        return results;
    }

    /**
     * Finds meta tag "Robots" and returns it's content if present. If not present returns null
     *
     * @return String|null
     */
    public String getMetaTegRobots() {
        logger.info("Getting robots value from the current page");
        return isElementPresent(META_ROBOTS_LOCATOR) ? driver.findElement(META_ROBOTS_LOCATOR).getAttribute("content") : null;
    }


    /**
     * Extracts products' names of "In Stock" product from current page and put it to List for further usage.
     *
     * @return List of Strings
     */
    public List<String> getListOfInStockProductNames() {
        List<String> inStockNames = new ArrayList<String>();
        assertTrue("NO PRODUCTS GRID AVAILABLE.", isElementPresent(By.xpath(PATH_TO_PRODUCTS_LIST)));
        for (WebElement link : driver.findElements(By.xpath(PATH_TO_PRODUCTS_LIST +
                "//div[@class='btn']/button[@title='Add to Bag']/../../../.." + PATH_TO_PRODUCTS_LINK))) {
            inStockNames.add(link.getAttribute("title"));
        }
        return inStockNames;
    }

    /**
     * Extracts products' names from current page and put it to List for further comparing with the next page.
     *
     * @return List of Strings
     */
    public List<String> getListOfProductNames() {
        List<String> names = new ArrayList<String>();
        assertTrue("NO PRODUCTS GRID AVAILABLE.", isElementPresent(By.xpath(PATH_TO_PRODUCTS_LIST)));
        for (WebElement link : driver.findElements(By.xpath(PATH_TO_PRODUCTS_LIST + PATH_TO_PRODUCTS_LINKS))) {
            String name = link.getText().split("\n")[0];
            names.add(name);
        }
        return names;
    }

    /**
     * Extracts products' names from current search results page and put it to List for further comparing with the next page.
     *
     * @return List of Strings
     */
    public List<String> getListOfProductNamesSearchResults() {
        List<String> names = new ArrayList<String>();
        for (WebElement link : driver.findElements(By.xpath(PATH_TO_PRODUCTS_LIST_SEARCH + PATH_TO_PRODUCTS_LINKS))) {
            names.add(link.getText());
        }
        return names;
    }

    /**
     * Extracts total products value from Source page code: from Lazy Loader JS.
     * @return  int: number of total product on page extracted from Lazy Loader JS.
     */
    public int getTotalProductsFromJS(){
        int total;
        String expectedCountOfProductsString = "";
        String pageSource = driver.getPageSource();
        Pattern p = Pattern.compile("'totalProducts': \\d+");
        Matcher matcher = p.matcher(pageSource);
        if (matcher.find()) {
            expectedCountOfProductsString = matcher.group();
        }
        total = Integer.parseInt(expectedCountOfProductsString.replaceAll("[^0-9]", ""));
        return total;
    }

    /**
     * Method perform wait till ajax mask opens, which indicate loading of next group of products in grid
     * And then perform wait till jax mask will disappear
     */
    public void waitTillNextGroupOfProductsWillBeLoadedInGrid() {
        logger.info("Waiting till products will load in the grid");
        waitForElementIsVisible(AJAX_LOADING_MASK, 2);
        waitForElementNotVisible(AJAX_LOADING_MASK, 300);
    }

    /**
     * Method search first product in grid and verifying his name to match expected name
     */
    public void verifyProductInGridByName(String productName) {
        ArrayList<String> actualProductNames = new ArrayList<String>();
        List<String> actualProductNamesFull = getListOfProductNamesSearchResults();
        for (String actualProductNameFull : actualProductNamesFull) {
            if (actualProductNameFull.contains("\n")) {
                actualProductNames.add(actualProductNameFull.substring(0, actualProductNameFull.indexOf("\n")));
            } else {
                actualProductNames.add(actualProductNameFull);
            }
        }
        assertTrue("Product: " + productName + " was not found on Search results page", actualProductNames.contains(productName));
    }

    /**
     * Opens Product detail page by clicking on link on Category landing page.
     *
     * @param productName String, productName it is a real product on current Category
     */
    public void openPdpByProductName(String productName) {
        assertTrue("LOOKS LIKE PRODUCT: \"" + productName + "\" IS OUT OF STOCK, BECAUSE ITEM WAS NOT FOUND ON THE PAGE",
                isElementPresent(By.xpath(PATH_TO_PRODUCTS_LIST + "//h2/a[contains(text(), \"" + productName + "\")]")));
        String productPageUrl = driver.findElement(By.xpath(PATH_TO_PRODUCTS_LIST + "//h2/a[contains(text(), \"" + productName
                + "\")]")).getAttribute("href");
        logger.info("Opening product " + productName + " details page");
        driver.get(productPageUrl);
    }

    /**
     * Searches Product by specified name on the page, hovers on it's Title and clicks "Add to Bag" button with necessary waiting.
     *
     * @param productName String Name of the product to add to cart. It is a real product on current CLP.
     */
    public void addToCartByProductName(String productName) {
        assertTrue("COULD NOT FIND PRODUCT: \"" + productName + "\" ON THE PAGE",
                isElementPresent(By.xpath(PATH_TO_PRODUCTS_LIST + "//h2/a[contains(text(), \"" + productName + "\")]")));
        Actions action = new Actions(driver);
        WebElement elemToHover = driver.findElement(By.xpath(PATH_TO_PRODUCTS_LIST + "//h2/a[contains(text(), \"" + productName + "\")]"));
        action.moveToElement(elemToHover);
        action.perform();
        waitForElementIsVisible(By.xpath(PATH_TO_PRODUCTS_LIST + "//h2/a[contains(text(), \"" + productName + "\")]/../.." + BUTTON_ADD_TO_BAG), 3);
        logger.info("Adding \"" + productName + "\" product to cart clicking on \"Add to Bag\" button");
        driver.findElement(By.xpath(PATH_TO_PRODUCTS_LIST + "//h2/a[contains(text(), \"" + productName + "\")]/../.." + BUTTON_ADD_TO_BAG)).click();
        waitForElementIsVisible(POP_UP_MINICART, 30);
        waitForElementNotVisible(POP_UP_MINICART, 30);
    }

    /**
     * Opens Product detail page of Random "In Stock" Product by clicking on link on Category landing page.
     * @return  String: Product Name(Extracted from CLP) of PDP that opened.
     */
    public String openRandomInStockPDP() {
        List<String> inStockProductNames = getListOfInStockProductNames();
        //make sure there are "In Stock" products
        assertTrue("THERE ARE NO IN STOCK PRODUCTS FOUND IN THE PRODUCTS GRID!", inStockProductNames.size() > 0);
        //pick random product and go to it's PDP
        Random randomGenerator = new Random();
        String productName;
        int productNumber;
        productNumber = randomGenerator.nextInt(inStockProductNames.size());
        productName = inStockProductNames.get(productNumber);
        logger.info("Chosen random product is: " + productName);
        openPdpByProductName(productName);
        return productName;
    }

    /**
     * finds random product, that has In stock status and returns it's name
     */
    public String getInStockRandomProductName() {
        List<String> inStockProductNames = getListOfInStockProductNames();
        //make sure there are "In Stock" products
        assertTrue("THERE ARE NO IN STOCK PRODUCTS FOUND IN THE PRODUCTS GRID!", inStockProductNames.size() > 0);
        //pick random product and go to it's PDP
        Random randomGenerator = new Random();
        String productName;
        int productNumber;
        productNumber = randomGenerator.nextInt(inStockProductNames.size());
        productName = inStockProductNames.get(productNumber);
        logger.info("Chosen random product is: " + productName);
        return productName;
    }

    /**
     * Adds random In Stock Product(extracted from Current CLP) to cart: hovers on it's Title and
     * clicks "Add to Bag" button with necessary waiting.
     */
    public void addRandomItemToCart() {
        addToCartByProductName(getInStockRandomProductName());
    }

    /**
     * Opens specified CLP and verifies that Julep logo is visible in header and it is not 404 page
     * Method also write into console time for open of page
     * @param linkToPage String URL of CLP
     */
    public void openFilteredCLPVerifyIt(String linkToPage) {
        long startTime;
        startTime = System.currentTimeMillis();
        driver.get(linkToPage);
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " PAGE=" +
                driver.getCurrentUrl() + " LOAD_TIME=" + Long.toString(System.currentTimeMillis() - startTime));
        assertTrue("JULEP LOGO IS NOT DISPLAYED IN HEADER", driver.findElement(Header.LOGO_JULEP).isDisplayed());
        assertFalse("404 PAGE IS OPENED!", is404Page());
    }

    public void scrollDownTillAllProductsLoad() {
        int total = getTotalProductsFromJS();
        int count = countProductsInGrid();
        WebElement justForScroll = driver.findElement(By.xpath("//h1[@class='logo']/a"));
        while (count != total){
            justForScroll.sendKeys(Keys.END); //scroll down
            justForScroll.sendKeys(Keys.END); //scroll down
            try {
                waitTillNextGroupOfProductsWillBeLoadedInGrid();
            }catch (Exception e) {
                break; //hack
            }
            count = countProductsInGrid();
        }
    }
}
