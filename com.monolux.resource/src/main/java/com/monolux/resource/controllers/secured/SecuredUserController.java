package com.monolux.resource.controllers.secured;

import com.monolux.common.web.response.ApiResponse;
import com.monolux.domain.exceptions.DomainEntityNotFoundException;
import com.monolux.domain.services.UserService;
import com.monolux.resource.dto.res.UserPwdChangedResInfo;
import com.monolux.resource.dto.res.UserResInfo;
import com.monolux.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "User API")
@RestController
@RequestMapping(value = SecuredUserController.BASE_PATH)
public class SecuredUserController extends BaseSecuredController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String BASE_PATH = BaseSecuredController.SECURED_API_PREFIX + "/users";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    SecuredUserController(final UserService userService, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // endregion

    // region ▒▒▒▒▒ Get Mapping ▒▒▒▒▒

    @ApiOperation(value = "Current User Info")
    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse> findMe(@RequestHeader final HttpHeaders reqHeaders,
                                              @AuthenticationPrincipal final String userId) {
        return this.userService.findUserById(userId)
                .map(user -> ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK, UserResInfo.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userNick(user.getUserNick())
                        .build())))
                .orElseThrow(DomainEntityNotFoundException::new);
    }

    // endregion

    // region ▒▒▒▒▒ Patch Mapping ▒▒▒▒▒

    @ApiOperation(value = "Initialize User Password Random")
    @PatchMapping(value = "/password/init")
    public ResponseEntity<ApiResponse> initUserPwd(@RequestHeader final HttpHeaders reqHeaders,
                                                   @AuthenticationPrincipal final String userId) {
        UserPwdChangedResInfo userPwdChangedResInfo = new UserPwdChangedResInfo(new RandomUtil().generateRandomPassword());
        String userPwdNewEncrypted = this.passwordEncoder.encode(userPwdChangedResInfo.userPwdChanged());

        this.userService.changeUserPwd(userId, userPwdNewEncrypted, true);

        return ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK, userPwdChangedResInfo));
    }

    // endregion
}