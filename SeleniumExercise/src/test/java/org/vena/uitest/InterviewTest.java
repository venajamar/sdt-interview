package org.vena.uitest;

import applicationpages.manager.CreateModalPage;
import applicationpages.manager.ManagerPage;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class InterviewTest {
	private WebDriver driver;

	private static ManagerPage managerPage;

	@BeforeClass
	public void setUp() {
		String DRIVER_PATH = "./src/main/resources/drivers/";

		//Check OS and pick correct driver, default is windows
		String OS = System.getProperty("os.name");
		if (OS.contains("Mac")) {
			DRIVER_PATH += "mac/chromedriver";
		} else if (OS.contains("Linux")) {
			DRIVER_PATH += "linux/chromedriver";
		} else {
			DRIVER_PATH += "windows/chromedriver.exe";
		}

		System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("window-size=1920,1080");

		driver = new ChromeDriver(options);
		driver.manage().window().setSize(new Dimension(1920, 1080));
	}

	@Test(priority = 1)
	public void testLogin() {
		driver.get("https://dev.vena.io");
		driver.findElement(By.id("email")).sendKeys("");
		driver.findElement(By.id("password")).sendKeys("");
		driver.findElement(By.cssSelector("[data-testid*='login-pw']")).click();
		driver.manage().timeouts().implicitlyWait(30L, TimeUnit.MILLISECONDS);
		assertNotNull(driver.findElement(By.cssSelector("[name='new_manager']")));
		String userName = driver.findElement(By.id("accountTab")).getText();
		assertEquals(userName, "Admin User");
	}

	@Test(priority = 2)
	public void testCreateFolder() {
		managerPage = new ManagerPage(driver);
		managerPage.clickCreateButton();
		CreateModalPage createModalPage = managerPage.selectFolderOption();
		createModalPage.setName("Test New Folder");
		managerPage = createModalPage.clickCreateButton();
		assertTrue(managerPage.getFoldersList().contains("Test New Folder"));
	}

	@Test(priority = 3)
	public void testCreateProcessUnderFolder() throws InterruptedException {
		managerPage = new ManagerPage(driver);
		managerPage.clickCreateButton();
		managerPage.selectFolderOption();
		WebElement nameTextBox = driver.findElement(By.cssSelector("[data-testid='inputDialog-TextField'] div input"));
		nameTextBox.sendKeys("Test New Folder 1");
		WebElement createButtonOnModal = driver.findElement(By.cssSelector("[data-testid='submit-Create']"));
		createButtonOnModal.click();
		Thread.sleep(5000);
		managerPage.clickFolder("Test New Folder 1");
		managerPage.clickCreateButton();
		managerPage.selectProcessOption();
		nameTextBox.sendKeys("Test New Process 1");
		createButtonOnModal.click();
	}

	@Test(priority = 4)
	public void testCreateButtonDisabled() {
		managerPage = new ManagerPage(driver);
		managerPage.clickCreateButton();
		CreateModalPage createModalPage = managerPage.selectFolderOption();
		assertTrue(createModalPage.isCreateButtonEnabled());
	}

	@Test(priority = 5)
	public void testClickProcess() {
		managerPage = new ManagerPage(driver);
		managerPage.clickProcess("Test New Process 1");
	}

	@AfterClass (alwaysRun = true)
	public void teardown() {
		driver.close();
	}
}