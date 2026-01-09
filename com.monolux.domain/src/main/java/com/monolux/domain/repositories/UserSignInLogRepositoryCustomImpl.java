package com.monolux.domain.repositories;

import com.monolux.domain.entities.UserSignInLog;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserSignInLogRepositoryCustomImpl extends QuerydslRepositorySupport implements UserSignInLogRepositoryCustom {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public UserSignInLogRepositoryCustomImpl() {
        super(UserSignInLog.class);
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒



    // endregion
}