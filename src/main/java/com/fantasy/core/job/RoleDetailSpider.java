package com.fantasy.core.job;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by panwang.chengpw on 2018/4/8.
 */
@Component
public class RoleDetailSpider {

    private static final Logger logger = LoggerFactory.getLogger(RoleDetailSpider.class);
    private WebDriver           webDriver;

    public RoleDetailSpider() {

        String driver = System.getProperty("webdriver.chrome.driver");
        //        String driver = System.getProperty("webdriver.firefox.bin");
        if (driver == null) {
            logger.info("没有设置 driver 变量");
            //            System.getProperties().setProperty("webdriver.firefox.bin", "/Users/chengpanwang/Downloads/geckodriver");
            System.getProperties().setProperty("webdriver.chrome.driver", "/Users/chengpanwang/Downloads/chromedriver");
        } else {
            logger.info("driver: {}", driver);
        }

    }

    public BigDecimal skillPrice(String url) {
        logger.info("角色详情页: {}", url);

        BigDecimal price = BigDecimal.ZERO;

        try {

            ChromeOptions options = new ChromeOptions();

            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");

            webDriver = new ChromeDriver(options);

            webDriver.get(url);
            //            Thread.sleep(5000);

            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            WebElement roleSkill = webElement.findElement(By.id("role_skill"));
            logger.info(roleSkill.getText());
            logger.info("选中技术标签");
            roleSkill.click();

            WebElement skillTb = webElement.findElement(By.className("skillTb"));

            for (WebElement item : skillTb.findElements(By.tagName("td"))) {
                String level = item.findElement(By.tagName("p")).getText();
                String h5 = item.findElement(By.tagName("h5")).getText();
                if (StringUtils.equals(h5, "强壮") && StringUtils.equals(level, "40")) {
                    price = price.add(new BigDecimal(700));
                    logger.info("强壮达到40，加700 ： {}", price);
                }
                if (StringUtils.equals(h5, "神速") && StringUtils.equals(level, "40")) {
                    price = price.add(new BigDecimal(700));
                    logger.info("神速达到40，加700 ： {}", price);
                }
            }

            webDriver.close();
        } catch (Exception e) {
            logger.error("", e);
        }

        return price;
    }

    public static void main(String[] args) {
        new RoleDetailSpider().skillPrice("http://xyq.cbg.163.com/equip?s=159&eid=201712242300113-159-M7HW5YLC9VBM&o&equip_refer=1");
    }
}
