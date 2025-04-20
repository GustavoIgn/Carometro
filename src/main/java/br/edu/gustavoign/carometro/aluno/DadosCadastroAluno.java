package br.edu.gustavoign.carometro.aluno;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAluno(

		@NotBlank String nome, String RA, String curso, String anoIngresso, Long usuarioId, String numeroCelular,
		String links, String comentarioHistorico, String comentarioFatec, String comentarioLivre, MultipartFile foto) {
}
