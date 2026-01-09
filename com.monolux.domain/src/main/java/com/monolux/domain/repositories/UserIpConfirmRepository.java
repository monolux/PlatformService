package com.monolux.domain.repositories;

import com.monolux.domain.entities.UserIpConfirm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserIpConfirmRepository extends JpaRepository<UserIpConfirm, String>, UserIpConfirmRepositoryCustom {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒

    boolean existsByUser_UserIdAndIpInfo_IpAndConfirmed(String userId, String userIp, Boolean confirmed);

    Optional<UserIpConfirm> findByUser_UserIdAndIpInfo_IpAndConfirmed(String userId, String userIp, Boolean confirmed);

    // endregion
}