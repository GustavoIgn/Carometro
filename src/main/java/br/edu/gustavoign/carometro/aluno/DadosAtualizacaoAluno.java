package br.edu.gustavoign.carometro.aluno;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAluno(@NotNull Long id, String nome, String RA, String curso, String anoIngresso,
		String numeroCelular, String links, String comentarioHistorico, String comentarioFatec, String comentarioLivre,
		MultipartFile foto) {
}