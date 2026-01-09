package com.monolux.domain.entities;

import com.monolux.domain.entities.embeddables.DateInfo;
import com.monolux.domain.entities.embeddables.IpInfo;
import com.monolux.domain.entities.listeners.EntityListener;
import com.monolux.domain.enumerations.Gender;
import com.monolux.domain.enumerations.Authorities;
import com.monolux.domain.enumerations.AuthorityType;
import com.monolux.utils.OtpUtil;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Entity
@Table(name = "users")
@EntityListeners(EntityListener.class)
public class User extends BaseEntity {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Id
    @ToString.Include(name = "userId", rank = 1)
    @Column(name = "user_id", unique = true, nullable = false, updatable = false, length = 64)
    private String userId;

    @Column(name = "user_pwd", nullable = false, length = 128)
    private String userPwd;

    @Column(name = "user_pwd_initialized", nullable = false)
    private Boolean userPwdInitialized;

    @Column(name = "using_otp", nullable = false)
    private Boolean usingOtp;

    @Column(name = "otp_secret", nullable = false, length = 64, columnDefinition = "varchar")
    private String otpSecret;

    @Setter
    @Column(name = "otp_secret_tmp", nullable = false, length = 64, columnDefinition = "varchar")
    private String otpSecretTmp;

    @ToString.Include(name = "userName", rank = 2)
    @Column(name = "user_name", nullable = false, length = 64, columnDefinition = "nvarchar")
    @Nationalized
    private String userName;

    @ToString.Include(name = "userNick", rank = 3)
    @Column(name = "user_nick", length = 64, columnDefinition = "nvarchar")
    @Nationalized
    private String userNick;

    @ToString.Include(name = "userProfileImg", rank = 4)
    @Column(name = "user_profile_img", length = 512)
    private String userProfileImg;

    @ToString.Include(name = "mobileNo", rank = 5)
    @Column(name = "mobile_no", nullable = false, length = 64)
    private String mobileNo;

    @ToString.Include(name = "mail", rank = 6)
    @Column(name = "mail", nullable = false, length = 512)
    private String mail;

    @Column(name = "mail_verified")
    private LocalDateTime mailVerified;

    @ToString.Include(name = "dateOfBirth", rank = 7)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "year", column = @Column(name = "year_of_birth", nullable = false)),
            @AttributeOverride(name = "month", column = @Column(name = "month_of_birth", nullable = false)),
            @AttributeOverride(name = "dayOfMonth", column = @Column(name = "day_of_birth", nullable = false))
    })
    private DateInfo dateOfBirth;

    @ToString.Include(name = "gender", rank = 8)
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 8)
    private Gender gender;

    @ToString.Include(name = "isForeigner", rank = 9)
    @Column(name = "is_foreigner", nullable = false)
    private Boolean isForeigner;

    @ToString.Include(name = "signUpIpInfo", rank = 10)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ip", column = @Column(name = "sign_up_ip", length = 15, nullable = false)),
            @AttributeOverride(name = "ipVal", column = @Column(name = "sign_up_ip_val", nullable = false))
    })
    private IpInfo signUpIpInfo;

    @ToString.Include(name = "signInLast", rank = 11)
    @Column(name = "sign_in_last")
    private LocalDateTime signInLast;

    @ToString.Include(name = "signInFailedCnt", rank = 12)
    @Column(name = "sign_in_failed_cnt", nullable = false)
    private Integer signInFailedCnt;

    @ToString.Include(name = "locked", rank = 13)
    @Column(name = "locked")
    private LocalDateTime locked;

    @ToString.Include(name = "lockedBy", rank = 14)
    @Column(name = "locked_by", length = 64)
    private String lockedBy;

    @ToString.Include(name = "deleted", rank = 15)
    @Column(name = "deleted")
    private LocalDateTime deleted;

    @ToString.Include(name = "deletedBy", rank = 16)
    @Column(name = "deleted_by", length = 64)
    private String deletedBy;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final List<UserIpConfirm> userIpConfirms = new ArrayList<>();

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public User(final String userId,
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
        this.userId = userId;
        this.userPwd = userPwd;
        this.userPwdInitialized = false;
        this.usingOtp = false;
        this.otpSecret = null;
        this.otpSecretTmp = null;
        this.userName = userName;
        this.userNick = userNick;
        this.userProfileImg = userProfileImg;
        this.mobileNo = mobileNo;
        this.mail = mail;
        this.mailVerified = null;
        this.dateOfBirth = new DateInfo(yearOfBirth, monthOfBirth, dayOfBirth);
        this.gender = gender;
        this.isForeigner = isForeigner;
        this.signUpIpInfo = new IpInfo(signUpIp);
        this.signInLast = null;
        this.signInFailedCnt = 0;
        this.locked = null;
        this.lockedBy = null;
        this.deleted = null;
        this.deletedBy = null;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public Boolean isEnabled() {
        LocalDateTime now = LocalDateTime.now();

        return Optional.ofNullable(this.deleted)
                .filter(value -> value.isEqual(now) || value.isBefore(now))
                .isEmpty();
    }

    public Boolean isNonExpired() {
        return this.isEnabled();
    }

    public Boolean isCredentialsNonExpired() {
        return this.isEnabled() && this.isNonExpired();
    }

    public Boolean isNonLocked() {
        LocalDateTime now = LocalDateTime.now();

        return this.isEnabled() && this.isNonExpired() && Optional.ofNullable(this.locked)
                .filter(value -> value.isEqual(now) || value.isBefore(now))
                .isEmpty();
    }

    /**
     * Get User Authorities
     * @return : Authorities
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO : 추후에 Authority 가 추가 되면 이 곳에 작성. 현재는 일반 유저 밖에 없는 상황.
        return Arrays.stream(Authorities.values())
                .filter(authorities -> authorities.getAuthorityType().equals(AuthorityType.USER))
                .map(authorities -> new SimpleGrantedAuthority(authorities.getCode())).toList();
    }

    public User changeUserPwd(final String userPwdNew, final Boolean userPwdInitialized) {
        this.userPwd = userPwdNew;
        this.userPwdInitialized = userPwdInitialized;
        this.signInFailedCnt = 0;
        return this;
    }

    public User signInSuccess() {
        this.signInLast = LocalDateTime.now();
        this.signInFailedCnt = 0;
        return this;
    }

    public User signInFailed() {
        this.signInFailedCnt++;
        return this;
    }

    public boolean confirmOtpSecretTemp(final String otpValue) {
        if (OtpUtil.checkOtpValue(this.otpSecretTmp, otpValue)) {
            this.otpSecret = this.otpSecretTmp;
            this.usingOtp = true;
            return true;
        }

        return false;
    }

    // endregion
}