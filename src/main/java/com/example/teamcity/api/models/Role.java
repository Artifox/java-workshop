package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Parameterizable;
import com.example.teamcity.api.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseModel {

    @Builder.Default
    @Parameterizable
    private UserRoles roleId = UserRoles.SYSTEM_ADMIN;
    @Builder.Default
    @Parameterizable
    private String scope = "g";

}
