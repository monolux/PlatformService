package com.monolux.encryption.block;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

@Slf4j
class DESEncryptor implements Encryptor {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final AlgorithmModeCombination algorithmModeCombination;

    private final SecretKeySpec secretKeySpec;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public DESEncryptor(final AlgorithmModeCombination algorithmModeCombination, final String key) throws InvalidKeyException {
        if (key == null || !algorithmModeCombination.getAlgorithm().getAllowKeyBytesLength().contains(key.getBytes(this.getCharset()).length)) {
            throw new InvalidKeyException("Please Check Secret Key");
        }

        this.algorithmModeCombination = algorithmModeCombination;
        this.secretKeySpec = new SecretKeySpec(key.getBytes(this.getCharset()), this.getAlgorithm().getCode());
    }

    // endregion

    // region ▒▒▒▒▒ Interface Implements ▒▒▒▒▒

    @Override
    public String encrypt(final String value) {
        if (value == null) {
            return null;
        }

        byte[] source = value.getBytes(this.getCharset());
        byte[] encrypted;
        Cipher cipher = this.algorithmModeCombination.getCipher();

        try {
            switch (this.getMode()) {
                case CBC, CFB, CTR, OFB -> {
                    byte[] initVector = EncryptionUtil.getRandomBytes(this.getBlockSize());
                    cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec, new IvParameterSpec(initVector));
                    encrypted = cipher.doFinal(source);
                    encrypted = EncryptionUtil.getMergeBytes(initVector, encrypted);
                    return new String(Base64.encodeBase64(encrypted), this.getCharset());
                }
                case ECB -> {
                    cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec);
                    encrypted = cipher.doFinal(source);
                    return new String(Base64.encodeBase64(encrypted), this.getCharset());
                }
                default -> {
                    return null;
                }
            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException ex) {
            return null;
        }
    }

    @Override
    public String decrypt(final String value) {
        try {
            if (value == null) {
                return null;
            }

            byte[] source = Base64.decodeBase64(value.getBytes(this.getCharset()));
            byte[] decrypted;
            Cipher cipher = this.algorithmModeCombination.getCipher();

            switch (this.getMode()) {
                case CBC, CFB, CTR, OFB -> {
                    if (source.length < this.getBlockSize()) {
                        return null;
                    }
                    byte[] initVector = EncryptionUtil.getSliceBytes(source, 0, this.getBlockSize());
                    if (initVector == null) {
                        return null;
                    }
                    cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, new IvParameterSpec(initVector));
                    decrypted = EncryptionUtil.getSliceBytes(source, this.getBlockSize(), source.length - this.getBlockSize());
                    decrypted = cipher.doFinal(decrypted);
                    return new String(decrypted, this.getCharset());
                }
                case ECB -> {
                    cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec);
                    decrypted = cipher.doFinal(source);
                    return new String(decrypted, this.getCharset());
                }
                default -> {
                    return null;
                }
            }
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException ignore) {
            return null;
        }
    }

    @Override
    public AlgorithmModeCombination algorithmModeCombination() {
        return this.algorithmModeCombination;
    }

    // endregion
}