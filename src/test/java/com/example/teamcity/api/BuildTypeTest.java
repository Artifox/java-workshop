package com.example.teamcity.api;

import com.example.teamcity.api.enums.UserRoles;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.common.SuiteTags.REGRESSION;

@Tag(REGRESSION)
public class BuildTypeTest extends BaseApiTest {

    @Test
    @Tags({@Tag("Positive"), @Tag("CRUD")})
    @DisplayName("User should be able to create build type")
    void userCreatesBuildTypeTest() {
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
            softy.assertThat(testData.getBuildType().getName())
                    .as("Check buildType name. Expected: %s, but Actual: %s", testData.getBuildType().getName(), createdBuildType.getName())
                    .isEqualTo(createdBuildType.getName());

    }

    @Test
    @Tags({@Tag("Negative"), @Tag("CRUD")})
    @DisplayName("User should not be able to create two build types with the same id")
    void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var buildTypeWithSameId = generate(Collections.singletonList(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(
                        String.format(
                                "The build configuration / template ID \"%s\" is already used by another configuration or template",
                                testData.getBuildType().getId())
                ));
    }

    @Test
    @Tags({@Tag("Positive"), @Tag("Roles")})
    @DisplayName("Project admin should be able to create build type for their project")
    void projectAdminCreatesBuildTypeTest() {
        var project = superUserCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        //дай, пожалуйста, ссылку на документацию, где показано, что нужно именно так нужно указывать скоуп, чтобы заасайнить юзера на проект
        //я взял из чата, сам не нашел
        testData.getUser().setRoles(generate(Roles.class, UserRoles.PROJECT_ADMIN, "p:" + project.getId()));
        var user = superUserCheckedRequests.<User>getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(user));
        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).create(testData.getBuildType());
        softy.assertThat(testData.getBuildType().getName())
                .as("Check buildType name. Expected: %s, but Actual: %s", testData.getBuildType().getName(), createdBuildType.getName())
                .isEqualTo(createdBuildType.getName());
    }

    @Test
    @Tags({@Tag("Negative"), @Tag("Roles")})
    @DisplayName("Project admin should not be able to create build type for not their project")
    void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var project1 = superUserCheckedRequests.<Project>getRequest(PROJECTS).create(generate(Project.class));
        var project2 = superUserCheckedRequests.<Project>getRequest(PROJECTS).create(generate(Project.class));

        var user1 = generate(User.class);
        user1.setRoles(generate(Roles.class, UserRoles.PROJECT_ADMIN, "p:" + project1.getId()));
        var user2 = generate(User.class);
        user2.setRoles(generate(Roles.class, UserRoles.PROJECT_ADMIN, "p:" + project2.getId()));
        superUserCheckedRequests.<User>getRequest(USERS).create(user1);
        superUserCheckedRequests.<User>getRequest(USERS).create(user2);

        testData.getBuildType().setProject(project1);
        new UncheckedBase(Specifications.authSpec(user2), BUILD_TYPES)
                .create(testData.getBuildType())
                .then().assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);

    }
}
