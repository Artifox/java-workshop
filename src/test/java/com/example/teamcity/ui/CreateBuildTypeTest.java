package com.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;

public class CreateBuildTypeTest extends BaseUiTest {

    @BeforeEach
    void setup() {
        superUserCheckedRequests.getRequest(Endpoint.USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
    }

    @Test
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
                .<BuildType>getRequest(Endpoint.BUILD_TYPES)
                .read("name:" + testData.getBuildType().getName());
        softy.assertThat(createdBuildType).isNotNull();

        //проверка на UI
        var foundBuildType = ProjectPage.open(createdProject.getId())
                .getBuildTypes().stream()
                .anyMatch(buildType -> buildType.getName().text().equals(testData.getBuildType().getName()));
        softy.assertThat(foundBuildType).isTrue();
    }

}
