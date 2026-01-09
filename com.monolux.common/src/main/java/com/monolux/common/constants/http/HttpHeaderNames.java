package com.monolux.common.constants.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpHeaderNames {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    // endregion
}