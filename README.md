# Amazon Purchase Automation with Selenium

This project automates the process of searching for a mobile phone on [Amazon.in](https://www.amazon.in), applying filters, selecting a product, adding it to the cart, and navigating to the cart—all using Selenium WebDriver in Java.

## 📌 Features

- Opens Amazon.in in Chrome
- Searches for "mobile"
- Applies **4 Stars & Up** filter
- Adjusts **price range** using slider inputs
- Clicks the **first search result**
- Adds the product to the cart
- Navigates to the cart

## 🛠️ Technologies Used

- Java 17
- Selenium WebDriver 4
- ChromeDriver
- Maven/Gradle (optional)
- JDK & Chrome browser installed

## 📦 Setup Instructions

1. **Install Chrome browser** (if not already installed)
2. **Download ChromeDriver**
   - Download from [https://sites.google.com/a/chromium.org/chromedriver/](https://sites.google.com/a/chromium.org/chromedriver/)
   - Ensure the version matches your Chrome browser
   - Set the path in the Java file:
     ```java
     System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
     ```

3. **Add Selenium library to your project**
   - If using Maven, add the dependency:
     ```xml
     <dependency>
         <groupId>org.seleniumhq.selenium</groupId>
         <artifactId>selenium-java</artifactId>
         <version>4.23.0</version>
     </dependency>
     ```

4. **Run the `AmazonPurchaseAutomation` Java file**

## 📝 Notes

- Amazon UI may change over time. Always inspect updated element XPaths if the script fails.
- Sliders are used for setting price range. If they break, consider falling back to manual input fields.
- Amazon may block automation for suspicious activity. Use responsibly.

## 📸 Demo

*Coming soon*

## 🔐 Disclaimer

This script is for **educational purposes only**. Do not use it for actual purchases or unethical web scraping. Always respect the website's Terms of Service.

---

Happy Testing! 🚀
