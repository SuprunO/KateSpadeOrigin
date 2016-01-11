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



