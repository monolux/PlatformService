package com.monolux.resource.dto.res;

import lombok.Builder;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.Collection;

@JsonPropertyOrder({
        "codeMasterId",
        "codeMasterName",
        "attr01",
        "attr02",
        "attr03",
        "attr04",
        "attr05",
        "attr06",
        "attr07",
        "attr08",
        "attr09",
        "attr10",
        "attr11",
        "attr12"})
@Builder
public record CodeMasterResInfo(String codeMasterId,
                                String codeMasterName,
                                String attr01,
                                String attr02,
                                String attr03,
                                String attr04,
                                String attr05,
                                String attr06,
                                String attr07,
                                String attr08,
                                String attr09,
                                String attr10,
                                String attr11,
                                String attr12,
                                Collection<CodeDetailResInfo> codeDetailResInfos) {

}