package com.monolux.domain.entities;

import com.monolux.domain.entities.embeddables.IpInfo;
import com.monolux.domain.entities.listeners.EntityListener;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Entity
@Table(name = "user_signin_logs")
@EntityListeners(EntityListener.class)
public class UserSignInLog extends BaseEntity {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Id
    @ToString.Include(name = "seq", rank = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, updatable = false)
    private Long seq;

    @ToString.Include(name = "userId", rank = 2)
    @Column(name = "user_id", nullable = false, updatable = false, length = 64)
    private String userId;

    @ToString.Include(name = "clientId", rank = 3)
    @Column(name = "client_id", nullable = false, length = 64, columnDefinition = "nvarchar")
    private String clientId;

    @ToString.Include(name = "grantType", rank = 4)
    @Column(name = "grant_type", nullable = false, length = 32, columnDefinition = "nvarchar")
    private String grantType;

    @ToString.Include(name = "signInIpInfo", rank = 5)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ip", column = @Column(name = "sign_in_ip", length = 15, nullable = false)),
            @AttributeOverride(name = "ipVal", column = @Column(name = "sign_in_ip_val", nullable = false))
    })
    private IpInfo signInIpInfo;

    @ToString.Include(name = "is_success", rank = 6)
    @Column(name = "is_success", nullable = false)
    private Boolean is_success;

    @ToString.Include(name = "message", rank = 7)
    @Column(name = "message", length = 256, columnDefinition = "nvarchar")
    private String message;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public UserSignInLog(final String userId,
                         final String clientId,
                         final String grantType,
                         final String signInIp,
                         final Boolean is_success,
                         final String message) {
        this.userId = userId;
        this.clientId = clientId;
        this.grantType = grantType;
        this.signInIpInfo = new IpInfo(signInIp);
        this.is_success = is_success;
        this.message = message;
    }

    // endregion
}