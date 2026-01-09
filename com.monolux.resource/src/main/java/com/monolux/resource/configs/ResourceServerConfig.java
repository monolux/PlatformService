package com.monolux.resource.configs;

import com.monolux.domain.enumerations.Authorities;
import com.monolux.domain.enumerations.AuthorityType;
import com.monolux.resource.controllers.opened.BaseOpenedController;
import com.monolux.resource.controllers.secured.BaseSecuredController;
import com.monolux.resource.controllers.secured.SecuredUserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final Environment environment;

    @Value("${spring.security.oauth2.client.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resource.token-info-uri}")
    private String checkTokenEndpointUri;

    @Value("${management.endpoints.web.base-path}")
    private String actuatorBasePath;

    private final PasswordEncoder passwordEncoder;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    ResourceServerConfig(final Environment environment) {
        this.environment = environment;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @Bean
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Primary
    @Bean
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId(this.clientId);
        remoteTokenServices.setClientSecret(this.clientSecret);
        remoteTokenServices.setCheckTokenEndpointUrl(this.checkTokenEndpointUri);
        return remoteTokenServices;
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        String actuatorUrls = this.actuatorBasePath + "/**";
        String openedApiUrls = BaseOpenedController.OPENED_API_PREFIX + "/**";
        String securedApiUrls = BaseSecuredController.SECURED_API_PREFIX + "/**";
        String securedUserApiUrls = SecuredUserController.BASE_PATH + "/**";

        List<String> permittedUrls = new ArrayList<>();

        // Swagger
        if (Arrays.stream(this.environment.getActiveProfiles())
                .anyMatch(profile -> profile.contains("local") || profile.contains("dev"))) {
            permittedUrls.addAll(Arrays.asList(SwaggerConfig.PERMIT_SWAGGER_URLS));
        }

        // Spring Actuator
        permittedUrls.add(actuatorUrls);

        // Opened Api
        permittedUrls.add(openedApiUrls);

        http
                .authorizeRequests()

                // Permited Urls
                .antMatchers(permittedUrls.toArray(String[]::new))
                .permitAll()

                // Secured User API (Apply Authority)
                // TODO : 현재는 일반 유저 밖에 없기 때문에 권한 심플, 추후 권한이 추가되면 작업 예정
                .antMatchers(securedUserApiUrls)
                .hasAnyAuthority(Arrays.stream(Authorities.values())
                        .filter(authorities -> authorities.getAuthorityType().equals(AuthorityType.USER))
                        .map(Authorities::getCode)
                        .toArray(String[]::new))

                // Secured API
                .antMatchers(securedApiUrls)
                .authenticated()

                .anyRequest().authenticated();
    }

    // endregion
}