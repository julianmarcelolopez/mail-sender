package com.jpdevs.mailsender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmailPreviewController {

	@GetMapping("/preview-email")
	public String previewEmail(Model model) {
		// Agregamos datos de ejemplo para la plantilla
		model.addAttribute("userName", "Usuario de Prueba");

		// Retorna el nombre de la plantilla (sin la extensión .html)
		return "email-template";
	}
}