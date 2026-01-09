package com.monolux.resource.dto.res;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"userPwdChanged"})
public record UserPwdChangedResInfo(String userPwdChanged) {

}