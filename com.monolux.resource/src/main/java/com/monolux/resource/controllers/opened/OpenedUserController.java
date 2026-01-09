package com.monolux.resource.controllers.opened;

import com.monolux.common.web.response.ApiResponse;
import com.monolux.domain.services.UserService;
import com.monolux.resource.dto.req.UserSignUpReqInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Api(tags = "User API")
@RestController
@RequestMapping(value = OpenedUserController.BASE_PATH)
public class OpenedUserController extends BaseOpenedController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    protected static final String BASE_PATH = BaseOpenedController.OPENED_API_PREFIX + "/users";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    OpenedUserController(final UserService userService, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // endregion

    // region ▒▒▒▒▒ Post Mapping ▒▒▒▒▒

    @ApiOperation(value = "SignUp User")
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ApiResponse> createUser(@RequestHeader final HttpHeaders reqHeaders,
                                                  @RequestBody @Valid final UserSignUpReqInfo userSignUpReqInfo) {
        return Optional.ofNullable(this.userService.createUser(userSignUpReqInfo.userId(),
                        this.passwordEncoder.encode(userSignUpReqInfo.userPwd()),
                        userSignUpReqInfo.userName(),
                        userSignUpReqInfo.userNick(),
                        userSignUpReqInfo.userProfileImg(),
                        userSignUpReqInfo.mobileNo(),
                        userSignUpReqInfo.mail(),
                        userSignUpReqInfo.yearOfBirth(),
                        userSignUpReqInfo.monthOfBirth(),
                        userSignUpReqInfo.dayOfBirth(),
                        userSignUpReqInfo.gender(),
                        userSignUpReqInfo.isForeigner(),
                        this.getClientIp()))
                .map(user -> ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.CREATED, userSignUpReqInfo)))
                .orElse(ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK)));
    }

    // endregion
}