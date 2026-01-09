package com.monolux.domain.entities;

import com.monolux.domain.entities.embeddables.IpInfo;
import com.monolux.domain.entities.listeners.EntityListener;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Entity
@Table(name = "user_ip_confirms")
@EntityListeners(EntityListener.class)
public class UserIpConfirm extends BaseEntity {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @ToString.Include(name = "id", rank = 1)
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ToString.Include(name = "ipInfo", rank = 2)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ip", column = @Column(name = "ip", length = 15, nullable = false, updatable = false)),
            @AttributeOverride(name = "ipVal", column = @Column(name = "ip_val", nullable = false, updatable = false))
    })
    private IpInfo ipInfo;

    @Setter
    @ToString.Include(name = "clientId", rank = 3)
    @Column(name = "client_id", length = 64, columnDefinition = "nvarchar")
    private String clientId;

    @Setter
    @ToString.Include(name = "redirectUri", rank = 4)
    @Column(name = "redirect_uri", length = 512, columnDefinition = "nvarchar")
    private String redirectUri;

    @Setter
    @ToString.Include(name = "confirmed", rank = 5)
    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public UserIpConfirm(final User user, final String ip, final String clientId, final String redirectUri) {
        this.user = user;
        this.ipInfo = new IpInfo(ip);
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.confirmed = false;
    }

    // endregion
}