package com.monolux.authorization.event.handlers;

import com.monolux.authorization.controllers.opened.LoginFormController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public LogoutSuccessHandler() {
        this.setDefaultTargetUrl(String.format("%s?logout", LoginFormController.LOGIN_FORM_URL));
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        /* 로그인 정보 초기화
        Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .ifPresent(auth -> new SecurityContextLogoutHandler().logout(request, response, authentication));
        */

        if (!response.isCommitted() && request.getParameter("redirect") != null) {
            String redirect = URLDecoder
                    .decode(request.getParameter("redirect").trim(), StandardCharsets.UTF_8);

            this.getRedirectStrategy().sendRedirect(request, response, redirect);
        } else {
            super.onLogoutSuccess(request, response, authentication);
        }
    }

    // endregion
}