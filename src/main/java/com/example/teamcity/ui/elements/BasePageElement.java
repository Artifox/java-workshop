package com.example.teamcity.ui.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public abstract class BasePageElement {

    private final SelenideElement element;

    public BasePageElement(SelenideElement element) {
        this.element = element;
    }

    protected SelenideElement find(By selector) {
        return element.$(selector);
    }

    protected SelenideElement find(String cssSelector) {
        return element.$(cssSelector);
    }

    protected ElementsCollection finaAll(By selector) {
        return element.$$(selector);
    }

    protected ElementsCollection finaAll(String cssSelector) {
        return element.$$(cssSelector);
    }

}
