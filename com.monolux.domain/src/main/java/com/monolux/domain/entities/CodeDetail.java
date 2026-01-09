package com.monolux.domain.entities;

import com.monolux.domain.entities.embeddables.id.CodeDetailId;
import com.monolux.domain.entities.listeners.EntityListener;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Entity
@Table(name = "code_details")
@EntityListeners(EntityListener.class)
public class CodeDetail extends BaseEntity {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @ToString.Include(name = "codeDetailId", rank = 1)
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codeMasterId", column = @Column(name = "code_master_id", nullable = false, updatable = false, length = 36)),
            @AttributeOverride(name = "codeDetailId", column = @Column(name = "code_detail_id", nullable = false, updatable = false, length = 36))
    })
    private CodeDetailId codeDetailId;

    @MapsId("codeMasterId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = CodeMaster.class)
    @JoinColumns({@JoinColumn(name = "code_master_id", nullable = false, updatable = false)})
    private CodeMaster codeMaster;

    @ToString.Include(name = "codeDetailName", rank = 2)
    @Column(name = "code_detail_name", nullable = false, length = 50, columnDefinition = "nvarchar")
    private String codeDetailName;

    @ToString.Include(name = "prop01", rank = 3)
    @Column(name = "attr_01", length = 200, columnDefinition = "nvarchar")
    private String attr01;

    @ToString.Include(name = "prop02", rank = 4)
    @Column(name = "attr_02", length = 200, columnDefinition = "nvarchar")
    private String attr02;

    @ToString.Include(name = "prop03", rank = 5)
    @Column(name = "attr_03", length = 200, columnDefinition = "nvarchar")
    private String attr03;

    @ToString.Include(name = "prop04", rank = 6)
    @Column(name = "attr_04", length = 200, columnDefinition = "nvarchar")
    private String attr04;

    @ToString.Include(name = "prop05", rank = 7)
    @Column(name = "attr_05", length = 200, columnDefinition = "nvarchar")
    private String attr05;

    @ToString.Include(name = "prop06", rank = 8)
    @Column(name = "attr_06", length = 200, columnDefinition = "nvarchar")
    private String attr06;

    @ToString.Include(name = "prop07", rank = 9)
    @Column(name = "attr_07", length = 200, columnDefinition = "nvarchar")
    private String attr07;

    @ToString.Include(name = "prop08", rank = 10)
    @Column(name = "attr_08", length = 200, columnDefinition = "nvarchar")
    private String attr08;

    @ToString.Include(name = "prop09", rank = 11)
    @Column(name = "attr_09", length = 200, columnDefinition = "nvarchar")
    private String attr09;

    @ToString.Include(name = "prop10", rank = 12)
    @Column(name = "attr_10", length = 200, columnDefinition = "nvarchar")
    private String attr10;

    @ToString.Include(name = "prop11", rank = 13)
    @Column(name = "attr_11", length = 200, columnDefinition = "nvarchar")
    private String attr11;

    @ToString.Include(name = "prop12", rank = 14)
    @Column(name = "attr_12", length = 200, columnDefinition = "nvarchar")
    private String attr12;

    @ToString.Include(name = "status", rank = 15)
    @Column(name = "status", nullable = false)
    private Boolean status;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public CodeDetail(final String codeMasterId,
                       final String codeDetailId,
                       final String codeDetailName,
                       final String attr01,
                       final String attr02,
                       final String attr03,
                       final String attr04,
                       final String attr05,
                       final String attr06,
                       final String attr07,
                       final String attr08,
                       final String attr09,
                       final String attr10,
                       final String attr11,
                       final String attr12) {
        this.codeDetailId = new CodeDetailId(codeMasterId, codeDetailId);
        this.codeDetailName = codeDetailName;
        this.attr01 = attr01;
        this.attr02 = attr02;
        this.attr03 = attr03;
        this.attr04 = attr04;
        this.attr05 = attr05;
        this.attr06 = attr06;
        this.attr07 = attr07;
        this.attr08 = attr08;
        this.attr09 = attr09;
        this.attr10 = attr10;
        this.attr11 = attr11;
        this.attr12 = attr12;
        this.status = true;
    }

    // endregion
}