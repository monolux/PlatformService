package com.monolux.resource.dto.req;

import com.monolux.domain.enumerations.Gender;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"userId", "userPwd", "userName", "userNick", "userProfileImg",
        "mobileNo", "mail", "yearOfBirth", "monthOfBirth", "dayOfBirth",
        "gender", "isForeigner"})
public record UserSignUpReqInfo(String userId, String userPwd, String userName, String userNick, String userProfileImg,
                                String mobileNo, String mail, Integer yearOfBirth, Byte monthOfBirth, Byte dayOfBirth,
                                Gender gender, Boolean isForeigner) {

}