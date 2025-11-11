package com.annztech.rewardsystem.modules.certificate.request.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.certificate.request.constants.CertificateRequestConstant;
import com.annztech.rewardsystem.modules.certificate.request.dto.*;
import com.annztech.rewardsystem.modules.certificate.request.service.CertificateRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/certificate-nomination")
@Tag(name = "Certificate Nominations", description = "Manage Certificate Nomination Requests")
public class CertificateRequestController extends LocalizationService {

    private final CertificateRequestService certificateRequestService;

    public CertificateRequestController(CertificateRequestService certificateRequestService) {
        this.certificateRequestService = certificateRequestService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object>  createNomination(
            @RequestPart("nominatedToEmployee") String nominatedToEmployee,
            @RequestPart("certificateId") String certificateId,
            @RequestPart("criteria") String criteriaJson,
            @RequestPart("files") List<MultipartFile> files
    )  {
        CertificateRequestCreateDTO dto = new CertificateRequestCreateDTO();
        dto.setNominatedToEmployee(nominatedToEmployee);
        dto.setCertificateId(certificateId);
        dto.setCriteriaJSONString(criteriaJson);
        dto.setFiles(files);
        CertificateRequestIdResponseDTO response = certificateRequestService.createCertificateRequest(dto);
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_CREATED), HttpStatus.OK, response);
    }

    @Operation(summary = "Get all Nominated Certificate Requests", description = "Retrieve a list of all nominated certificate requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificate requests retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CertificateRequestResponseDTO.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<Object> getAllCertificateRequests(@RequestParam(required = false) String query  , @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_FETCHED), HttpStatus.OK, certificateRequestService.getAllCertificateRequests(query, pageable));
    }

    @Operation(summary = "Get Nominated Certificate Request by ID", description = "Retrieve a specific certificate request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificate request retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CertificateRequestResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid certificate request ID",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Invalid ID\"}"))),
            @ApiResponse(responseCode = "404", description = "Certificate request not found",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Not found\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCertificateRequestById(
            @Parameter(description = "Certificate Request ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        CertificateRequestResponseDTO response = certificateRequestService.getCertificateRequestById(id);
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_FETCHED), HttpStatus.OK, response);
    }

    @Operation(summary = "Update Nomination Certificate Request", description = "Updates an existing certificate request by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificate request updated successfully",
                    content = @Content(schema = @Schema(implementation = CertificateRequestIdResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid certificate request ID or data",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Invalid input\"}"))),
            @ApiResponse(responseCode = "404", description = "Certificate request not found",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Not found\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCertificateRequest(
            @Parameter(description = "Certificate Request ID", required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable String id,
            @Valid @RequestBody CertificateRequestUpdateDTO dto) {
        CertificateRequestIdResponseDTO response = certificateRequestService.updateCertificateRequest(id, dto);
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_UPDATED), HttpStatus.OK, response);
    }

    @Operation(summary = "Delete Nomination Certificate Request", description = "Deletes a certificate request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificate request deleted successfully",
                    content = @Content(schema = @Schema(implementation = CertificateRequestIdResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid certificate request ID",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Invalid ID\"}"))),
            @ApiResponse(responseCode = "404", description = "Certificate request not found",
                    content = @Content(schema = @Schema(example = "{\"message\":\"Not found\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificateRequest(
            @Parameter(description = "Certificate Request ID", required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        CertificateRequestIdResponseDTO response = certificateRequestService.deleteCertificateRequest(id);
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_DELETED), HttpStatus.OK, response);
    }
    @GetMapping("/search")
    public ResponseEntity<Object> searchCertificateRequest(
            @Parameter(
                    description = "Search query for code, nominatedTo first name, or nominatedBy first name. " +
                            "If null, all records are retrieved.",
                    example = "John"
            )
            @RequestParam(value = "q", required = false) String search,

            @Parameter(
                    description = "Action field: ACTIVE or INACTIVE. If null, all records are retrieved.",
                    example = "ACTIVE"
            )
            @RequestParam(value = "field", required = false) String actionField,

            @Parameter(
                    description = "Status code of the certificate request.", example = "PENDING/REJECTED"
            )
            @RequestParam(value = "status", required = false) String status
    ) {
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_FETCHED), HttpStatus.OK, certificateRequestService.search(search, actionField, status));
    }

    @PatchMapping("/publish")
    public ResponseEntity<Object> publishCertificate(@RequestBody CertificateRequestPublishDTO certificateRequestPublishDTO){
        return AppResponse.success(getMessage(CertificateRequestConstant.CERTIFICATE_REQUEST_PUBLISHED), HttpStatus.OK, certificateRequestService.certificatePublish(certificateRequestPublishDTO));
    }
}
