package com.example.teamcity.api.models;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Steps extends BaseModel {

    private Integer count;
    private List<Step> step;

}
