package io.github.selcukes.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DemoTest extends TestFixtures {
    @Test
    public void testSuccessfulLogin() {
        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
       // page.pause();
        page.locator("input[name='username']").fill("Admin");
        page.fill("input[name='password']", "admin123", new Page.FillOptions().setTimeout(30000));
        //page.locator("input[name='password']").fill("admin123");
        page.locator("button[type='submit']").click();

        assertThat(page).hasURL("https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index");
    }

    @Test
    public void eComTest() {
        page.navigate("https://ecommerce-playground.lambdatest.io/index.php?route=common/home");
        page.pause();
        var myAccount = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My account"));
        myAccount.click();

        page.getByPlaceholder("E-Mail Address").fill("rprudhvi.office@gmail.com");
        page.getByPlaceholder("Password").fill("Pass123$");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Edit your account information")).click();
        page.getByPlaceholder("Last Name").fill("Babu");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        var successMessage = page.getByText("Success: Your account has been successfully updated.");
        assertThat(successMessage).isVisible();

        myAccount.hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Logout").setExact(true)).click();
        var logoutHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Account Logout"));
        assertThat(logoutHeader).isVisible();
    }
}
