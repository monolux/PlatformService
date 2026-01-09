package com.monolux.authorization.event.handlers;

import com.monolux.authorization.controllers.opened.LoginFormController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public AuthenticationFailureHandler() {
        super(String.format("%s?error", LoginFormController.LOGIN_FORM_URL));
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);
    }

    // endregion
}