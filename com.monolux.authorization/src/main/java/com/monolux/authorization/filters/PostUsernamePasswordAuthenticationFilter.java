package com.monolux.authorization.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monolux.authorization.configs.ApplicationConfig;
import com.monolux.authorization.configs.WebSecurityConfig;
import com.monolux.authorization.controllers.opened.LoginFormController;
import com.monolux.authorization.oauth2.AuthorizationCodeServices;
import com.monolux.authorization.oauth2.ClientDetailsService;
import com.monolux.authorization.oauth2.UserDetails;
import com.monolux.authorization.oauth2.UserDetailsService;
import com.monolux.common.constants.oauth2.OAuth2Errors;
import com.monolux.common.constants.oauth2.OAuth2GrantType;
import com.monolux.common.constants.oauth2.OAuth2ParamNames;
import com.monolux.domain.services.ClientService;
import com.monolux.domain.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component(PostUsernamePasswordAuthenticationFilter.BEAN_NAME_POST_USERNAME_PASSWORD_AUTHENTICATION_FILTER)
public class PostUsernamePasswordAuthenticationFilter extends OncePerRequestFilter {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_POST_USERNAME_PASSWORD_AUTHENTICATION_FILTER = "postUsernamePasswordAuthenticationFilter";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final ClientDetailsService clientDetailsService;

    private final AuthorizationCodeServices authorizationCodeServices;

    private final UserService userService;

    private final ClientService clientService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    public PostUsernamePasswordAuthenticationFilter(@Qualifier(ApplicationConfig.BEAN_NAME_PASSWORD_ENCODER) final PasswordEncoder passwordEncoder,
                                                    @Qualifier(UserDetailsService.BEAN_NAME_USER_DETAILS_SERVICE) final UserDetailsService userDetailsService,
                                                    final ClientDetailsService clientDetailsService,
                                                    final AuthorizationCodeServices authorizationCodeServices,
                                                    final UserService userService,
                                                    final ClientService clientService) {
        super();
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.authorizationCodeServices = authorizationCodeServices;
        this.userService = userService;
        this.clientService = clientService;
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken authentication &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails principal) {
            Map<String, String[]> parameters = request.getParameterMap();
            String grantType = Optional.ofNullable(parameters.get(OAuth2ParamNames.GRANT_TYPE))
                    .map(value -> value[0].trim()).orElse("");
            String responseType = Optional.ofNullable(parameters.get(OAuth2ParamNames.RESPONSE_TYPE))
                    .map(value -> value[0].trim()).orElse("");

            if (responseType.equals(OAuth2ParamNames.CODE)) {
                this.doAuthorizationCodeGrant(authentication, principal, request, response);
            } else if (grantType.equals(OAuth2GrantType.PASSWORD)) {
                this.doPasswordGrant(authentication, principal, request, response);
            } else if (grantType.equals(OAuth2GrantType.CLIENT_CREDENTIALS)) {
                this.doClientCredentialGrant(authentication, principal, request, response);
            }
        }

        // is not redirected
        if (!response.isCommitted()) {
            filterChain.doFilter(request, response);
        }
    }

    // endregion

    // region ▒▒▒▒▒ User Define Methods ▒▒▒▒▒

    private void writeErrorResponse(final HttpServletResponse response, final RuntimeException ex) {
        try {
            if (!response.isCommitted()) {
                DefaultWebResponseExceptionTranslator exceptionTranslator = new DefaultWebResponseExceptionTranslator();
                ResponseEntity<OAuth2Exception> entity = exceptionTranslator.translate(ex);
                response.setStatus(entity.getStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), entity.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doAuthorizationCodeGrant(final UsernamePasswordAuthenticationToken authentication,
                                          final org.springframework.security.core.userdetails.UserDetails principal,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) {
        if (!authentication.isAuthenticated()) {
            return;
        }

        Map<String, String> parameters = OAuth2Utils.extractMap(request.getQueryString());

        if (!parameters.containsKey(OAuth2ParamNames.RESPONSE_TYPE) || !parameters.get(OAuth2ParamNames.RESPONSE_TYPE).equals(OAuth2ParamNames.CODE)) {
            return;
        }

        if (!(new HttpSessionRequestCache().getRequest(request, response) instanceof DefaultSavedRequest defaultSavedRequest)) {
            return;
        }

        String userId = principal.getUsername();
        Optional<String> optClientId = Optional.ofNullable(parameters.get(OAuth2ParamNames.CLIENT_ID));

        if (optClientId.isEmpty()) {
            return;
        }

        String ip = request.getRemoteAddr();

        if (!(principal instanceof UserDetails userDetails)) {
            return;
        }

        Optional.ofNullable(this.authorizationCodeServices.createAuthorizationCode(authentication, defaultSavedRequest,
                        new DefaultOAuth2RequestFactory(this.clientDetailsService)))
                .ifPresent(createdCode -> {
                    if (userDetails.isUsingOtp() && !this.userService.existsUserIpConfirm(userId, ip)) {
                        this.userService.createUserIpConfirm(userId, ip, optClientId.get(), String.format("%s?code=%s", parameters.get(OAuth2ParamNames.REDIRECT_URI), createdCode))
                                .ifPresent(userIpConfirm -> {
                                    try {
                                        String url = String.format("%s?%s=%s", LoginFormController.OTP_LOGIN_FORM_URL,
                                                LoginFormController.OTP_LOGIN_PARAM_NAME, userIpConfirm.getId());
                                        url = String.format("%s?redirect=%s", WebSecurityConfig.LOGOUT_URL, URLEncoder.encode(url, StandardCharsets.UTF_8));
                                        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                                        response.sendRedirect(url);
                                        authentication.setAuthenticated(false);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    } else {
                        try {
                            String url = String.format("%s?code=%s", parameters.get(OAuth2ParamNames.REDIRECT_URI), createdCode);
                            url = String.format("%s?redirect=%s", WebSecurityConfig.LOGOUT_URL, URLEncoder.encode(url, StandardCharsets.UTF_8));
                            response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                            response.sendRedirect(url);
                            authentication.setAuthenticated(false);

                            boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
                            String messageSuccess = isLangKorean ? "성공" : "Success";

                            this.createUserSignInLog(userId, optClientId.get(), OAuth2GrantType.AUTHORIZATION_CODE, ip, true, messageSuccess);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void doPasswordGrant(final UsernamePasswordAuthenticationToken authentication,
                                 final org.springframework.security.core.userdetails.UserDetails principal,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response) {
        if (!authentication.isAuthenticated()) {
            return;
        }

        Map<String, String[]> parameters = request.getParameterMap();
        String grantType = Optional.ofNullable(parameters.get(OAuth2ParamNames.GRANT_TYPE))
                .map(value -> value[0].trim()).orElse("");
        List<String> scopes = Optional.ofNullable(parameters.get(OAuth2ParamNames.SCOPE))
                .map(value -> Arrays.stream(URLDecoder.decode(value[0].trim(), StandardCharsets.UTF_8).split(" ")).toList())
                .orElse(Collections.emptyList());

        if (!grantType.equals(OAuth2GrantType.PASSWORD) || scopes.isEmpty()) {
            return;
        }

        boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
        String messageSuccess = isLangKorean ? "성공" : "Success";
        String messageFailed = OAuth2Errors.BAD_CREDENTIAL.getErrorMessage();
        String messageInvalidScope = String.format("%s %s", messageFailed, isLangKorean ? "(유효하지 않은 scope)" : "(Invalid scope)");

        String userId = Optional.ofNullable(parameters.get(OAuth2ParamNames.USERNAME))
                .map(value -> value[0].trim()).orElse("");
        String password = Optional.ofNullable(parameters.get(OAuth2ParamNames.PASSWORD))
                .map(value -> value[0].trim()).orElse("");
        String clientId = principal.getUsername();
        UserDetails user = null;
        ClientDetails client = null;

        String ip = request.getRemoteAddr();

        try {
            user = userId.isBlank() ? null : this.userDetailsService.loadUserByUsername(userId);
            client = clientId.isBlank() ? null : clientDetailsService.loadClientByClientId(clientId);
        } catch(UsernameNotFoundException | NoSuchClientException ex) {
            this.createUserSignInLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, false, String.format("%s (%s)", messageFailed, ex.getMessage()));
            authentication.setAuthenticated(false);
            this.writeErrorResponse(response, ex);
        }

        if (user != null && client != null) {
            if (this.passwordEncoder.matches(password, user.getPassword())) {
                if (user.getSignInFailedCnt() >= UserDetailsService.SIGN_IN_FAILED_LIMIT) {
                    String message = isLangKorean ? "로그인 실패 한도 초과로 계정이 잠겼습니다." : "Account locked due to login attempts exceeded.";
                    this.createUserSignInLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, false, message);
                    authentication.setAuthenticated(false);
                    this.writeErrorResponse(response, new BadCredentialsException(message));
                } else if (this.clientDetailsService.loadClientByClientId(clientId).getScope().containsAll(scopes)) {
                    this.createUserSignInLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, true, messageSuccess);
                } else {
                    this.createUserSignInLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, false, messageInvalidScope);
                    authentication.setAuthenticated(false);
                    this.writeErrorResponse(response, new InvalidScopeException(messageInvalidScope));
                }
            } else {
                this.createUserSignInLog(userId, clientId, OAuth2GrantType.PASSWORD, ip, false, messageFailed);
                authentication.setAuthenticated(false);
                this.writeErrorResponse(response, new BadCredentialsException(messageFailed));
            }
        }
    }

    private void doClientCredentialGrant(final UsernamePasswordAuthenticationToken authentication,
                                         final org.springframework.security.core.userdetails.UserDetails principal,
                                         final HttpServletRequest request,
                                         final HttpServletResponse response) {
        Map<String, String[]> parameters = request.getParameterMap();
        String grantType = Optional.ofNullable(parameters.get(OAuth2ParamNames.GRANT_TYPE))
                .map(value -> value[0].trim()).orElse("");
        List<String> scopes = Optional.ofNullable(parameters.get(OAuth2ParamNames.SCOPE))
                .map(value -> Arrays.stream(URLDecoder.decode(value[0].trim(), StandardCharsets.UTF_8).split(" ")).toList())
                .orElse(Collections.emptyList());

        if (!grantType.equals(OAuth2GrantType.CLIENT_CREDENTIALS) || scopes.isEmpty()) {
            return;
        }

        String clientId = principal.getUsername();

        boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
        String messageSuccess = isLangKorean ? "성공" : "Success";
        String messageFailed = OAuth2Errors.BAD_CREDENTIAL.getErrorMessage();
        String messageInvalidScope = String.format("%s %s", messageFailed, isLangKorean ? "(유효하지 않은 scope)" : "(Invalid scope)");
        String ip = request.getRemoteAddr();

        if (this.clientDetailsService.loadClientByClientId(clientId).getScope().containsAll(scopes)) {
            this.createClientSignInLog(clientId, ip, true, messageSuccess);
        } else {
            this.createClientSignInLog(clientId, ip, false, messageInvalidScope);
            authentication.setAuthenticated(false);
            this.writeErrorResponse(response, new InvalidScopeException(messageInvalidScope));
        }
    }

    private void createUserSignInLog(final String userId,
                                     final String clientId,
                                     final String grantType,
                                     final String ip,
                                     final boolean success,
                                     final String message) {
        if (success) {
            this.userService.signInSuccess(userId, clientId, grantType, ip);
        } else {
            this.userService.signInFailed(userId, clientId, grantType, ip, message);
        }
    }

    private void createClientSignInLog(final String clientId,
                                       final String ip,
                                       final boolean success,
                                       final String message) {
        if (success) {
            this.clientService.createClientSignInLog(clientId, ip, true, null);
        } else {
            this.clientService.createClientSignInLog(clientId, ip, false, message);
        }
    }

    // endregion
}