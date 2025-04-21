package br.edu.gustavoign.carometro.coordenador;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coordenador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Coordenador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private boolean valido = false;

	private String tokenValidacao; // usado para validação por e-mail

	public Coordenador(DadosCadastroCoordenador dados) {
		this.nome = dados.nome();
		this.valido = true;
	}
	
	public Coordenador() {
		
	}

	public void atualizarInformacoes(DadosAtualizacaoCoordenador dados) {
		if (dados.nome() != null)
			this.nome = dados.nome();
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

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public String getTokenValidacao() {
		return tokenValidacao;
	}

	public void setTokenValidacao(String tokenValidacao) {
		this.tokenValidacao = tokenValidacao;
	}

	
}
