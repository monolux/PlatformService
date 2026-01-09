package com.monolux.encryption.block;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class EncryptorFactory {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Value("${encryption.aes_key}")
    private String AES_KEY;

    @Value("${encryption.des_key}")
    private String DES_KEY;

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public Encryptor create(final AlgorithmModeCombination algorithmModeCombination) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmModeCombination.getAlgorithm().equals(Algorithm.AES)) {
            return this.create(algorithmModeCombination, this.AES_KEY);
        } else if (algorithmModeCombination.getAlgorithm().equals(Algorithm.DES) ||
                algorithmModeCombination.getAlgorithm().equals(Algorithm.DESede)) {
            return this.create(algorithmModeCombination, this.DES_KEY);
        } else {
            throw new InvalidAlgorithmParameterException();
        }
    }

    protected Encryptor create(final AlgorithmModeCombination algorithmModeCombination, final String key) throws InvalidKeyException {
        if (key == null || key.isEmpty()) {
            throw new InvalidKeyException("Please Check Secret Key");
        }

        if (algorithmModeCombination.getAlgorithm().equals(Algorithm.AES)) {
            return new AESEncryptor(algorithmModeCombination, key);
        } else if (algorithmModeCombination.getAlgorithm().equals(Algorithm.DES) || algorithmModeCombination.getAlgorithm().equals(Algorithm.DESede)) {
            return new DESEncryptor(algorithmModeCombination, key);
        } else {
            return null;
        }
    }

    // endregion
}