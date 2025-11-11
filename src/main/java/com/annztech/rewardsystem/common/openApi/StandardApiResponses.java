package com.annztech.rewardsystem.common.openApi;


import com.annztech.rewardsystem.common.dto.AppResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // Required for runtime processing
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Resource created successful",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))
        ),
        @ApiResponse(
                responseCode = "200",
                description = "Operation successful",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = @Content(mediaType = "application/json",schema = @Schema(implementation = AppResponse.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(mediaType = "application/json",schema = @Schema(implementation = AppResponse.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Server error",
                content = @Content(mediaType = "application/json",schema = @Schema(implementation = AppResponse.class))
        )
})
public @interface StandardApiResponses {
}