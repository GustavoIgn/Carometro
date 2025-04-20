package br.edu.gustavoign.carometro.curso;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCurso(

		@NotBlank String nome, String anoImplementacao) {
}
