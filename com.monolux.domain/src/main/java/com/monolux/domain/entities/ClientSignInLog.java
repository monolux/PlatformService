package com.monolux.domain.entities;

import com.monolux.domain.entities.embeddables.IpInfo;
import com.monolux.domain.entities.listeners.EntityListener;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Entity
@Table(name = "client_signin_logs")
@EntityListeners(EntityListener.class)
public class ClientSignInLog extends BaseEntity {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Id
    @ToString.Include(name = "seq", rank = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, updatable = false)
    private Long seq;

    @ToString.Include(name = "clientId", rank = 2)
    @Column(name = "client_id", nullable = false, length = 64, columnDefinition = "nvarchar")
    private String clientId;

    @ToString.Include(name = "signInIpInfo", rank = 3)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ip", column = @Column(name = "sign_in_ip", length = 15, nullable = false)),
            @AttributeOverride(name = "ipVal", column = @Column(name = "sign_in_ip_val", nullable = false))
    })
    private IpInfo signInIpInfo;

    @ToString.Include(name = "is_success", rank = 4)
    @Column(name = "is_success", nullable = false)
    private Boolean is_success;

    @ToString.Include(name = "message", rank = 5)
    @Column(name = "message", length = 256, columnDefinition = "nvarchar")
    private String message;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public ClientSignInLog(final String clientId,
                           final String signInIp,
                           final Boolean is_success,
                           final String message) {
        this.clientId = clientId;
        this.signInIpInfo = new IpInfo(signInIp);
        this.is_success = is_success;
        this.message = message;
    }

    // endregion
}