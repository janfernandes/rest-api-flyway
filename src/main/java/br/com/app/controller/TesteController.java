package br.com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe controller que expoe os serviços da classe StatusRegistro
 * 
 * @author Danilo Humberto
 *
 */
@RestController
public class TesteController {
	
	@GetMapping("/teste")
	public String buscarTodos() {
		return "Teste";
	}
	
	@GetMapping("/calcularAreaCirculo/{raio}")
	public double calculaAreaCirculo(@PathVariable("raio") Long raio) {
		return (double) (3.14*raio*raio);
		
	}
}
