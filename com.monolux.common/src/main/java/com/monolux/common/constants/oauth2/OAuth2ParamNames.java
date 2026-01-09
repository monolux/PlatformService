package com.monolux.common.constants.oauth2;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuth2ParamNames {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String CLIENT_ID = OAuth2Utils.CLIENT_ID;

    public static final String GRANT_TYPE = OAuth2Utils.GRANT_TYPE;

    public static final String RESPONSE_TYPE = OAuth2Utils.RESPONSE_TYPE;

    public static final String SCOPE = OAuth2Utils.SCOPE;

    public static final String REDIRECT_URI = OAuth2Utils.REDIRECT_URI;

    public static final String CODE = "code";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    // endregion
}