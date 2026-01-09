package com.monolux.authorization.oauth2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Slf4j
@Getter
public class UserDetails extends org.springframework.security.core.userdetails.User {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String userDisplayName;

    private final String userNick;

    private final boolean usingOtp;

    private final String otpSecret;

    private final String otpSecretTmp;

    private final Integer signInFailedCnt;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public UserDetails(final String username,
                       final String password,
                       final boolean enabled,
                       final boolean accountNonExpired,
                       final boolean credentialsNonExpired,
                       final boolean accountNonLocked,
                       final Collection<? extends GrantedAuthority> authorities,
                       final String userDisplayName,
                       final String userNick,
                       final Boolean usingOtp,
                       final String otpSecret,
                       final String otpSecretTmp,
                       final Integer signInFailedCnt) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userDisplayName = userDisplayName;
        this.userNick = userNick;
        this.usingOtp = usingOtp;
        this.otpSecret = otpSecret;
        this.otpSecretTmp = otpSecretTmp;
        this.signInFailedCnt = signInFailedCnt;
    }

    // endregion
}