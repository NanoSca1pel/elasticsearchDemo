package com.lht.taobao;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

/**
 * @author lhtao
 * @descript
 * @date 2021/5/17 17:00
 */
public class TestChromeDriver {
    private static ChromeDriverService service;

    public static WebDriver getChromeDriver() throws IOException {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
        // 创建一个 ChromeDriver 的接口，用于连接 Chrome（chromedriver.exe 的路径可以任意放置，只要在newFile（）的时候写入你放的路径即可）
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\chrome\\chromedriver.exe")).usingAnyFreePort().build();
        service.start();
        // 创建一个 Chrome 的浏览器实例
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        //去除“chrome正受到自动测试软件的控制”的提示
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        //使用谷歌浏览器并启用静默模式
        //options.addArguments("--headless");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return new RemoteWebDriver(service.getUrl(), desiredCapabilities);
    }

    public static void main(String[] args) throws IOException {

        WebDriver driver = TestChromeDriver.getChromeDriver();
        // 让浏览器访问 Baidu
        driver.get("https://www.taobao.com/");
        // 用下面代码也可以实现
        //driver.navigate().to("https://www.taobao.com/");
        // 获取 网页的 title
        System.out.println(" Page title is: " + driver.getTitle());
        // 通过 className 找到 input 的 DOM
        WebElement element = driver.findElement(By.id("q"));
        // 输入关键字
        element.sendKeys("东鹏瓷砖");
        // 提交 input 所在的 form
        element.submit();
        // 通过判断 title 内容等待搜索页面加载完毕，间隔秒
        new WebDriverWait(driver, 1).until(new ExpectedCondition() {
            @Override
            public Object apply(Object input) {
                WebDriver wd = (WebDriver) input;
                System.out.println("===============" + wd.getTitle() + "===============");
                return wd.getTitle().toLowerCase().startsWith("东鹏瓷砖");
            }
        });
        // 显示搜索结果页面的 title
        System.out.println(" Page title is: " + driver.getTitle());
        // 关闭浏览器
        driver.quit();
        // 关闭 ChromeDriver 接口
        service.stop();
    }
}
