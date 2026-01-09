package com.monolux.domain.entities.embeddables;

import com.monolux.domain.exceptions.DomainIllegalArgumentException;
import com.monolux.utils.IpV4Util;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Embeddable
public class IpInfo {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @ToString.Include(name = "ip", rank = 1)
    @Column(nullable = false, length = 15)
    private String ip;

    @ToString.Include(name = "ipVal", rank = 2)
    @Column(nullable = false)
    private Long ipVal;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public IpInfo(final String ip) {
        if (!IpV4Util.isValidIpAddress(ip)) {
            throw new DomainIllegalArgumentException("IP address is not valid.");
        }

        this.ip = ip;
        this.ipVal = IpV4Util.ipToLong(ip);
    }

    // endregion
}