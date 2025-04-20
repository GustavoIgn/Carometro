package br.edu.gustavoign.carometro.curso;

public record DadosListagemCurso(Long id, String nome, String ano) {

	public DadosListagemCurso(Curso curso) {
		this(curso.getId(), curso.getNome(), curso.getAnoImplementacao());
	}
}
