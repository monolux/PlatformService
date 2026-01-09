package com.monolux.encryption.block;

import lombok.Getter;

@Getter
public enum Mode {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    CBC     ("CBC", "PKCS5Padding"),    // Cipher Block Chaning Mode
    CFB     ("CFB", "NoPadding"),       // Cipher FeedBack Mode
    CTR     ("CTR", "NoPadding"),       // CounTeR Mode
    ECB     ("ECB", "PKCS5Padding"),    // Electric CodeBook Mode
    GCM     ("GCM", "NoPadding"),       // Galois/Counter Mode
    OFB     ("OFB", "NoPadding");       // Output Feedback Mode

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String code;

    private final String padding;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    Mode(final String code, final String padding) {
        this.code = code;
        this.padding = padding;
    }

    // endregion
}