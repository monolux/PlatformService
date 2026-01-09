package com.monolux.encryption.block;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

@Getter
public class AlgorithmModeCombination {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final Algorithm algorithm;

    private final Mode mode;

    private final Cipher cipher;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public AlgorithmModeCombination(final Algorithm algorithm, final Mode mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.algorithm = algorithm;
        this.mode = mode;

        if (!algorithm.getAllowModes().contains(mode)) {
            throw new IllegalArgumentException();
        }

        this.cipher = Cipher.getInstance(String.format("%s/%s/%s", algorithm.getCode(), mode.getCode(), mode.getPadding()));
    }

    // endregion
}