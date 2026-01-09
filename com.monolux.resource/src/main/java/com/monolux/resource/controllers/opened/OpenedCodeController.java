package com.monolux.resource.controllers.opened;

import com.monolux.common.web.response.ApiResponse;
import com.monolux.domain.services.CodeDetailService;
import com.monolux.domain.services.CodeMasterService;
import com.monolux.resource.dto.res.CodeDetailResInfo;
import com.monolux.resource.dto.res.CodeMasterResInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "System Code API")
@RestController
@RequestMapping(value = OpenedUserController.BASE_PATH)
public class OpenedCodeController extends BaseOpenedController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    protected static final String BASE_PATH = BaseOpenedController.OPENED_API_PREFIX + "/codes";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final CodeMasterService codeMasterService;

    private final CodeDetailService codeDetailService;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    OpenedCodeController(final CodeMasterService codeMasterService,
                         final CodeDetailService codeDetailService) {
        this.codeMasterService = codeMasterService;
        this.codeDetailService = codeDetailService;
    }

    // endregion

    // region ▒▒▒▒▒ Get Mapping ▒▒▒▒▒

    @ApiOperation(value = "CodeMaster Info")
    @GetMapping(value = "/{codeMasterId}")
    public ResponseEntity<ApiResponse> findCode(@RequestHeader final HttpHeaders reqHeaders,
                                                @Parameter(description = "CodeMasterId")
                                                @PathVariable(value = "codeMasterId") final String codeMasterId) {
        return this.codeMasterService.findCodeMasterById(codeMasterId)
                .map(codeMaster -> ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK,
                        CodeMasterResInfo.builder()
                                .codeMasterId(codeMaster.getCodeMasterId())
                                .codeMasterName(codeMaster.getCodeMasterName())
                                .attr01(codeMaster.getAttr01())
                                .attr02(codeMaster.getAttr02())
                                .attr03(codeMaster.getAttr03())
                                .attr04(codeMaster.getAttr04())
                                .attr05(codeMaster.getAttr05())
                                .attr06(codeMaster.getAttr06())
                                .attr07(codeMaster.getAttr07())
                                .attr08(codeMaster.getAttr08())
                                .attr09(codeMaster.getAttr09())
                                .attr10(codeMaster.getAttr10())
                                .attr11(codeMaster.getAttr11())
                                .attr12(codeMaster.getAttr12())
                                .codeDetailResInfos(codeMaster.getCodeDetails()
                                        .stream().map(codeDetail -> CodeDetailResInfo.builder()
                                                .codeMasterId(codeMaster.getCodeMasterId())
                                                .codeDetailId(codeDetail.getCodeDetailId().getCodeDetailId())
                                                .codeDetailName(codeDetail.getCodeDetailName())
                                                .attr01(codeDetail.getAttr01())
                                                .attr02(codeDetail.getAttr02())
                                                .attr03(codeDetail.getAttr03())
                                                .attr04(codeDetail.getAttr04())
                                                .attr05(codeDetail.getAttr05())
                                                .attr06(codeDetail.getAttr06())
                                                .attr07(codeDetail.getAttr07())
                                                .attr08(codeDetail.getAttr08())
                                                .attr09(codeDetail.getAttr09())
                                                .attr10(codeDetail.getAttr10())
                                                .attr11(codeDetail.getAttr11())
                                                .attr12(codeDetail.getAttr12())
                                                .build())
                                        .toList())
                        .build())))
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "CodeDetail Info")
    @GetMapping(value = "/{codeMasterId}/{codeDetailId}")
    public ResponseEntity<ApiResponse> findCode(@RequestHeader final HttpHeaders reqHeaders,
                                                @Parameter(description = "CodeMasterId")
                                                @PathVariable(value = "codeMasterId") final String codeMasterId,
                                                @Parameter(description = "codeDetailId")
                                                @PathVariable(value = "codeDetailId") final String codeDetailId) {
        return this.codeDetailService.findCodeDetailById(codeMasterId, codeDetailId)
                .map(codeDetail -> ResponseEntity.ok(ApiResponse.createResponse(HttpStatus.OK,
                        CodeDetailResInfo.builder()
                        .codeMasterId(codeMasterId)
                        .codeDetailId(codeDetailId)
                        .codeDetailName(codeDetail.getCodeDetailName())
                        .attr01(codeDetail.getAttr01())
                        .attr02(codeDetail.getAttr02())
                        .attr03(codeDetail.getAttr03())
                        .attr04(codeDetail.getAttr04())
                        .attr05(codeDetail.getAttr05())
                        .attr06(codeDetail.getAttr06())
                        .attr07(codeDetail.getAttr07())
                        .attr08(codeDetail.getAttr08())
                        .attr09(codeDetail.getAttr09())
                        .attr10(codeDetail.getAttr10())
                        .attr11(codeDetail.getAttr11())
                        .attr12(codeDetail.getAttr12())
                        .build())))
                .orElse(ResponseEntity.notFound().build());
    }

    // endregion
}