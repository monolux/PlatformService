package com.monolux.authorization.configs;

import com.monolux.authorization.controllers.opened.LoginFormController;
import com.monolux.authorization.controllers.opened.TurnstileController;
import com.monolux.authorization.filters.PostUsernamePasswordAuthenticationFilter;
import com.monolux.authorization.event.handlers.AuthenticationFailureHandler;
import com.monolux.authorization.event.handlers.AuthenticationSuccessHandler;
import com.monolux.authorization.event.handlers.LogoutSuccessHandler;
import com.monolux.authorization.oauth2.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_AUTHENTICATION_MANAGER = "authenticationManager";

    public final static String USERNAME_ELEMENT_ID = "username";

    public final static String PASSWORD_ELEMENT_ID = "password";

    private final static String OAUTH_URL = "/oauth";

    public final static String LOGIN_PROCESSING_URL = "/login";

    public final static String LOGOUT_URL = "/logout";

    private final static String RESOURCE_URL = "/resources";

    private final static String CSS_URL = "/css";

    private final static String IMG_URL = "/img";

    private final static String JS_URL = "/js";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Value("${management.endpoints.web.base-path}")
    private String actuatorBasePath;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final ClientDetailsService clientDetailsService;

    private final PostUsernamePasswordAuthenticationFilter postUsernamePasswordAuthenticationFilter;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    WebSecurityConfig(@Qualifier(ApplicationConfig.BEAN_NAME_PASSWORD_ENCODER) final PasswordEncoder passwordEncoder,
                      @Qualifier(UserDetailsService.BEAN_NAME_USER_DETAILS_SERVICE) final UserDetailsService userDetailsService,
                      @Qualifier(ClientDetailsService.BEAN_NAME_CLIENT_DETAILS_SERVICE) final ClientDetailsService clientDetailsService,
                      @Qualifier(PostUsernamePasswordAuthenticationFilter.BEAN_NAME_POST_USERNAME_PASSWORD_AUTHENTICATION_FILTER)
                      final PostUsernamePasswordAuthenticationFilter postUsernamePasswordAuthenticationFilter,
                      final AuthenticationSuccessHandler authenticationSuccessHandler,
                      final AuthenticationFailureHandler authenticationFailureHandler,
                      final LogoutSuccessHandler logoutSuccessHandler) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.postUsernamePasswordAuthenticationFilter = postUsernamePasswordAuthenticationFilter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒



    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Qualifier(WebSecurityConfig.BEAN_NAME_AUTHENTICATION_MANAGER)
    @Bean(name = WebSecurityConfig.BEAN_NAME_AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        // super.configure(auth);
        auth
                .authenticationProvider(new AuthenticationProvider(this.userDetailsService,
                        this.passwordEncoder, this.clientDetailsService));
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        String[] uris = new String[] { WebSecurityConfig.LOGIN_PROCESSING_URL, WebSecurityConfig.LOGOUT_URL,
                TurnstileController.BASE_PATH + "/**",
                WebSecurityConfig.OAUTH_URL + "/**",
                this.actuatorBasePath + "/**",
                WebSecurityConfig.RESOURCE_URL + "/**",
                WebSecurityConfig.CSS_URL + "/**",
                WebSecurityConfig.IMG_URL + "/**",
                WebSecurityConfig.JS_URL + "/**",
                LoginFormController.LOGIN_FORM_URL,
                LoginFormController.OTP_LOGIN_FORM_URL,
                LoginFormController.OTP_LOGIN_PROCESSING_URL};

        http
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()

                .authorizeRequests()
                .antMatchers(uris).permitAll()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage(LoginFormController.LOGIN_FORM_URL)
                .usernameParameter(WebSecurityConfig.USERNAME_ELEMENT_ID)
                .passwordParameter(WebSecurityConfig.PASSWORD_ELEMENT_ID)
                .loginProcessingUrl(WebSecurityConfig.LOGIN_PROCESSING_URL)
                .successHandler(this.authenticationSuccessHandler)
                .failureHandler(this.authenticationFailureHandler)
                .permitAll()
                .and()

                .logout()
                .logoutUrl(WebSecurityConfig.LOGOUT_URL)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(this.logoutSuccessHandler)
                .and()

                .addFilterAfter(this.postUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .httpBasic();
    }

    @Override
    public void configure(final WebSecurity web) {

    }

    // endregion
}