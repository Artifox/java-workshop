package com.example.teamcity.api;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.common.SuiteTags.REGRESSION;
import static io.qameta.allure.Allure.step;

@Tag(REGRESSION)
public class BuildTypeTest extends BaseApiTest {

    @Test
    @Tags({@Tag("Positive"), @Tag("CRUD")})
    @DisplayName("User should be able to create build type")
    void userCreatesBuildTypeTest() {
        var user = generate(User.class);

        superUserCheckedRequests.getRequest(USERS).create(user);
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(user));

        var project = generate(Project.class);

        project = userCheckRequests.<Project>getRequest(PROJECTS).create(project);

        var buildType = generate(Collections.singletonList(project), BuildType.class);

        userCheckRequests.getRequest(BUILD_TYPES).create(buildType);

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(buildType.getId());
            softy.assertThat(buildType.getName())
                    .as("Check buildType name. Expected: %s, but Actual: %s", buildType.getName(), createdBuildType.getName())
                    .isEqualTo(createdBuildType.getName());

    }

    @Test
    @Tags({@Tag("Negative"), @Tag("CRUD")})
    @DisplayName("User should not be able to create two build types with the same id")
    void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var user = generate(User.class);

        superUserCheckedRequests.getRequest(USERS).create(user);
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(user));

        var project = generate(Project.class);

        project = userCheckRequests.<Project>getRequest(PROJECTS).create(project);

        var buildType1 = generate(Collections.singletonList(project), BuildType.class);
        var buildType2 = generate(Collections.singletonList(project), BuildType.class, buildType1.getId());

        userCheckRequests.getRequest(BUILD_TYPES).create(buildType1);
        new UncheckedBase(Specifications.authSpec(user), BUILD_TYPES)
                .create(buildType2)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(
                        String.format(
                                "The build configuration / template ID \"%s\" is already used by another configuration or template",
                                buildType1.getId())
                ));
    }

    @Test
    @Tags({@Tag("Positive"), @Tag("Roles")})
    @DisplayName("Project admin should be able to create build type for their project")
    void projectAdminCreatesBuildTypeTest() {
        step("Create user");
        step("Create project");
        step("Grant user PROJECT_ADMIN role in project");

        step("Create buildType for project by user (PROJECT_ADMIN)");
        step("Check buildType was created successfully");
    }

    @Test
    @Tags({@Tag("Negative"), @Tag("Roles")})
    @DisplayName("Project admin should not be able to create build type for not their project")
    void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create user1");
        step("Create project1");
        step("Grant user1 PROJECT_ADMIN role in project1");

        step("Create user2");
        step("Create project2");
        step("Grant user2 PROJECT_ADMIN role in project2");

        step("Create buildType for project1 by user2");
        step("Check buildType was not created  with forbidden codeø");
    }

}
