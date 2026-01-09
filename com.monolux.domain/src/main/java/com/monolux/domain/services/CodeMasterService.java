package com.monolux.domain.services;

import com.monolux.domain.entities.CodeMaster;
import com.monolux.domain.exceptions.DomainEntityExistsException;
import com.monolux.domain.repositories.CodeMasterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodeMasterService extends BaseService {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final CodeMasterRepository codeMasterRepository;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    CodeMasterService(final CodeMasterRepository codeMasterRepository) {
        this.codeMasterRepository = codeMasterRepository;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public CodeMaster createCodeMaster(final String codeMasterId,
                                       final String codeMasterName,
                                       final String attr01,
                                       final String attr02,
                                       final String attr03,
                                       final String attr04,
                                       final String attr05,
                                       final String attr06,
                                       final String attr07,
                                       final String attr08,
                                       final String attr09,
                                       final String attr10,
                                       final String attr11,
                                       final String attr12) {
        if (this.codeMasterRepository.existsById(codeMasterId)) {
            throw new DomainEntityExistsException("There is a duplicate CodeMasterId.");
        }

        CodeMaster codeMaster = CodeMaster.builder()
                .codeMasterId(codeMasterId)
                .codeMasterName(codeMasterName)
                .attr01(attr01)
                .attr02(attr02)
                .attr03(attr03)
                .attr04(attr04)
                .attr05(attr05)
                .attr06(attr06)
                .attr07(attr07)
                .attr08(attr08)
                .attr09(attr09)
                .attr10(attr10)
                .attr11(attr11)
                .attr12(attr12)
                .build();

        this.codeMasterRepository.save(codeMaster);

        return codeMaster;
    }

    public Optional<CodeMaster> findCodeMasterById(final String codeMasterId) {
        return this.codeMasterRepository.findById(codeMasterId);
    }

    // endregion
}