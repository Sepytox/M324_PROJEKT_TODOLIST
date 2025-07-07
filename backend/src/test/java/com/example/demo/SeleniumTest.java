package com.example.demo;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumTest {
    
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
        driver.manage().window().maximize();

        /*
         * For automated testing while pushing, GUI is not enabled thus:
         * 
         *      ChromeOptions options = new ChromeOptions();
         *      options.addArguments("--headless=new");
         *
         */
    }


    @Test
    public void editButtonCanEdit() {
        driver.findElement(By.id("formtochange")).sendKeys("previous input");
        driver.findElement(By.className("Submission")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement ul = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("ul")));

        driver.findElement(By.id("Edit")).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.className("EditorialSlave")).clear();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.className("EditorialSlave")).sendKeys("Edit works fine");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("Save")).click();

        boolean editWorks = ul.findElements(By.tagName("li")).stream()
        .anyMatch(li -> li.getText().contains("Edit works fine"));
        //Proof that it works
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(editWorks, "Edit Button Failed");
    }

    @AfterEach
    public void shutDown() {
        driver.findElement(By.id("DoneEdit")).click();
        if(driver != null) {
            driver.quit();
        }
    }
    
}
