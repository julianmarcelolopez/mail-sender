package com.jpdevs.mailsender.controller;

import com.jpdevs.mailsender.model.EmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Email Controller", description = "API endpoints para el envío de emails")
public interface EmailApi {

	@Operation(summary = "Enviar email HTML", description = "Envía un email con contenido HTML")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Email enviado exitosamente",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
	})
	@PostMapping("/html")
	ResponseEntity<?> sendHtmlEmail(@RequestBody EmailRequest emailRequest);

	@Operation(summary = "Enviar email simple", description = "Envía un email con contenido de texto plano")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Email simple enviado exitosamente",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
	})
	@PostMapping("/simple")
	ResponseEntity<?> sendSimpleEmail(@RequestBody EmailRequest emailRequest);
}