package com.monolux.domain.repositories;

import com.monolux.domain.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public UserRepositoryCustomImpl() {
        super(User.class);
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒



    // endregion
}