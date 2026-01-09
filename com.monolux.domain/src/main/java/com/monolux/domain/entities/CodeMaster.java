package com.monolux.domain.entities;

import com.monolux.domain.entities.listeners.EntityListener;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Entity
@Table(name = "code_masters")
@EntityListeners(EntityListener.class)
public class CodeMaster extends BaseEntity {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Id
    @ToString.Include(name = "codeMasterId", rank = 1)
    @Column(name = "code_master_id", unique = true, nullable = false, updatable = false, length = 36)
    private String codeMasterId;

    @ToString.Include(name = "codeMasterName", rank = 2)
    @Column(name = "code_master_name", nullable = false, length = 50, columnDefinition = "nvarchar")
    private String codeMasterName;

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

    @OneToMany(mappedBy = "codeDetailId.codeMasterId", fetch = FetchType.EAGER)
    private final List<CodeDetail> codeDetails = new ArrayList<>();

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public CodeMaster(final String codeMasterId,
                      final String codeMasterName,
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
        this.codeMasterId = codeMasterId;
        this.codeMasterName = codeMasterName;
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