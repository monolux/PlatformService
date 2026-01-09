package com.monolux.resource.controllers.secured;

import com.monolux.resource.controllers.BaseController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseSecuredController extends BaseController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String SECURED_API_PREFIX = BaseController.API_PREFIX + "/secured";

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    protected OAuth2Authentication getOAuth2Authentication() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication oAuth2Authentication) {
            return oAuth2Authentication;
        }

        return null;
    }

    // endregion
}