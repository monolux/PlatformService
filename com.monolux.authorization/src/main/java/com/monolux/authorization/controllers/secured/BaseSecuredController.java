package com.monolux.authorization.controllers.secured;

import com.monolux.authorization.controllers.BaseController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseSecuredController extends BaseController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String SECURED_API_PREFIX = "/api/v1/secured";

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    protected OAuth2Authentication getOAuth2Authentication() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication oAuth2Authentication) {
            return oAuth2Authentication;
        }

        return null;
    }

    // endregion
}