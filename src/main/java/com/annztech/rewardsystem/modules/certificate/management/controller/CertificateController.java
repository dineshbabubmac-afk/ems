package com.annztech.rewardsystem.modules.certificate.management.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.management.constants.CertificateConstants;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateCreateDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateResponseDTO;
import com.annztech.rewardsystem.modules.certificate.management.dto.CertificateUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.management.service.CertificateService;
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
@RequestMapping("/api/v1/certificates")
@Tag(name = "Certificates", description = "Create & Manage the Certificates")
public class CertificateController extends LocalizationService {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Operation(summary = "Create a new Certificate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Certificate created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CertificateIdResponseDTO.class)
                    )
            ),
    })
    @PostMapping
    public ResponseEntity<Object> createCertificate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Certificate details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CertificateCreateDTO.class))
            )
            @Valid @RequestBody CertificateCreateDTO dto){
        return AppResponse.success(getMessage(CertificateConstants.CERTIFICATE_CREATED_SUCCESSFULLY), HttpStatus.CREATED, certificateService.createCertificate(dto) );
    }

    @Operation(summary = "Get all Certificates")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all Certificates successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateResponseDTO.class))
                    )
            ),
    })
    @GetMapping
    public ResponseEntity<Object> getAllCertificates(){
        return AppResponse.success(getMessage(CertificateConstants.CERTIFICATES_FETCHED_SUCCESSFULLY), HttpStatus.OK, certificateService.getAllCertificates());
    }

    @Operation(summary = "Get Certificate By Id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = " Fetched Certificate By Id successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateResponseDTO.class))
                    )
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCertificatesById(
            @Parameter(description = "Certificate ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id
    ){
        return AppResponse.success(getMessage(CertificateConstants.CERTIFICATES_FETCHED_BY_ID), HttpStatus.OK, certificateService.getCertificateById(id));
    }

    @Operation(summary = "Update the Certificate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated Certificate successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateIdResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Certificate not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCertificate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Certificate details to be updated",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CertificateUpdateDTO.class))
            )
            @Parameter(description = "Certificate ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id,
            @RequestBody CertificateUpdateDTO dto
    ){
        return AppResponse.success(getMessage(CertificateConstants.CERTIFICATE_UPDATED_SUCCESSFULLY), HttpStatus.OK, certificateService.updateCertificate(id, dto));
    }

    @Operation(summary = "Delete the Certificate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Deleted Certificate successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateIdResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Certificate not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificate(
            @Parameter(description = "Certificate ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id
    ){
        return AppResponse.success(getMessage(CertificateConstants.CERTIFICATE_DELETED_SUCCESSFULLY), HttpStatus.OK, certificateService.deleteCertificate(id));
    }
}
