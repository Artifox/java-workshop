package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProjectPage extends BasePage {

    private static final String PROJECT_URL = "/project/%s";

    public SelenideElement title = $("span[class*='ProjectPageHeader']");
    public SelenideElement title1 = $x("//*[contains(@class, 'ProjectPageHeader__title')]");

    public static ProjectPage open(String projectId) {
        return Selenide.open(String.format(PROJECT_URL, projectId), ProjectPage.class);
    }
}
