package com.monolux.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @ToString.Include(name = "created", rank = 1)
    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    private java.time.LocalDateTime created;

    @ToString.Include(name = "createdBy", rank = 2)
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 64)
    private String createdBy;

    @ToString.Include(name = "modified", rank = 3)
    @LastModifiedDate
    @Column(name = "modified")
    private java.time.LocalDateTime modified;

    @ToString.Include(name = "modifiedBy", rank = 4)
    @LastModifiedBy
    @Column(name = "modified_by", length = 64)
    private String modifiedBy;

    // endregion
}