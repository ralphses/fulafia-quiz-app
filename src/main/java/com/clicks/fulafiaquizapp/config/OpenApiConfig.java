package com.clicks.fulafiaquizapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Documentation for come carry me application backend",
                description = "This is the full documentation of all the endpoints in this API"
        ),
        servers = {
                @Server(description = "Server for local testing", url = "http://localhost:8080"),
                @Server(description = "Server for staging testing", url = "https://fulafia-quiz-app-production.up.railway.app/")
        }
)
public class OpenApiConfig {
}
