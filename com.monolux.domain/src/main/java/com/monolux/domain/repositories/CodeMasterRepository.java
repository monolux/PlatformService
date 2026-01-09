package com.monolux.domain.repositories;

import com.monolux.domain.entities.CodeMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeMasterRepository extends JpaRepository<CodeMaster, String>, CodeMasterRepositoryCustom {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒



    // endregion
}