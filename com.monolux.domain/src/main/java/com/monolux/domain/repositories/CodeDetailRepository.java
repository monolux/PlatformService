package com.monolux.domain.repositories;

import com.monolux.domain.entities.CodeDetail;
import com.monolux.domain.entities.embeddables.id.CodeDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeDetailRepository extends JpaRepository<CodeDetail, CodeDetailId>, CodeDetailRepositoryCustom {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒



    // endregion
}