package com.monolux.authorization.configs;

import com.monolux.authorization.oauth2.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private final org.springframework.security.oauth2.provider.ClientDetailsService clientDetailsService;

    private final ApprovalStore approvalStore;

    private final org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices authorizationCodeServices;

    private final TokenStore tokenStore;

    private final TokenEnhancerChain tokenEnhancerChain;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    AuthorizationServerConfig(@Qualifier(DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION) final DataSource dataSource,
                              @Qualifier(WebSecurityConfig.BEAN_NAME_AUTHENTICATION_MANAGER) final AuthenticationManager authenticationManager,
                              @Qualifier(ApplicationConfig.BEAN_NAME_PASSWORD_ENCODER) final PasswordEncoder passwordEncoder,
                              @Qualifier(UserDetailsService.BEAN_NAME_USER_DETAILS_SERVICE) final UserDetailsService userDetailsService,
                              @Qualifier(ClientDetailsService.BEAN_NAME_CLIENT_DETAILS_SERVICE) final ClientDetailsService clientDetailsService,
                              @Qualifier(AuthorizationCodeServices.BEAN_NAME_AUTHORIZATION_CODE_SERVICES) final AuthorizationCodeServices authorizationCodeServices,
                              @Qualifier(JdbcTokenStore.BEAN_NAME_JDBC_TOKEN_STORE) final JdbcTokenStore tokenStore) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.approvalStore = new JdbcApprovalStore(dataSource);
        this.authorizationCodeServices = authorizationCodeServices;
        this.tokenStore = tokenStore;
        this.tokenEnhancerChain = new TokenEnhancerChain();
        this.tokenEnhancerChain.setTokenEnhancers(List.of(new TokenEnhancer()));
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(this.authenticationManager)
                .userDetailsService(this.userDetailsService)
                .approvalStore(this.approvalStore)
                .authorizationCodeServices(this.authorizationCodeServices)
                .tokenStore(this.tokenStore)
                .tokenEnhancer(this.tokenEnhancerChain);
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .withClientDetails(this.clientDetailsService);
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer security) {
        security.passwordEncoder(this.passwordEncoder)
                //.tokenKeyAccess("permitAll()")        // jwt Token Key Access
                .checkTokenAccess("isAuthenticated()")  // denyAll() : Default, permitAll()
                .allowFormAuthenticationForClients();
    }

    // endregion
}