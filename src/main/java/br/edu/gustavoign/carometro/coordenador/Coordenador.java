package br.edu.gustavoign.carometro.coordenador;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
=======
import jakarta.persistence.*;
import lombok.*;
>>>>>>> f5e5ca8bfa5d378ba3ea3dff69cd24e22ad0b5db

@Entity
@Table(name = "coordenador")
@Getter
@Setter
<<<<<<< HEAD
=======
@NoArgsConstructor
>>>>>>> f5e5ca8bfa5d378ba3ea3dff69cd24e22ad0b5db
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
