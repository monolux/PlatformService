package com.monolux.encryption.block;

import lombok.Getter;

import java.util.List;

@Getter
public enum Algorithm {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    AES     ("AES", 16,
            List.of(/*AES-128 Bit*/16, /*AES-192 Bit*/24, /*AES-256 Bit*/32),
            List.of(Mode.CBC, Mode.CFB, Mode.CTR, Mode.ECB, Mode.GCM, Mode.OFB)),
    DES     ("DES", 8,
            List.of(8),
            List.of(Mode.CBC, Mode.CFB, Mode.CTR, Mode.ECB, Mode.OFB)),
    DESede  ("DESede", 8,
            List.of(24),
            List.of(Mode.CBC, Mode.CFB, Mode.CTR, Mode.ECB, Mode.OFB));                                                   // DESede (Triple DES)

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String code;

    private final int blockBytesLength;

    private final List<Integer> allowKeyBytesLength;

    private final List<Mode> allowModes;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    Algorithm(final String code, final int blockBytesLength, final List<Integer> allowKeyBytesLength, final List<Mode> allowModes) {
        this.code = code;
        this.blockBytesLength = blockBytesLength;
        this.allowKeyBytesLength = allowKeyBytesLength;
        this.allowModes = allowModes;
    }

    // endregion
}