package br.edu.gustavoign.carometro.aluno;

import java.sql.Blob;

public record DadosListagemAluno(Long id, String nome, String RA, String curso, String ano, String links,
		String comentarioHistorico, String comentarioFatec, String comentarioLivre, Blob foto, String numeroCelular) {

	public DadosListagemAluno(Aluno aluno) {
		this(aluno.getId(), aluno.getNome(), aluno.getRA(), aluno.getCurso(), aluno.getAnoIngresso(), aluno.getLinks(),
				aluno.getComentarioHistorico(), aluno.getComentarioFatec(), aluno.getComentarioLivre(), aluno.getFoto(),
				aluno.getNumeroCelular());
	}
}
