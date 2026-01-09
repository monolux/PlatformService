package com.monolux.authorization.event.publisher;

import com.monolux.common.constants.oauth2.OAuth2GrantType;
import com.monolux.common.constants.oauth2.OAuth2ParamNames;
import com.monolux.common.constants.oauth2.Oauth2ResponseType;
import com.monolux.domain.services.ClientService;
import com.monolux.domain.services.UserService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticationEventPublisher extends DefaultAuthenticationEventPublisher {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final UserService userService;

    private final ClientService clientService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public AuthenticationEventPublisher(final org.springframework.context.ApplicationEventPublisher applicationEventPublisher,
                                        final UserService userService,
                                        final ClientService clientService) {
        super(applicationEventPublisher);
        this.userService = userService;
        this.clientService = clientService;
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        super.publishAuthenticationSuccess(authentication);
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        this.createSignInFailedLog(authentication, exception);
        super.publishAuthenticationFailure(exception, authentication);
    }

    // endregion

    // region ▒▒▒▒▒ User Define Methods ▒▒▒▒▒

    private HttpServletRequest getCurrentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }

        return null;
    }

    private HttpServletResponse getCurrentResponse() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getResponse();
        }

        return null;
    }

    private DefaultSavedRequest getSavedRequest() {
        return Optional.ofNullable(this.getCurrentRequest())
                .flatMap(request -> Optional.ofNullable(this.getCurrentResponse())
                        .map(response -> {
                            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                            return savedRequest instanceof DefaultSavedRequest defaultSavedRequest ? defaultSavedRequest : null;
                        })).orElse(null);
    }

    private void createUserSignInFailedLog(final String userId,
                                           final String clientId,
                                           final String grantType,
                                           final String ip,
                                           final String message) {
        this.userService.signInFailed(userId, clientId, grantType, ip, message);
    }

    private void createClientSignInFailedLog(final String clientId,
                                             final String ip,
                                             final String message) {
        this.clientService.createClientSignInLog(clientId, ip, false, message);
    }

    private void createSignInFailedLog(final Authentication authentication, final Exception ex) {
        String message = ex.getMessage();

        if (authentication.getDetails() instanceof WebAuthenticationDetails webAuthenticationDetails) {
            String ip = webAuthenticationDetails.getRemoteAddress();
            Map<String, String[]> parameterMap = Optional.ofNullable(this.getCurrentRequest())
                    .map(ServletRequest::getParameterMap)
                    .orElse(new HashMap<>());

            if (parameterMap.containsKey(OAuth2ParamNames.GRANT_TYPE) && parameterMap.get(OAuth2ParamNames.GRANT_TYPE)[0].equals(OAuth2GrantType.PASSWORD)) {
                String userId = parameterMap.get(OAuth2ParamNames.USERNAME)[0];
                String clientId = authentication.getName();
                this.createUserSignInFailedLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, message);
            } else if (parameterMap.containsKey(OAuth2ParamNames.GRANT_TYPE) && parameterMap.get(OAuth2ParamNames.GRANT_TYPE)[0].equals(OAuth2GrantType.CLIENT_CREDENTIALS)) {
                this.createClientSignInFailedLog(authentication.getName(), ip, message);
            } else {
                Map<String, String> parameters = Optional.ofNullable(this.getSavedRequest())
                        .map(savedRequest -> OAuth2Utils.extractMap(savedRequest.getQueryString()))
                        .orElse(Collections.emptyMap());

                if (parameters.containsKey(OAuth2ParamNames.RESPONSE_TYPE) && parameters.get(OAuth2ParamNames.RESPONSE_TYPE).equals(Oauth2ResponseType.CODE)) {
                    String userId = authentication.getName();
                    String clientId = Optional.ofNullable(parameters.get(OAuth2ParamNames.CLIENT_ID)).orElse("");
                    this.createUserSignInFailedLog(userId, clientId, OAuth2GrantType.AUTHORIZATION_CODE, ip, message);
                }
            }
        } else if (authentication.getDetails() instanceof HashMap<?,?> hashMap &&
                hashMap.containsKey(OAuth2Utils.GRANT_TYPE) &&
                hashMap.get(OAuth2Utils.GRANT_TYPE).equals(OAuth2GrantType.PASSWORD) &&
                SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken clientToken) {
            String userId = authentication.getName();
            String clientId = clientToken.getName();
            String ip = clientToken.getDetails() instanceof WebAuthenticationDetails webAuthenticationDetails ? webAuthenticationDetails.getRemoteAddress() :
                    Optional.ofNullable(this.getCurrentRequest()).map(ServletRequest::getRemoteAddr).orElse("127.0.0.1");
            this.createUserSignInFailedLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, message);
        }
    }

    // endregion
}