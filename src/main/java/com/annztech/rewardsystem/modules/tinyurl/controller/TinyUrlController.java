package com.annztech.rewardsystem.modules.tinyurl.controller;

import com.annztech.rewardsystem.modules.tinyurl.service.TinyUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/certificate")
@Slf4j
@Tag(name = "Tiny URL", description = "APIs for resolving shortened URLs")
public class TinyUrlController {

    private final TinyUrlService tinyUrlService;

    public TinyUrlController(TinyUrlService tinyUrlService) {
        this.tinyUrlService = tinyUrlService;
    }

    @GetMapping("/redirect")
    @Operation(
            summary = "Redirect to original long URL",
            description = "Resolves a short code into its original long URL and redirects the client to that URL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Successfully redirected to the original long URL"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing short code"),
            @ApiResponse(responseCode = "404", description = "No mapping found for given code"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    public ResponseEntity<String> redirectTemplate(
            @Parameter(
                    description = "Shortened code used to lookup the original URL",
                    required = true,
                    example = "abc123"
            )
            @RequestParam("code") String code ) throws IOException {
        String htmlContent = tinyUrlService.getCertificateFromTinyUrl(code);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
    }

    @GetMapping("/redirect/me")
    @Operation(
            summary = "Redirect to original long URL",
            description = "Resolves a short code into its original long URL and redirects the client to that URL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Successfully redirected to the original long URL"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing short code"),
            @ApiResponse(responseCode = "404", description = "No mapping found for given code"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    public ResponseEntity<String> redirectMeTemplate(
            @Parameter(
                    description = "Shortened code used to lookup the original URL",
                    required = true,
                    example = "abc123"
            )
            @RequestParam("code") String code ) throws IOException {
        String html = tinyUrlService.getEmployeeCertificateHtml(code);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }
}
