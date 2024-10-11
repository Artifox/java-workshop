package com.example.teamcity.api;

import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected CheckedRequests superUserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());

    protected SoftAssertions softy;

    @BeforeEach
    public void beforeTest() {
        softy = new SoftAssertions();
    }

    @AfterEach
    public void afterTest() {
        softy.assertAll();
    }
}
