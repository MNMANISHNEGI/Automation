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
import java.util.List;

public class AmazonPurchaseAutomation {
    public static void main(String[] args) throws InterruptedException {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\91789\\Desktop\\UPES\\java\\chromedriver.exe");

        // Create ChromeOptions instance
        ChromeOptions options = new ChromeOptions();

        // Create a new instance of the ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        // Navigate to Amazon
        driver.get("https://www.amazon.in/");
        driver.manage().window().maximize();

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

        // Apply the "4 Stars & Up" filter.  Handle StaleElementReferenceException.
        boolean filterApplied = false;
        while (!filterApplied) {
            try {
                WebElement fourStarsFilter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='reviewsRefinements']//i[@class='a-icon a-icon-star-medium a-star-medium-4']")));
                fourStarsFilter.click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//i[@class='a-icon a-icon-star-medium a-star-medium-4']")));
                System.out.println("Applied 4 stars filter.");
                filterApplied = true;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException occurred while applying the filter. Retrying...");
                driver.navigate().refresh();
            }
        }

        // Enter price range manually
        WebElement minSlider = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("p_36/range-slider_slider-item_lower-bound-slider")));
        WebElement maxSlider = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("p_36/range-slider_slider-item_upper-bound-slider")));
        WebElement goButton = driver.findElement(By.cssSelector("span.a-button-inner > input[type='submit']")); // Corrected selector for the Go button.

        // Scroll sliders into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", minSlider);
        Thread.sleep(1000);

        // Use JavaScript to change slider values and click the Go button.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            // Set slider values directly using JavaScript
            js.executeScript("arguments[0].value = 59; arguments[0].dispatchEvent(new Event('input'));", minSlider);
            js.executeScript("arguments[0].value = 78; arguments[0].dispatchEvent(new Event('input'));", maxSlider);

            // Wait for a short period (e.g., 500ms) to allow the UI to update.
            Thread.sleep(500);

            // Click the "Go" button to apply the filter
            goButton.click();
            // Wait for the search results to be present and visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.s-main-slot")));
            System.out.println("Price range sliders adjusted and applied.");

        } catch (Exception e) {
            System.err.println("Error adjusting price sliders or applying filter: " + e.getMessage());
        }


        // Wait for filtered products to update.
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.s-main-slot")));

        // Find all search results
        List<WebElement> searchResults = driver.findElements(By.xpath("//div[@data-asin]"));
        if (searchResults.size() > 0) {
            // Click on the first search result
            WebElement firstSearchResult = driver.findElement(By.xpath("//div[@class='a-section']//a[@class='a-link-normal']"));
            String href = firstSearchResult.getAttribute("href");
            System.out.println("href: " + href);
            firstSearchResult.click();
            System.out.println("Clicked on the first search result.");
        } else {
            System.out.println("No search results found");
            driver.quit();
            return;
        }

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
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        System.out.println("Scrolled down.");

        // Locate and click on the "Add to Cart" button
        WebElement addToCartButton = null;
        try {
            // Increased wait time to 30 seconds
            addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println("Add to Cart button not clickable after 30 seconds.  Trying a different strategy.");
            // Try finding the button by another attribute, e.g., name or class
            List<WebElement> addCartButtons = driver.findElements(By.cssSelector("input[name='submit.add-to-cart']")); // Example
            if (addCartButtons.size() > 0) {
                addToCartButton = addCartButtons.get(0); // Take the first one
            }
            if (addToCartButton == null)
            {
                List<WebElement> addCartButtons2 = driver.findElements(By.cssSelector("#addToCart input[type='submit']"));
                if (addCartButtons2.size() > 0) {
                    addToCartButton = addCartButtons2.get(0);
                }
            }

        }

        if (addToCartButton != null) {
            addToCartButton.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attach-sidesheet-view-cart-button")));
            System.out.println("Added phone to cart.");
        } else {
            System.err.println("Failed to find Add to Cart button.");
            driver.quit();
            return; // Exit the program
        }

        // Click on the "Go to Cart" button
        WebElement goToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("attach-sidesheet-view-cart-button")));
        goToCartButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeCartViewForm")));
        System.out.println("Navigated to the cart.");

        // Quit the driver
        driver.quit();
    }
}
