package com.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBuildTypeTest extends BaseUiTest {

    Project createdProject;
    CheckedRequests userCheckRequests;

    @BeforeEach
    void setup() {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        createdProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
    }

    @Test
    @Tag("Positive")
    @Tag("Regression")
    @DisplayName("User should be able to create build type for project")
    void createBuildTypeForProject() {
        loginAs(testData.getUser());
        var createdProject = superUserCheckedRequests
                .<Project>getRequest(Endpoint.PROJECTS)
                .read("name:" + testData.getProject().getName());
        CreateBuildTypePage
                .open(createdProject.getId())
                .createBuildTypeWithRepoUrl(REPO_URL)
                .setupBuildConfiguration(testData.getBuildType().getName());

        //проверка через API
        var createdBuildType = superUserCheckedRequests
                .<BuildType>getRequest(BUILD_TYPES)
                .read("name:" + testData.getBuildType().getName());
        softy.assertThat(createdBuildType).isNotNull();

        //проверка на UI
        var foundBuildType = ProjectPage.open(createdProject.getId())
                .getBuildTypes().stream()
                .anyMatch(buildType -> buildType.getName().text().equals(testData.getBuildType().getName()));
        softy.assertThat(foundBuildType).isTrue();
    }

    @Test
    @Tag("Negative")
    @Tag("Regression")
    @DisplayName("User should not be able to create build type with invalid repository url")
    void createBuildTypeForProjectWithInvalidRepoUrl() {
        var buildTypeCountBefore = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).readAll().size();

        loginAs(testData.getUser());

        var createBuildTypePage = CreateBuildTypePage
                .open(createdProject.getId());
        createBuildTypePage
                .createBuildTypeWithRepoUrlWithoutValidation("random_text_instead_url");
        createBuildTypePage
                .getUrlNotRecognizedMessage()
                .shouldHave(exactText("Cannot create a project using the specified URL. The URL is not recognized."));
        var buildTypeCountAfter = userCheckRequests.getRequest(BUILD_TYPES).readAll().size();

        softy.assertThat(buildTypeCountBefore).as("Number of build types wasn't changed")
                .isEqualTo(buildTypeCountAfter);
    }

}
