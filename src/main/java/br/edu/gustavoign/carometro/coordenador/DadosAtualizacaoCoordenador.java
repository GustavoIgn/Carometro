package br.edu.gustavoign.carometro.coordenador;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCoordenador(
	@NotNull
	Long id,
	String nome,
	String curso
	) {
}