package com.monolux.domain.repositories;

import com.monolux.domain.entities.CodeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class CodeDetailRepositoryCustomImpl extends QuerydslRepositorySupport implements CodeDetailRepositoryCustom {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public CodeDetailRepositoryCustomImpl() {
        super(CodeDetail.class);
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒



    // endregion
}