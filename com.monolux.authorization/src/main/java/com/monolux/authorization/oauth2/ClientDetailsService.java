package com.monolux.authorization.oauth2;

import com.monolux.authorization.configs.ApplicationConfig;
import com.monolux.authorization.configs.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Slf4j
@Service(ClientDetailsService.BEAN_NAME_CLIENT_DETAILS_SERVICE)
public class ClientDetailsService extends JdbcClientDetailsService {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_CLIENT_DETAILS_SERVICE = "clientDetailsServiceCustom";

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    ClientDetailsService(@Qualifier(DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION) final DataSource dataSource,
                         @Qualifier(ApplicationConfig.BEAN_NAME_PASSWORD_ENCODER) final PasswordEncoder passwordEncoder) {
        super(dataSource);
        this.setPasswordEncoder(passwordEncoder);
    }

    // endregion
}