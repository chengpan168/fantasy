package com.fantasy.core.spider;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Stopwatch;

@Service
public class ZaoSEOSpider {

    private static final Logger logger = LoggerFactory.getLogger(ZaoSEOSpider.class);

    int                         maxId  = 4016;

    private void runTask() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        for (;;) {
            System.getProperties().setProperty("webdriver.chrome.driver", "/Users/chengpanwang/Downloads/chromedriver");
            WebDriver webDriver = new ChromeDriver();

            webDriver.get("https://www.3dzao.cn/articles/detail/0.html");
            //            Thread.sleep(5000);
            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            System.out.println(webElement.getAttribute("outerHTML"));
            webDriver.close();
        }

    }

}
