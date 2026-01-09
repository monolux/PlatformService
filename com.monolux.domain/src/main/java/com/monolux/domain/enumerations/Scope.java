package com.monolux.domain.enumerations;

import lombok.Getter;

@Getter
public enum Scope {
    // region ▒▒▒▒▒ Enumerations ▒▒▒▒▒

    CONTENTS_CREATE  ("contents.create", "contents.create"),

    CONTENTS_READ    ("contents.read", "contents.read"),

    CONTENTS_UPDATE  ("contents.update", "contents.update"),

    CONTENTS_DELETE  ("contents.delete", "contents.delete"),

    CONTENTS_ALL     ("contents.all", "contents.all");

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final String value;

    private final String desc;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    Scope(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    // endregion
}