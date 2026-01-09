package com.monolux.domain.services;

import com.monolux.domain.entities.CodeDetail;
import com.monolux.domain.entities.embeddables.id.CodeDetailId;
import com.monolux.domain.repositories.CodeDetailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodeDetailService extends BaseService {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final CodeDetailRepository codeDetailRepository;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    CodeDetailService(final CodeDetailRepository codeDetailRepository) {
        this.codeDetailRepository = codeDetailRepository;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public Optional<CodeDetail> findCodeDetailById(final String codeMasterId, final String codeDetailId) {
        return this.codeDetailRepository.findById(new CodeDetailId(codeMasterId, codeDetailId));
    }

    // endregion
}