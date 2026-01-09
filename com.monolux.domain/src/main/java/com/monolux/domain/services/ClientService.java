package com.monolux.domain.services;

import com.monolux.domain.entities.ClientSignInLog;
import com.monolux.domain.repositories.ClientSignInLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ClientService extends BaseService {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final ClientSignInLogRepository clientSignInLogRepository;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    public ClientService(final ClientSignInLogRepository clientSignInLogRepository) {
        this.clientSignInLogRepository = clientSignInLogRepository;
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public void createClientSignInLog(final String clientId,
                                      final String ip,
                                      final boolean success,
                                      final String message) {
        boolean isLangKorean = LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
        this.clientSignInLogRepository.save(new ClientSignInLog(clientId, ip, success, success ? isLangKorean ? "성공" : "Success" : message));
    }

    // endregion
}