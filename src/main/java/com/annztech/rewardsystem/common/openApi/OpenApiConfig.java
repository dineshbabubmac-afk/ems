package com.annztech.rewardsystem.common.openApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Reward System API",
                version = "v1",
                description = """
            ### Standard API Response Structure

            All endpoints return a consistent JSON response format, regardless of success or failure:

            #### ✅ Success Example:
            ```json
            {
              "data": {
                "id": "1234",
                "title": "Sample Title"
              },
              "status": "success",
              "message": "Operation completed successfully",
              "error": null,
              "timestamp": "2025-08-01T06:23:11.341Z"
            }
            ```

            #### ❌ Error Example:
            ```json
            {
              "status": "error",
              "message": "Invalid request parameters",
              "error": {
                "code": 400,
                "message": "Title field is required"
              },
              "timestamp": "2025-08-01T06:23:11.341Z"
            }
            ```

            - `data`: Response payload (generic per endpoint)
            - `status`: "success" or "error"
            - `message`: Informational status message
            - `error`: Detailed error object (only present for errors)
            - `timestamp`: ISO 8601 formatted time
            """
        )
)
public class OpenApiConfig {
    @Value("${openapi.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl)))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
