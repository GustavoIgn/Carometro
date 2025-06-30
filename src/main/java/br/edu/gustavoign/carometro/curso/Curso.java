package br.edu.gustavoign.carometro.curso;

import br.edu.gustavoign.carometro.coordenador.Coordenador;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
<<<<<<< HEAD
=======
import lombok.NoArgsConstructor;
>>>>>>> f5e5ca8bfa5d378ba3ea3dff69cd24e22ad0b5db
import lombok.Setter;

@Entity
@Table(name = "curso")
@Getter
@Setter
<<<<<<< HEAD
=======
@NoArgsConstructor
>>>>>>> f5e5ca8bfa5d378ba3ea3dff69cd24e22ad0b5db
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String anoImplementacao;
	@ManyToOne
	@JoinColumn(name = "coord_id", nullable = false)
	private Coordenador coord;

	public Curso() {
	}

	public Curso(DadosCadastroCurso dados, Coordenador coord) {
		super();
		this.nome = dados.nome();
		this.anoImplementacao = dados.anoImplementacao();
		this.coord = coord;
	}

	public void atualizarInformacoes(DadosAtualizacaoCurso dados) {
	    if (dados.nome() != null) this.nome = dados.nome();
	    if (dados.anoImplementacao() != null) this.anoImplementacao = dados.anoImplementacao();
	    if (dados.coordId() != null) this.coord.setId(dados.coordId()); // ou setCoord() se buscar o obj.
	}

	public Coordenador getCoord() {
		return coord;
	}

	public void setCoord(Coordenador coord) {
		this.coord = coord;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAnoImplementacao() {
		return anoImplementacao;
	}

	public void setAnoImplementacao(String anoImplementacao) {
		this.anoImplementacao = anoImplementacao;
	}

}
