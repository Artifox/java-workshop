package com.example.teamcity;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;

public class BaseTest {

    protected CheckedRequests superUserCheckedRequests = new CheckedRequests(Specifications.superUserSpec());
    protected TestData testData;

    protected SoftAssertions softy;

    @BeforeEach
    public void beforeTest() {
        softy = new SoftAssertions();
        testData = generate();
    }

    @AfterEach
    public void afterTest() {
        softy.assertAll();
        TestDataStorage.getStorage().deleteCreatedEntities();
    }
}
