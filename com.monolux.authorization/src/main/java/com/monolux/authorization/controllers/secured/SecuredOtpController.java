package com.monolux.authorization.controllers.secured;

import com.monolux.common.web.response.ApiResponse;
import com.monolux.domain.services.UserService;
import com.monolux.utils.OtpUtil;
import com.monolux.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = SecuredOtpController.BASE_PATH)
public class SecuredOtpController extends BaseSecuredController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String BASE_PATH = BaseSecuredController.SECURED_API_PREFIX + "/otp";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Value("${otp:issuer-name}")
    private String otpIssuerName;

    private final UserService userService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    SecuredOtpController(final UserService userService) {
        this.userService = userService;
    }

    // endregion

    // region ▒▒▒▒▒ Get Mapping ▒▒▒▒▒

    @GetMapping(value = "/secret/new/qr")
    public ResponseEntity<byte[]> createNewOtpSecretQr() {
        return this.userService.createUserTempOtpSecret(this.getOAuth2Authentication().getName())
                .map(user -> {
                    String url = OtpUtil.createOtpSecretUrl(this.otpIssuerName, user.getUserId(), user.getOtpSecretTmp());
                    byte[] qrCodeImage = QRCodeUtil.createQRCode(url, 350, 350);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(QRCodeUtil.MEDIA_TYPE);
                    return ResponseEntity.ok().headers(headers).body(qrCodeImage);
                }).orElse(ResponseEntity.internalServerError().build());
    }

    @GetMapping(value = "/secret/new/text")
    public ResponseEntity<ApiResponse> createNewOtpSecretText() {
        return this.userService.createUserTempOtpSecret(this.getOAuth2Authentication().getName())
                .map(user -> ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK, user.getOtpSecretTmp())))
                .orElse(ResponseEntity.internalServerError().build());
    }

    // endregion

    // region ▒▒▒▒▒ Post Mapping ▒▒▒▒▒

    @PostMapping(value = "/secret/new/confirm/{otpValue}")
    public ResponseEntity<ApiResponse> confirmNewOtpSecret(@PathVariable(value = "otpValue") final String otpValue) {
        return this.userService.confirmUserTempOtpSecret(this.getOAuth2Authentication().getName(), otpValue)
                .map(user -> ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK)))
                .orElse(ResponseEntity.internalServerError().build());
    }

    // endregion
}