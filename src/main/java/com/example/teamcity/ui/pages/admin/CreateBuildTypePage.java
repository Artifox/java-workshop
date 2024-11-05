package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Condition.visible;

public class CreateBuildTypePage extends CreateBasePage {

    public static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";

    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(String.format(CREATE_URL, projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createBuildTypeWithRepoUrl(String repoUrl) {
        baseCreateForm(repoUrl);

        return this;
    }

    public CreateBuildTypePage createBuildTypeWithRepoUrlWithoutValidation(String repoUrl) {
        baseCreateFormWithoutValidation(repoUrl);

        return this;
    }

    public void setupBuildConfiguration(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
    }

}
