package br.edu.gustavoign.carometro.curso;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String anoImplementacao;

	public Curso() {
	}

	public Curso(DadosCadastroCurso dados) {
		super();
		this.nome = dados.nome();
		this.anoImplementacao = dados.anoImplementacao();
	}

	public void atualizarInformacoes(DadosAtualizacaoCurso dados) {
		if (dados.nome() != null)
			this.nome = dados.nome();
		if (dados.anoImplementacao() != null)
			this.anoImplementacao = dados.anoImplementacao();
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
