package com.example.teamcity.api.models;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Step extends BaseModel {

    private String id;
    private String name;
    @Builder.Default
    private String type = "simpleRunner";

}
