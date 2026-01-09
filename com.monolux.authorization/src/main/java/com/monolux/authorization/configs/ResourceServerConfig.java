package com.monolux.authorization.configs;

import com.monolux.authorization.controllers.secured.BaseSecuredController;
import com.monolux.authorization.controllers.secured.SecuredOtpController;
import com.monolux.domain.enumerations.Authorities;
import com.monolux.domain.enumerations.AuthorityType;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.Arrays;

@Configuration(proxyBeanMethods = false)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String securedApiUrls = BaseSecuredController.SECURED_API_PREFIX + "/**";
        String securedOtpApiUrls = SecuredOtpController.BASE_PATH + "/**";

        http
                .requestMatchers()
                .antMatchers(securedApiUrls)
                .and()
                .authorizeRequests()

                // Secured OTP API (Apply Authority)
                .antMatchers(securedOtpApiUrls)
                .hasAnyAuthority(Arrays.stream(Authorities.values())
                        .filter(authorities -> authorities.getAuthorityType().equals(AuthorityType.USER))
                        .map(Authorities::getCode)
                        .toList().toArray(String[]::new))

                // Secured API
                .antMatchers(securedApiUrls)
                .authenticated()

                .anyRequest().authenticated();
    }

    // endregion
}