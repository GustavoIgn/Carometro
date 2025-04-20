package br.edu.gustavoign.carometro.coordenador;

public record DadosListagemCoordenador(Long id, String nome) {

	public DadosListagemCoordenador (Coordenador coordenador) {
	    this(coordenador.getId(),
	    	coordenador.getNome());
	}
}
