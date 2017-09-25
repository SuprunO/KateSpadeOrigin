Speroteck test automation for Magento and Demandware
====================================================

Features:
--------
Coming soon...


Features requirements:
-------
* **Web service:** with access form web (run test(s) from web);
* **Web service:** Структурирование/Хрениение/редактирование/ доступ к данным проектов.
* **Testing** - run test project/test case/ group of tests; get test results;
* **API** - run test project/test case/ group of tests; get test results;
* **Page/Tests Constructor:** конструктор тестов/набора тестов с группровкой по проекту
* **Save/Load pages locators:** принимать локатроры элементов страниц из файла
* **Save/Load tests:** принимать набор тестов из файла
* **Templates for Magento/Demandware:** обладать шаблонами тестов для деволтной мадженты и Демандвары
* **Test results:** отображать результаты тестов, создание развернутых отчетов.
* **Integration with CI:** интегрироваться с TeamCity/Jenkins и др. CI


Install
-------
* Java SDK
* Chromedriver from link: https://sites.google.com/a/chromium.org/chromedriver/downloads
* Gradle from link http://gradle.org/gradle-download/


FAQ:
---
* Add GRADLE_HOME/bin to your PATH environment variable.
* Note that it's not currently possible to set JVM options for Gradle on the command line.
* **Run from IDE:** JUnit runner, set "VM Options": -ea -DbaseUrl=http://lea-mage12.lcgosc.com/ -Dbrowser=CHROME Then right click on test class/method >> run or debug
* **SauceLabs: authentication** to run tests in SauceLabs service using REST API: 
    * add DSAUCE_USER_NAME=<user> and DSAUCE_API_KEY=<api_key> to command line with appropriate values
    *  or create a ~/.sauce-ondemand properties-file with "username" and "key" properties
* **Available parameters:**
    * -Dbrowser specifies the browser that will be lunched to execute tests.  
        Values:  chrome(default) | firefox | ie | htmlunit
        Note: -DenableJS should be addaed to enable JS for HTML Unit Driver
    * -DbaseUrl  specifies target site for testing ex.: http://mysite.com 
    * -DsslEnabled  specifies usage of "https://" for current site
    yes  (if omitted or any other value is specified - "no" will be applied) 
    * -DlogLevel  specifies the level of logging and the amount of messages that will appear in console
     Values:  info(default) | error | warn (Case Insensetive) 
    * -DsauceLabsSession    Runs test in SauceLabs remote browser.
    Syntax is the following: "OS\*version*browser" 
    Please note: all whitespaces are collapsed ex: OS X => OSX  
    see details on: [wiki.saucelabs.com](https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options) also [configurator to get desired version](https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/) 

