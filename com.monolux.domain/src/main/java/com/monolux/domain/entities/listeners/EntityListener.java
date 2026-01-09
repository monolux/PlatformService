package com.monolux.domain.entities.listeners;

import com.monolux.domain.entities.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@NoArgsConstructor
public class EntityListener {
    // region ▒▒▒▒▒ Persist ▒▒▒▒▒

    @PrePersist
    void onPrePersist(BaseEntity entity) {

    }

    @PostPersist
    void onPostPersist(BaseEntity entity) {

    }

    // endregion

    // region ▒▒▒▒▒ Load ▒▒▒▒▒

    @PostLoad
    void onPostLoad(final BaseEntity entity) {

    }

    // endregion

    // region ▒▒▒▒▒ Update ▒▒▒▒▒

    @PreUpdate
    void onPreUpdate(final BaseEntity entity) {

    }

    @PostUpdate
    void onPostUpdate(final BaseEntity entity) {

    }

    // endregion

    // region ▒▒▒▒▒ Remove ▒▒▒▒▒

    @PreRemove
    void onPreRemove(final BaseEntity entity) {

    }

    @PostRemove
    void onPostRemove(final BaseEntity entity) {

    }

    // endregion
}