package br.edu.gustavoign.carometro.coordenador;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCoordenador(
		
		@NotBlank
		String nome, String curso) {
}
