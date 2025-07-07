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
    private void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http//localhost:3000");
        driver.manage().window().maximize();
    }


    @Test
    private void editButtonCanEdit() {
        driver.findElement(By.id("formtochange")).sendKeys("previous input");
        driver.findElement(By.className("Submission")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement ul = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("ul")));

        driver.findElement(By.id("Edit")).click();
        driver.findElement(By.className("EditorialSlave")).clear();
        driver.findElement(By.className("EditorialSlave")).sendKeys("Edit works fine");
        driver.findElement(By.id("Save")).click();

        boolean editWorks = ul.findElements(By.tagName("li")).stream()
        .anyMatch(li -> li.getText().contains("Edit works fine"));

        assertTrue(editWorks, "Edit Button Failed");
    }

    @AfterEach
    private void shutDown() {
        if(driver != null) {
            driver.quit();
        }
    }
    
}
