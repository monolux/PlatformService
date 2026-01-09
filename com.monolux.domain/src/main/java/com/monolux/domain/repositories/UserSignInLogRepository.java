package com.monolux.domain.repositories;

import com.monolux.domain.entities.UserSignInLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSignInLogRepository extends JpaRepository<UserSignInLog, Long>, UserSignInLogRepositoryCustom {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒



    // endregion
}