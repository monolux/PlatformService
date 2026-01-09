package com.monolux.domain.repositories;

import com.monolux.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒

    Boolean existsByUserNick(final String userNick);

    Boolean existsByMobileNo(final String mobileNo);

    Boolean existsByMail(final String mail);

    // endregion
}