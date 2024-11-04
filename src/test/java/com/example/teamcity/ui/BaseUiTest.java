package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.pages.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

public class BaseUiTest extends BaseTest {

    protected static final String REPO_URL = "https://github.com/AlexPshe/spring-core-for-qa";

    @BeforeAll
    public static void setupUiTest() {
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        Configuration.browserSize = Config.getProperty("browserSize");

        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true,
                        "enableLog", true));
    }

    @AfterAll
    public static void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    protected void loginAs(User user) {
        LoginPage.open().login(user);
    }

}
