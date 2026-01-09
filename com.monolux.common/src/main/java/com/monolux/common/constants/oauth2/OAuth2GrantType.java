package com.monolux.common.constants.oauth2;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuth2GrantType {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String AUTHORIZATION_CODE = "authorization_code";

    public static final String PASSWORD = "password";

    public static final String CLIENT_CREDENTIALS = "client_credentials";

    public static final String REFRESH_TOKEN = "refresh_token";

    // endregion
}