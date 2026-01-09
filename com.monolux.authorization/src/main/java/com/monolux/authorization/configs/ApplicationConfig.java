package com.monolux.authorization.configs;

import com.monolux.domain.services.ClientService;
import com.monolux.domain.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = { "com.monolux.domain" })
@EntityScan(basePackages = { "com.monolux.domain" })
@ComponentScan(basePackages = { "com.monolux.encryption" })
public class ApplicationConfig {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_PASSWORD_ENCODER = "passwordEncoder";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationEventPublisher authenticationEventPublisher;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    ApplicationConfig(final ApplicationEventPublisher applicationEventPublisher,
                      final UserService userService,
                      final ClientService clientService) {
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.authenticationEventPublisher = new com.monolux.authorization.event.publisher.AuthenticationEventPublisher(applicationEventPublisher, userService, clientService);
    }

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @Qualifier(ApplicationConfig.BEAN_NAME_PASSWORD_ENCODER)
    @Bean(name = ApplicationConfig.BEAN_NAME_PASSWORD_ENCODER)
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return this.authenticationEventPublisher;
    }

    // endregion
}