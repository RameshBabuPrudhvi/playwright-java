package io.github.selcukes.playwright;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiTest {

    private static final String BASE_URL = "https://postman-echo.com";
    private APIRequestContext request;
    private Playwright playwright;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();


    }

    @Test
    public void requestTest() {
        var headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        this.request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(BASE_URL)
                .setExtraHTTPHeaders(headers));

        String json = "{ \"name\":\"Ramesh\", \"notes\":\"hello\" }";

        var options = RequestOptions.create().setData(json);

        var response = request.post("/post", options);

        assertThat(response).isOK();
        String responseBody = response.text();
        System.out.println("API Response: " + responseBody);

        assertTrue(responseBody.contains("Ramesh"));
        assertTrue(responseBody.contains("hello"));
    }

    @Test
    public void authTest() {
        String auth = "postman:password";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders.put("Content-Type", "application/json");
        authHeaders.put("Authorization", "Basic " + encodedAuth);
        APIRequestContext authContext = playwright.request().newContext((
                new APIRequest.NewContextOptions()
                        .setBaseURL(BASE_URL)
                        .setExtraHTTPHeaders(authHeaders)));

        var response = authContext.get("/basic-auth");
        assertThat(response).isOK();
        String responseBody = response.text();
        System.out.println("Auth API Response: " + responseBody);
    }
}
