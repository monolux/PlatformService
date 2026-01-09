package com.monolux.encryption.block;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Encryptor {
    // region ▒▒▒▒▒ Interfaces ▒▒▒▒▒

    String encrypt(String value);

    String decrypt(String value);

    AlgorithmModeCombination algorithmModeCombination();

    default Algorithm getAlgorithm() {
        return this.algorithmModeCombination().getAlgorithm();
    }

    default Mode getMode() {
        return this.algorithmModeCombination().getMode();
    }

    default int getBlockSize() {
        return this.getAlgorithm().getBlockBytesLength();
    }

    /**
     * Charset Fixed (UTF-8)
     * @return Charset (UTF-8)
     */
    default Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    // endregion
}