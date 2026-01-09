package com.monolux.resource.dto.res;

import lombok.Builder;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"userId", "userName", "userNick"})
@Builder
public record UserResInfo(String userId, String userName, String userNick) {

}