package br.com.app.util;

import lombok.Data;

/**
 * Classe de Erro Generico
 * @author Danilo Humberto
 *
 */
@Data
public class ErroGenerico {
	/**
	 * Atributo errorMessage
	 */
	private String errorMessage;

	/**
	 * Construtor
	 * @param errorMessage
	 */
	public ErroGenerico(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
