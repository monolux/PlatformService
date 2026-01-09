package com.monolux.domain.repositories;

import com.monolux.domain.entities.ClientSignInLog;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ClientSignInLogRepositoryCustomImpl extends QuerydslRepositorySupport implements ClientSignInLogRepositoryCustom {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public ClientSignInLogRepositoryCustomImpl() {
        super(ClientSignInLog.class);
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒



    // endregion
}