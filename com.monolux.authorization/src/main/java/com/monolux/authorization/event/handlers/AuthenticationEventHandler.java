package com.monolux.authorization.event.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventHandler {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public AuthenticationEventHandler() {

    }

    // endregion

    // region ▒▒▒▒▒ EventListeners ▒▒▒▒▒

    @EventListener
    public void onSuccess(final AuthenticationSuccessEvent success) {

    }

    @EventListener
    public void onFailure(final AbstractAuthenticationFailureEvent failures) {

    }

    // endregion
}