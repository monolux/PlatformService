package com.monolux.authorization.oauth2;

import com.monolux.domain.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service(UserDetailsService.BEAN_NAME_USER_DETAILS_SERVICE)
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_USER_DETAILS_SERVICE = "userDetailsServiceCustom";

    public final static Integer SIGN_IN_FAILED_LIMIT = 5;

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final UserService userService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    public UserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
        com.monolux.domain.entities.User user = this.userService.findUserById(userId)
                .orElseThrow(() -> {
                    boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
                    String message = isLangKorean ? "사용자 '%s' 을(를) 찾을 수 없습니다." : "User '%s' not found.";
                    message = String.format(message, userId);
                    return new UsernameNotFoundException(message);
                });

        return new UserDetails(user.getUserId(),
                user.getUserPwd(),
                user.isEnabled(),
                user.isNonExpired(),
                user.isCredentialsNonExpired(),
                user.isNonLocked(),
                user.getAuthorities(),
                user.getUserName(),
                user.getUserNick(),
                user.getUsingOtp(),
                user.getOtpSecret(),
                user.getOtpSecretTmp(),
                user.getSignInFailedCnt());
    }

    // endregion
}