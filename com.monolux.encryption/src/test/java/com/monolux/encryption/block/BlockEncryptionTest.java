package com.monolux.encryption.block;

import org.junit.jupiter.api.*;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@DisplayName("Block Encryption")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class BlockEncryptionTest {
    // region ▒▒▒▒▒ Tests ▒▒▒▒▒

    @Test
    @Order(1)
    @DisplayName("Test Block Encryption AlgorithmModeCombination")
    public void doTestInvlaidAlgorithmModeCombination() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AlgorithmModeCombination(Algorithm.DES, Mode.GCM));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AlgorithmModeCombination(Algorithm.DESede, Mode.GCM));
    }

    @Test
    @Order(2)
    @DisplayName("Test Block Encryption")
    void doTest() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        final String plainText1 = "This is test 1 (이것은 테스트 1 입니다.)";
        final String plainText2 = "This is test 2 (이것은 테스트 2 입니다.)";

        EncryptorFactory encryptorFactory = new EncryptorFactory();

        for(Algorithm algorithm : Algorithm.values()) {
            for (Mode mode : algorithm.getAllowModes()) {
                List<String> keys = algorithm.getAllowKeyBytesLength()
                        .stream()
                        .map(EncryptionUtil::getRandomString).toList();

                for (String key : keys) {
                    Encryptor encryptor = encryptorFactory.create(new AlgorithmModeCombination(algorithm, mode), key);

                    final String encrypted1 = encryptor.encrypt(plainText1);
                    final String encrypted2 = encryptor.encrypt(plainText2);

                    Assertions.assertNotNull(encrypted1);
                    Assertions.assertNotNull(encrypted2);
                    Assertions.assertNotEquals(encrypted1, encrypted2);

                    final String decrypted1 = encryptor.decrypt(encrypted1);
                    final String decrypted2 = encryptor.decrypt(encrypted2);

                    Assertions.assertNotNull(decrypted1);
                    Assertions.assertEquals(plainText1, decrypted1);
                    Assertions.assertNotNull(decrypted2);
                    Assertions.assertEquals(plainText2, decrypted2);
                }
            }
        }
    }

    // endregion
}