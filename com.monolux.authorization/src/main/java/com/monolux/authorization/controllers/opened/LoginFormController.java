package com.monolux.authorization.controllers.opened;

import com.monolux.authorization.configs.WebSecurityConfig;
import com.monolux.domain.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequestMapping
public class LoginFormController extends BaseOpenedController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String LOGIN_FORM_URL = "/forms/login";

    public static final String OTP_LOGIN_FORM_URL = "/forms/login/otp";

    public static final String OTP_LOGIN_PROCESSING_URL = "/forms/login/otp/process";

    public static final String OTP_LOGIN_PARAM_NAME = "otpId";

    private static final String OTP_ID_ELEMENT_ID = "otpId";

    private static final String OTP_PASSWORD_ELEMENT_ID = "otpPassword";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final HttpSession httpSession;

    @Value("${cloud-flare.turnstile.site-key}")
    private String cloudFlareTurnstileSiteKey;

    @Value("${server.port}")
    private int port;

    private final UserService userService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    LoginFormController(final HttpSession httpSession,
                        final UserService userService) {
        this.httpSession = httpSession;
        this.userService = userService;
    }

    // endregion

    // region ▒▒▒▒▒ Get Mapping ▒▒▒▒▒

    @GetMapping(value = LoginFormController.LOGIN_FORM_URL)
    public String loginForm(final Model model,
                            HttpServletRequest request, HttpServletResponse response) {
        if (this.httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof Exception exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }

        model.addAttribute("usernameElementId", WebSecurityConfig.USERNAME_ELEMENT_ID);
        model.addAttribute("passwordElementId", WebSecurityConfig.PASSWORD_ELEMENT_ID);

        if (LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage())) {
            model.addAttribute("logoutMessage", "로그아웃 되었습니다.");
        } else {
            model.addAttribute("logoutMessage", "You have been signed out.");
        }

        model.addAttribute("cloudFlareTurnstileSiteKey", this.cloudFlareTurnstileSiteKey);
        model.addAttribute("cloudFlareTurnstileTokenValidateUrl", String.format("http://localhost:%d/turnstile/validate", this.port));

        return "LoginForm";
    }

    @GetMapping(value = LoginFormController.OTP_LOGIN_FORM_URL)
    public String otpLoginForm(final Model model,
                               @RequestParam(name = LoginFormController.OTP_LOGIN_PARAM_NAME) final String otpId) {
        model.addAttribute(LoginFormController.OTP_LOGIN_PARAM_NAME, otpId);

        model.addAttribute("otpIdElementId", LoginFormController.OTP_ID_ELEMENT_ID);
        model.addAttribute("otpPasswordElementId", LoginFormController.OTP_PASSWORD_ELEMENT_ID);

        model.addAttribute("otpLoginProcessingUrl", LoginFormController.OTP_LOGIN_PROCESSING_URL);

        return "OtpLoginForm";
    }

    // endregion

    // region ▒▒▒▒▒ Post Mapping ▒▒▒▒▒

    @PostMapping(value = LoginFormController.OTP_LOGIN_PROCESSING_URL)
    public String otpLogin(@RequestParam(LoginFormController.OTP_ID_ELEMENT_ID) final String otpId,
                           @RequestParam(LoginFormController.OTP_PASSWORD_ELEMENT_ID) final String otpPassword) {
        return this.userService.validateUserIpConfirmUsingOtpLogin(otpId, otpPassword)
                .map(userIpConfirm -> String.format("redirect:%s", userIpConfirm.getRedirectUri()))
                .orElse(String.format("redirect:%s?%s=%s", LoginFormController.OTP_LOGIN_FORM_URL, LoginFormController.OTP_LOGIN_PARAM_NAME, otpId));
    }

    // endregion
}