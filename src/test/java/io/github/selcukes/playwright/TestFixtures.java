package io.github.selcukes.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestFixtures {
    // Shared between all tests in the class.
    Playwright playwright;
    Browser browser;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    @AfterAll
    void closeBrowser() {
        playwright.close();
    }

    // New instance for each test method.
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {
        context =  browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")));

        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("target/screenshot" + UUID.randomUUID() + ".png"))
                .setFullPage(true));
        context.close();
    }
}