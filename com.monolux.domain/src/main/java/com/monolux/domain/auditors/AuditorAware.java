package com.monolux.domain.auditors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

@Slf4j
public class AuditorAware implements org.springframework.data.domain.AuditorAware<String> {
    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName).orElse("system"));
    }

    // endregion
}