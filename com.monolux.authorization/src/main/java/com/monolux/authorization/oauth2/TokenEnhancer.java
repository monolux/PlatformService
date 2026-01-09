package com.monolux.authorization.oauth2;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.Serializable;
import java.util.Map;

@Slf4j
public class TokenEnhancer implements org.springframework.security.oauth2.provider.token.TokenEnhancer {
    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken defaultOAuth2AccessToken) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                defaultOAuth2AccessToken
                        .setAdditionalInformation(Map.ofEntries(Map.entry(AdditionalUserInfo.PROPERTY_NAME, AdditionalUserInfo.builder()
                                .userName(userDetails.getUsername())
                                .userDisplayName(userDetails.getUserDisplayName())
                                .userNick(userDetails.getUserNick())
                                .build())));
            }
        }

        return accessToken;
    }

    // endregion

    // region ▒▒▒▒▒ Inner Classes ▒▒▒▒▒

    record AdditionalUserInfo(String userName, String userDisplayName, String userNick) implements Serializable {
        // region ▒▒▒▒▒ Constants ▒▒▒▒▒

        static final String PROPERTY_NAME = "userInfoExt";

        // endregion

        // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

        @Builder
        AdditionalUserInfo {

        }

        // endregion
    }

    // endregion
}