package com.monolux.common.constants.oauth2;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;

public enum OAuth2Errors {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    BAD_CREDENTIAL,
    CREDENTIALS_EXPIRED,
    LOCKED,
    EXPIRED,
    DISABLED;

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    OAuth2Errors() {

    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public String getErrorMessage() {
        if (this.equals(BAD_CREDENTIAL)) {
            return this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials");
        } else if (this.equals(CREDENTIALS_EXPIRED)) {
            return this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired");
        } else if (this.equals(LOCKED)) {
            return this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked");
        } else if (this.equals(EXPIRED)) {
            return this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired");
        } else if (this.equals(DISABLED)) {
            return this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled");
        } else {
            throw new IllegalArgumentException();
        }
    }

    // endregion
}