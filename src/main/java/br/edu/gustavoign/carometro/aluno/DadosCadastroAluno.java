package br.edu.gustavoign.carometro.aluno;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroAluno(@NotBlank String nome, String RA, @NotNull Long cursoId, String anoIngresso,
		String numeroCelular, String links, String comentarioHistorico, String comentarioFatec, String comentarioLivre,
		MultipartFile foto) {
}
