package com.example.teamcity.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static com.example.teamcity.common.SuiteTags.REGRESSION;
import static io.qameta.allure.Allure.step;

@Tag(REGRESSION)
public class BuildTypeTest extends BaseApiTest {

    @Test
    @Tags({@Tag("Positive"), @Tag("CRUD")})
    @DisplayName("User should be able to create build type")
    void userCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Create build type for project");
        step("Check buildType was created successfully with correct data");
    }

    @Test
    @Tags({@Tag("Negative"), @Tag("CRUD")})
    @DisplayName("User should not be able to create two build types with the same id")
    void userCreatesTwoBuildTypesWithTheSameIdTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType1 for project by user");
        step("Check buildType2 with same id as buildType1 for project by user");
        step("Check buildType2 was not created with bad request code");
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
