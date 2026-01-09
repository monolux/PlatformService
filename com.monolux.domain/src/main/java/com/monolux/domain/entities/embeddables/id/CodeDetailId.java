package com.monolux.domain.entities.embeddables.id;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Embeddable
public class CodeDetailId implements Serializable {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @ToString.Include(name = "codeMasterId", rank = 1)
    @Column(nullable = false, updatable = false, length = 36)
    private String codeMasterId;

    @ToString.Include(name = "codeDetailId", rank = 2)
    @Column(nullable = false, updatable = false, length = 36)
    private String codeDetailId;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public CodeDetailId(final String codeMasterId, final String codeDetailId) {
        this.codeMasterId = codeMasterId;
        this.codeDetailId = codeDetailId;
    }

    // endregion

    // region ▒▒▒▒▒ Override Methods ▒▒▒▒▒

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof CodeDetailId other) {
            return this.codeMasterId.equals(other.codeMasterId) && this.codeDetailId.equals(other.codeDetailId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.codeMasterId, this.codeDetailId);
    }

    // endregion
}