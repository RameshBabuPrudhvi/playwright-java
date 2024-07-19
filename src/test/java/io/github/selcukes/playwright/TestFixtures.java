package io.github.selcukes.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestFixtures {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright
                .chromium()
                .launch(new BrowserType
                        .LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(50));
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser
                .newContext(new Browser
                        .NewContextOptions()
                        .setRecordVideoDir(Paths.get("target/videos/")));

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("target/screenshot/" + UUID.randomUUID() + ".png"))
                .setFullPage(true));
        context.tracing()
                .stop(new Tracing
                        .StopOptions()
                        .setPath(Paths.get("trace.zip")));
        context.close();
    }

    @AfterAll
    void closeBrowser() {
        playwright.close();
    }
}