package com.monolux.authorization.oauth2;

import com.monolux.common.constants.oauth2.OAuth2Errors;
import com.monolux.common.constants.oauth2.OAuth2ParamNames;
import com.monolux.common.constants.oauth2.Oauth2ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class AuthenticationProvider extends DaoAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final ClientDetailsService clientDetailsService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public AuthenticationProvider(final UserDetailsService userDetailsService,
                                  final PasswordEncoder passwordEncoder,
                                  final ClientDetailsService clientDetailsService) {
        super();
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(passwordEncoder);
        super.setPreAuthenticationChecks(new AccountStatusUserDetailsChecker());
        this.clientDetailsService = clientDetailsService;
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        this.checkSignInFailedLimit(userDetails, authentication);
        this.checkAuthorizationCodeGrantClientInfo(authentication);

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    // endregion

    // region ▒▒▒▒▒ User Define Methods ▒▒▒▒▒

    private void checkSignInFailedLimit(final UserDetails userDetails,
                                        final UsernamePasswordAuthenticationToken authentication) {
        if (userDetails instanceof com.monolux.authorization.oauth2.UserDetails user) {
            int signInFailedCnt = user.getSignInFailedCnt();

            if (authentication.getCredentials() != null) {
                if (!this.getPasswordEncoder().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                    signInFailedCnt++;
                }
            }

            if (signInFailedCnt >= com.monolux.authorization.oauth2.UserDetailsService.SIGN_IN_FAILED_LIMIT) {
                authentication.setAuthenticated(false);
                boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
                String message = isLangKorean ? "로그인 실패 한도 초과로 계정이 잠겼습니다." : "Account locked due to login attempts exceeded.";
                throw new BadCredentialsException(message);
            }
        }
    }

    private void checkAuthorizationCodeGrantClientInfo(final UsernamePasswordAuthenticationToken authentication) {
        Optional.ofNullable(this.getSavedRequest())
                .ifPresent(savedRequest -> {
                    Map<String, String> savedRequestParameters = OAuth2Utils.extractMap(savedRequest.getQueryString());

                    if (savedRequestParameters.containsKey(OAuth2ParamNames.RESPONSE_TYPE) &&
                            savedRequestParameters.get(OAuth2ParamNames.RESPONSE_TYPE).equals(Oauth2ResponseType.CODE) &&
                            savedRequestParameters.containsKey(OAuth2ParamNames.CLIENT_ID) &&
                            savedRequestParameters.containsKey(OAuth2ParamNames.SCOPE)) {
                        String clientId = savedRequestParameters.get(OAuth2ParamNames.CLIENT_ID);
                        String redirectUri = savedRequestParameters.get(OAuth2ParamNames.REDIRECT_URI);

                        if (redirectUri.isBlank()) {
                            return;
                        }

                        List<String> scopes = Optional.ofNullable(savedRequestParameters.get(OAuth2ParamNames.SCOPE))
                                .map(scope -> Arrays.stream(URLDecoder.decode(scope, StandardCharsets.UTF_8).split(" ")).toList())
                                .orElse(Collections.emptyList());

                        if (scopes.isEmpty()) {
                            return;
                        }

                        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);

                        if (!clientDetails.getRegisteredRedirectUri().contains(redirectUri)) {
                            authentication.setAuthenticated(false);
                            boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
                            String errorMessage = OAuth2Errors.BAD_CREDENTIAL.getErrorMessage();
                            errorMessage = String.format("%s %s", errorMessage, isLangKorean ? "(유효하지 않은 redirect uri)" : "(Invalid redirect uri)");
                            throw new InternalAuthenticationServiceException(errorMessage, new RedirectMismatchException(errorMessage));
                        } else if (!clientDetails.getScope().containsAll(scopes)) {
                            authentication.setAuthenticated(false);
                            boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
                            String errorMessage = OAuth2Errors.BAD_CREDENTIAL.getErrorMessage();
                            errorMessage = String.format("%s %s", errorMessage, isLangKorean ? "(유효하지 않은 scope)" : "(Invalid scope)");
                            throw new InternalAuthenticationServiceException(errorMessage, new InvalidScopeException(errorMessage));
                        }
                    }
                });
    }

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

    // endregion
}