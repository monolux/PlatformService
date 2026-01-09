package com.monolux.domain.repositories;

import com.monolux.domain.entities.CodeMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class CodeMasterRepositoryCustomImpl extends QuerydslRepositorySupport implements CodeMasterRepositoryCustom {
    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public CodeMasterRepositoryCustomImpl() {
        super(CodeMaster.class);
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒



    // endregion
}
