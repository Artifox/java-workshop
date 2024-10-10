package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

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

        step("Create user", () -> {
            var requester = new CheckedBase<User>(Specifications.superUserSpec(), Endpoint.USERS);
            requester.create(user);
        });

        var project = generate(Project.class);
        AtomicReference<String> projectId = new AtomicReference<>("");

        step("Create project by user", () -> {
            var requester = new CheckedBase<Project>(Specifications.authSpec(user), Endpoint.PROJECTS);
            projectId.set(requester.create(project).getId());
        });

        var buildType = generate(BuildType.class);
        buildType.setProject(Project.builder().id(projectId.get()).locator(null).build());

        var requester = new CheckedBase<BuildType>(Specifications.authSpec(user), Endpoint.BUILD_TYPES);
        AtomicReference<String> buildTypeId = new AtomicReference<>("");

        step("Create build type for project by user", () -> {
            buildTypeId.set(requester.create(buildType).getId());
        });
        step("Check buildType was created successfully with correct data", () -> {
            var createdBuildType = requester.read(buildTypeId.get());
            softy.assertThat(buildType.getName())
                    .as("Check buildType name. Expected: %s, but Actual: %s", buildType.getName(), createdBuildType.getName())
                    .isEqualTo(createdBuildType.getName());
        });
    }

    @Test
    @Tags({@Tag("Negative"), @Tag("CRUD")})
    @DisplayName("User should not be able to create two build types with the same id")
    void userCreatesTwoBuildTypesWithTheSameIdTest() {
        step("Create user", () -> {

        });
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
