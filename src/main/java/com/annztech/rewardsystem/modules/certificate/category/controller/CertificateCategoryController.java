package com.annztech.rewardsystem.modules.certificate.category.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.category.constants.CertificateCategoryConstants;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryCreateDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryIdResponseDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryResponseDTO;
import com.annztech.rewardsystem.modules.certificate.category.dto.CertificateCategoryUpdateDTO;
import com.annztech.rewardsystem.modules.certificate.category.service.CertificateCategoryService;
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
@RequestMapping("/api/v1/certificate-category")
@Tag(name = "Certificate Category", description = "Create & Manage the Certificate Category in English & Arabic languages")
public class CertificateCategoryController extends LocalizationService {
    private final CertificateCategoryService certificateCategoryService;

    public CertificateCategoryController(CertificateCategoryService certificateCategoryService) {
        this.certificateCategoryService = certificateCategoryService;
    }

    @Operation(summary = "Create Certificate-Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Certificate Category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CertificateCategoryIdResponseDTO.class)
                    )
            ),
    })
    @PostMapping
    public ResponseEntity<Object> createCertificateCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Certificate Category in English and Arabic",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CertificateCategoryCreateDTO.class))
            )
            @Valid @RequestBody CertificateCategoryCreateDTO dto){
        return AppResponse.success(getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_CREATED), HttpStatus.OK, certificateCategoryService.createCertificateCategory(dto) );
    }

    @Operation(summary = "Get all Certificate Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all Certificate Category successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateCategoryResponseDTO.class))
                    )
            ),
    })
    @GetMapping
    public ResponseEntity<Object> getAllCertificateCategory(){
        return AppResponse.success(getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_FETCHED), HttpStatus.OK, certificateCategoryService.getAllCertificateCategories());
    }

    @Operation(summary = "Update the Certificate Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated Certificate Category successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateCategoryIdResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Certificate Category not found")
    })
    @PatchMapping("/{categoryId}")
    public ResponseEntity<Object> updateCertificateCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Certificate Category in English and Arabic",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CertificateCategoryUpdateDTO.class))
            )
            @Parameter(description = "Certificate Category ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String categoryId,
            @RequestBody CertificateCategoryUpdateDTO dto
    ){
        return AppResponse.success(getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_UPDATED), HttpStatus.OK, certificateCategoryService.updateCertificateCategory(categoryId, dto));
    }

    @Operation(summary = "Delete the Certificate Category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Deleted Certificate Category successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CertificateCategoryIdResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Certificate Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificateCategory(
            @Parameter(description = "Certificate Category ID", required = true, example = "f452e870-6c41-4d45-918d-3c924e9016c4")
            @PathVariable String id
    ){
        return AppResponse.success(getMessage(CertificateCategoryConstants.CERTIFICATE_CATEGORY_DELETED), HttpStatus.OK, certificateCategoryService.deleteCertificateCategory(id));
    }
}
