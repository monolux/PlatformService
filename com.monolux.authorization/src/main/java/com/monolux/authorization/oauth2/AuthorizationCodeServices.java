package com.monolux.authorization.oauth2;

import com.monolux.authorization.configs.DataSourceConfig;
import com.monolux.common.constants.oauth2.OAuth2ParamNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Service(AuthorizationCodeServices.BEAN_NAME_AUTHORIZATION_CODE_SERVICES)
public class AuthorizationCodeServices extends JdbcAuthorizationCodeServices {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_AUTHORIZATION_CODE_SERVICES = "AuthorizationCodeServicesCustom";

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    AuthorizationCodeServices(@Qualifier(DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION) final DataSource dataSource) {
        super(dataSource);
    }

    // endregion

    // region ▒▒▒▒▒ User Define Methods ▒▒▒▒▒

    public String createAuthorizationCode(final UsernamePasswordAuthenticationToken authentication,
                                          final DefaultSavedRequest savedRequest,
                                          final DefaultOAuth2RequestFactory defaultOAuth2RequestFactory) {
        if (!authentication.isAuthenticated() || savedRequest == null || savedRequest.getQueryString() == null) {
            return null;
        }

        Map<String, String> parameters = OAuth2Utils.extractMap(savedRequest.getQueryString());

        if (!parameters.containsKey(OAuth2ParamNames.RESPONSE_TYPE) || !parameters.get(OAuth2ParamNames.RESPONSE_TYPE).equals(OAuth2ParamNames.CODE)) {
            return null;
        }

        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            AuthorizationRequest authorizationRequest = defaultOAuth2RequestFactory.createAuthorizationRequest(parameters);
            authorizationRequest.setApproved(true);
            OAuth2Request storedOAuth2Request = defaultOAuth2RequestFactory.createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);

            return this.createAuthorizationCode(combinedAuth);
        }

        return null;
    }

    // endregion
}