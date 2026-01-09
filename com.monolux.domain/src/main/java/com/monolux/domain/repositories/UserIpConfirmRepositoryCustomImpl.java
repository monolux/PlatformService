package com.monolux.domain.repositories;

import com.monolux.domain.entities.UserIpConfirm;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserIpConfirmRepositoryCustomImpl extends QuerydslRepositorySupport implements UserIpConfirmRepositoryCustom {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public UserIpConfirmRepositoryCustomImpl() {
        super(UserIpConfirm.class);
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒



    // endregion
}