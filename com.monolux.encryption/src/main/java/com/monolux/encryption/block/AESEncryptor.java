package com.monolux.encryption.block;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

@Slf4j
class AESEncryptor implements Encryptor {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    private final String GCM_AAD_DATA = "=,*JE5yFXz{w&v=,kkFNDRWd6CeJTL]h";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final AlgorithmModeCombination algorithmModeCombination;

    private final SecretKeySpec secretKeySpec;

    private final int AES_GCM_NONCE_LENGTH = 12;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public AESEncryptor(final AlgorithmModeCombination algorithmModeCombination, final String key) throws InvalidKeyException {
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
                case GCM -> {
                    byte[] nonce = EncryptionUtil.getRandomBytes(this.AES_GCM_NONCE_LENGTH);
                    cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec, new GCMParameterSpec(/*Tag Length Bit*/this.getBlockSize() * 8, nonce));
                    cipher.updateAAD(this.GCM_AAD_DATA.getBytes(this.getCharset()));
                    encrypted = cipher.doFinal(source);
                    encrypted = EncryptionUtil.getMergeBytes(nonce, encrypted);
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
                case GCM -> {
                    if (source.length < AES_GCM_NONCE_LENGTH) {
                        return null;
                    }
                    byte[] nonce = EncryptionUtil.getSliceBytes(source, 0, this.AES_GCM_NONCE_LENGTH);
                    if (nonce == null) {
                        return null;
                    }
                    cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, new GCMParameterSpec(/*Bit*/this.getBlockSize() * 8, nonce));
                    cipher.updateAAD(this.GCM_AAD_DATA.getBytes(this.getCharset()));
                    decrypted = EncryptionUtil.getSliceBytes(source, this.AES_GCM_NONCE_LENGTH, source.length - this.AES_GCM_NONCE_LENGTH);
                    decrypted = cipher.doFinal(decrypted);
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