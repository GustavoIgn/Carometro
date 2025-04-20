package br.edu.gustavoign.carometro.aluno;

import java.sql.Blob;

import javax.sql.rowset.serial.SerialBlob;

import br.edu.gustavoign.carometro.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	private String nome;
	private String RA;
	private String numeroCelular;
	private String curso;
	private String anoIngresso;
	private String links;
	private String comentarioHistorico;
	private String comentarioFatec;
	private String comentarioLivre;
	private Blob foto;
	private boolean validado = false;

	public Aluno() {
	}

	public Aluno(DadosCadastroAluno dados, Usuario usuario) {
		this.nome = dados.nome();
		this.RA = dados.RA();
		this.curso = dados.curso();
		this.anoIngresso = dados.anoIngresso();
		this.links = dados.links();
		this.comentarioHistorico = dados.comentarioHistorico();
		this.comentarioFatec = dados.comentarioFatec();
		this.comentarioLivre = dados.comentarioLivre();
		this.usuario = usuario;
		this.numeroCelular = dados.numeroCelular();
		try {
			if (dados.foto() != null && !dados.foto().isEmpty()) {
				this.foto = new SerialBlob(dados.foto().getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.validado = false;
	}

	public void atualizarInformacoes(DadosAtualizacaoAluno dados) {
		if (dados.nome() != null)
			this.nome = dados.nome();
		if (dados.RA() != null)
			this.RA = dados.RA();
		if (dados.curso() != null)
			this.curso = dados.curso();
		if (dados.anoIngresso() != null)
			this.anoIngresso = dados.anoIngresso();
		if (dados.links() != null)
			this.links = dados.links();
		if (dados.comentarioHistorico() != null)
			this.comentarioHistorico = dados.comentarioHistorico();
		if (dados.comentarioFatec() != null)
			this.comentarioFatec = dados.comentarioFatec();
		if (dados.comentarioLivre() != null)
			this.comentarioLivre = dados.comentarioLivre();
		if (dados.foto() != null && !dados.foto().isEmpty()) {
			try {
				this.foto = new SerialBlob(dados.foto().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (dados.numeroCelular() != null) {
			this.numeroCelular = dados.numeroCelular();
		}
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

	public String getRA() {
		return RA;
	}

	public void setRA(String rA) {
		RA = rA;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getAnoIngresso() {
		return anoIngresso;
	}

	public void setAnoIngresso(String ano) {
		this.anoIngresso = ano;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getComentarioHistorico() {
		return comentarioHistorico;
	}

	public void setComentarioHistorico(String comentarioHistorico) {
		this.comentarioHistorico = comentarioHistorico;
	}

	public String getComentarioFatec() {
		return comentarioFatec;
	}

	public void setComentarioFatec(String comentarioFatec) {
		this.comentarioFatec = comentarioFatec;
	}

	public String getComentarioLivre() {
		return comentarioLivre;
	}

	public void setComentarioLivre(String comentarioLivre) {
		this.comentarioLivre = comentarioLivre;
	}

	public Blob getFoto() {
		return foto;
	}

	public void setFoto(Blob foto) {
		this.foto = foto;
	}

	public void setUsuario(Usuario usuarioLogado) {
		this.usuario = usuarioLogado;
	}

	public String getNumeroCelular() {
		return numeroCelular;
	}

	public void setNumeroCelular(String numeroCelular) {
		this.numeroCelular = numeroCelular;
	}

	public boolean isValidado() {
		return validado;
	}

	public void setValidado(boolean validado) {
		this.validado = validado;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	
}
