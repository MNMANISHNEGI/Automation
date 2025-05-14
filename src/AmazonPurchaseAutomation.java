//Chrome browser
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Set;
import java.time.Duration;

public class AmazonPurchaseAutomation {
    public static void main(String[] args) throws InterruptedException {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\91789\\Desktop\\UPES\\java\\chromedriver.exe");

        // Create ChromeOptions instance for incognito mode
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--incognito");

        // Create a new instance of the ChromeDriver with incognito mode
        WebDriver driver = new ChromeDriver(options);

        // Navigate to Amazon
        driver.get("https://www.amazon.in/");

        // Check if landed on the correct page
        if (driver.getTitle().contains("Amazon")) {
            System.out.println("Landed on the correct page.");
        } else {
            System.out.println("Did not land on the expected page.");
        }

        // Print the URL and Title of the page
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());

        // Locate the search box and search for "mobile"
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("mobile");
        searchBox.submit();

        // Create WebDriverWait instance with a timeout of 20 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Apply the "4 Stars & Up" filter
        WebElement fourStarsFilter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='reviewsRefinements']//i[@class='a-icon a-icon-star-medium a-star-medium-4']")));
        fourStarsFilter.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//i[@class='a-icon a-icon-star-medium a-star-medium-4']")));
        System.out.println("Applied 4 stars filter.");

        // Enter price range manually
        // Wait until the slider elements are present
        WebElement minSlider = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("p_36/range-slider_slider-item_lower-bound-slider")));
        WebElement maxSlider = driver.findElement(
                By.id("p_36/range-slider_slider-item_upper-bound-slider"));

// Scroll sliders into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", minSlider);
        Thread.sleep(1000); // Optional: let it settle

// Use JavaScript to change slider values
        JavascriptExecutor js = (JavascriptExecutor) driver;

// Set minimum price (e.g., 40 out of 189)
        js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
                minSlider, 40);

// Set maximum price (e.g., 120 out of 189)
        js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
                maxSlider, 120);

        Thread.sleep(2000); // Wait for results to refresh
        System.out.println("Price range sliders adjusted.");


        // Optional: Wait for filtered products to update, e.g., wait for a product container to reappear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.s-main-slot")));
        System.out.println("Price filter applied using slider.");
        // crude check



        // Click on the first search result
        WebElement firstSearchResult = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"search\"]/div[1]/div[1]/div/span[1]/div[1]/div[3]/div/div/div/div/span/div/div/div/div[2]/div/div/div[1]/h2/a/span")));
        firstSearchResult.click();
        System.out.println("Clicked on the first search result.");

        // Store the original window handle
        String originalWindowHandle = driver.getWindowHandle();

        // Switch to the new tab
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(originalWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Scroll down to make the "Add to Cart" button visible
        int scrollPixels = 500; // Adjust this value based on how much you want to scroll
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + scrollPixels + ");");
        System.out.println("Scrolled down.");

        // Locate and click on the "Add to Cart" button
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
        addToCartButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attach-sidesheet-view-cart-button")));
        System.out.println("Added phone to cart.");

        // Click on the "Go to Cart" button
        WebElement goToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("attach-sidesheet-view-cart-button")));
        goToCartButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeCartViewForm")));
        System.out.println("Navigated to the cart.");

        // Quit the driver, closing all associated windows
        driver.quit();
    }
}

