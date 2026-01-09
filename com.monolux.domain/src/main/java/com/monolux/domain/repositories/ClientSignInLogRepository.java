package com.monolux.domain.repositories;

import com.monolux.domain.entities.ClientSignInLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientSignInLogRepository extends JpaRepository<ClientSignInLog, Long>, ClientSignInLogRepositoryCustom {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒



    // endregion
}