package com.monolux.domain.services;

import com.monolux.common.constants.oauth2.OAuth2GrantType;
import com.monolux.common.constants.oauth2.OAuth2Errors;
import com.monolux.domain.entities.User;
import com.monolux.domain.entities.UserIpConfirm;
import com.monolux.domain.entities.UserSignInLog;
import com.monolux.domain.enumerations.Gender;
import com.monolux.domain.exceptions.DomainEntityExistsException;
import com.monolux.domain.exceptions.DomainEntityNotFoundException;
import com.monolux.domain.exceptions.DomainIllegalArgumentException;
import com.monolux.domain.repositories.UserIpConfirmRepository;
import com.monolux.domain.repositories.UserRepository;
import com.monolux.domain.repositories.UserSignInLogRepository;
import com.monolux.utils.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService extends BaseService {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final UserRepository userRepository;

    private final UserSignInLogRepository userSignInLogRepository;

    private final UserIpConfirmRepository userIpConfirmRepository;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    UserService(final UserRepository userRepository,
                final UserSignInLogRepository userSignInLogRepository,
                final UserIpConfirmRepository userIpConfirmRepository) {
        this.userRepository = userRepository;
        this.userSignInLogRepository = userSignInLogRepository;
        this.userIpConfirmRepository = userIpConfirmRepository;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public User createUser(final String userId,
                           final String userPwd,
                           final String userName,
                           final String userNick,
                           final String userProfileImg,
                           final String mobileNo,
                           final String mail,
                           final Integer yearOfBirth,
                           final Byte monthOfBirth,
                           final Byte dayOfBirth,
                           final Gender gender,
                           final Boolean isForeigner,
                           final String signUpIp) {

        if (this.userRepository.existsById(userId)) {
            throw new DomainEntityExistsException("There is a duplicate userId.");
        } else if (Optional.ofNullable(userNick)
                .filter(value -> !value.isEmpty() && !value.isBlank())
                .isPresent() && this.userRepository.existsByUserNick(userNick)) {
            throw new DomainIllegalArgumentException("There is a duplicate UserNick.");
        } else if (this.userRepository.existsByMobileNo(mobileNo)) {
            throw new DomainIllegalArgumentException("There is a duplicate UserMobileNo.");
        } else if (this.userRepository.existsByMail(mail)) {
            throw new DomainIllegalArgumentException("There is a duplicate UserMail.");
        }

        User user = User.builder()
                .userId(userId)
                .userPwd(userPwd)
                .userName(userName)
                .userNick(userNick)
                .userProfileImg(userProfileImg)
                .mobileNo(mobileNo)
                .mail(mail)
                .yearOfBirth(yearOfBirth)
                .monthOfBirth(monthOfBirth)
                .dayOfBirth(dayOfBirth)
                .gender(gender)
                .isForeigner(isForeigner)
                .signUpIp(signUpIp)
                .build();

        this.userRepository.save(user);

        return user;
    }

    public Optional<User> findUserById(final String userId) {
        return this.userRepository.findById(userId);
    }

    public User changeUserPwd(final String userId, final String userPwdNew) {
        return this.changeUserPwd(userId, userPwdNew, false);
    }

    public User changeUserPwd(final String userId, final String userPwdNew, final Boolean userPwdInitialized) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(DomainEntityNotFoundException::new);
        return this.userRepository.save(user.changeUserPwd(userPwdNew, userPwdInitialized));
    }

    public void signInSuccess(final String userId, final String clientId, final String grantType, final String ip) {
        this.userRepository.save(this.userRepository.findById(userId)
                .orElseThrow(DomainEntityNotFoundException::new)
                .signInSuccess());
        this.userSignInLogRepository.save(new UserSignInLog(userId, clientId, grantType, ip, true, LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage()) ? "성공" : "Success"));
    }

    public void signInFailed(final String userId, final String clientId, final String grantType, final String ip, final String message) {
        this.userRepository.findById(userId)
                .map(user -> this.userRepository.save(user.signInFailed()));
        this.userSignInLogRepository.save(new UserSignInLog(userId, clientId, grantType, ip, false, message));
    }

    public boolean existsUserIpConfirm(final String userId, final String ip) {
        return this.userIpConfirmRepository.existsByUser_UserIdAndIpInfo_IpAndConfirmed(userId, ip, true);
    }

    public Optional<UserIpConfirm> createUserIpConfirm(final String userId, final String ip, final String clientId, final String redirectUri) {
        return this.userRepository.findById(userId)
                .map(user -> this.userIpConfirmRepository
                        .save(this.userIpConfirmRepository
                                .findByUser_UserIdAndIpInfo_IpAndConfirmed(userId, ip, false)
                                .map(userIpConfirm -> {
                                    userIpConfirm.setClientId(clientId);
                                    userIpConfirm.setRedirectUri(redirectUri);
                                    return userIpConfirm;
                                }).orElse(new UserIpConfirm(user, ip, clientId, redirectUri))));
    }

    public Optional<UserIpConfirm> validateUserIpConfirmUsingOtpLogin(final String otpId, final String otpPassword) {
        return this.userIpConfirmRepository.findById(otpId)
                .filter(userIpConfirm -> {
                    if (OtpUtil.checkOtpValue(userIpConfirm.getUser().getOtpSecret(), otpPassword)) {
                        userIpConfirm.setConfirmed(true);
                        this.signInSuccess(userIpConfirm.getUser().getUserId(), userIpConfirm.getClientId(), OAuth2GrantType.AUTHORIZATION_CODE, userIpConfirm.getIpInfo().getIp());
                        return true;
                    } else {
                        String message = OAuth2Errors.BAD_CREDENTIAL.getErrorMessage();
                        if (LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage())) {
                            message = String.format("%s (OTP 패스워드 오류)", message);
                        } else {
                            message = String.format("%s (Invalid OTP password)", message);
                        }
                        this.signInFailed(userIpConfirm.getUser().getUserId(), userIpConfirm.getClientId(), OAuth2GrantType.AUTHORIZATION_CODE, userIpConfirm.getIpInfo().getIp(), message);
                        return false;
                    }
                }).map(this.userIpConfirmRepository::save);
    }

    public Optional<User> createUserTempOtpSecret(final String userId) {
        return this.userRepository.findById(userId)
                .map(user -> {
                    user.setOtpSecretTmp(OtpUtil.createOtpSecret());
                    return this.userRepository.save(user);
                });
    }

    public Optional<User> confirmUserTempOtpSecret(final String userId, final String otpValue) {
        return this.userRepository.findById(userId)
                .filter(user -> user.confirmOtpSecretTemp(otpValue))
                .map(this.userRepository::save);
    }

    // endregion
}