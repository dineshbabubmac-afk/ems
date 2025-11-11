package com.annztech.rewardsystem.modules.certificate.template.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.template.constants.TemplateConstants;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.template.dto.TemplateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificate-template")
@Tag(name = "Certificate Template", description = "Create & Manage the Certificate Template")
public class TemplateController extends LocalizationService {
    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Operation(summary = "Create Certificate Template")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created Certificate Template successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TemplateIdResponseDTO.class)
                    )
            ),
    })
    @PostMapping
    public ResponseEntity<Object> createCertificateTemplate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Certificate Template details",
            required = true,
            content = @Content(schema = @Schema(implementation = TemplateCreateDTO.class))
    )
            @Valid @RequestBody TemplateCreateDTO dto){
        return AppResponse.success(getMessage(TemplateConstants.TEMPLATE_CREATED), HttpStatus.CREATED, templateService.createCertificateTemplate(dto));
    }

    @Operation(summary = "Get all Certificate Templates")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all Certificate Templates successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TemplateResponseDTO.class))
                    )
            ),
    })
    @GetMapping
    public ResponseEntity<Object> getAllCertificateTemplates(){
        return AppResponse.success(getMessage(TemplateConstants.ALL_TEMPLATES), HttpStatus.OK, templateService.getAllCertificateTemplates());
    }

    @Operation(summary = "Get Certificate Template By Id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = " Fetched Certificate By Template successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TemplateResponseDTO.class))
                    )
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCertificatesTemplateById(
            @Parameter(description = "Template ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id
    ){
        return AppResponse.success(getMessage(TemplateConstants.TEMPLATE_FETCHED_BY_ID), HttpStatus.OK, templateService.getCertificateTemplateById(id));
    }

    @Operation(summary = "Update the Certificate Template")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated Certificate Template successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TemplateIdResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Certificate Template not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCertificateTemplate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Certificate Template details to be updated",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TemplateUpdateDTO.class))
            )
            @Parameter(description = "Certificate Template ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id,
            @RequestBody TemplateUpdateDTO dto
    ){
        return AppResponse.success(getMessage(TemplateConstants.TEMPLATE_UPDATED), HttpStatus.OK, templateService.updateCertificateTemplate(id, dto));
    }

    @Operation(summary = "Delete the Certificate Template")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Deleted Certificate Template successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TemplateIdResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Certificate Template not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificateTemplate(
            @Parameter(description = "Certificate Template ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id
    ){
        return AppResponse.success(getMessage(TemplateConstants.TEMPLATE_DELETED), HttpStatus.OK, templateService.deleteCertificateTemplate(id));
    }

    @Operation(summary = "View the Certificate Template (Render as HTML)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched Certificate Template view successfully",
                    content = @Content(
                            mediaType = "text/html"
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid template ID"),
            @ApiResponse(responseCode = "404", description = "Certificate Template not found")
    })
    @GetMapping("/{id}/view")
    public ResponseEntity<String> viewCertificateTemplate(
            @Parameter(description = "Certificate Template ID", required = true)
            @PathVariable String id
    ) {
        String htmlContent = templateService.getCertificateTemplateViewById(id);
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlContent);
    }

}
