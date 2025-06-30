package br.edu.gustavoign.carometro.aluno;

import java.sql.Blob;

import javax.sql.rowset.serial.SerialBlob;

import br.edu.gustavoign.carometro.curso.Curso;
import br.edu.gustavoign.carometro.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
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
	@ManyToOne
	@JoinColumn(name = "curso_id", nullable = false)
	private Curso curso;
	private String anoIngresso;
	private String links;
	@Column(name = "comentario_historico", length = 500) // Limite de 500 caracteres
	private String comentarioHistorico;
	@Column(name = "comentario_fatec", length = 500) // Limite de 500 caracteres
	private String comentarioFatec;
	@Column(name = "comentario_livre", length = 1000) // Limite de 1000 caracteres
	private String comentarioLivre;
	private Blob foto;
	private Boolean validado = false;

	public Aluno() {
	}

	public Aluno(DadosCadastroAluno dados, Usuario usuario, Curso curso) {
		this.nome = dados.nome();
		this.RA = dados.RA();
		this.curso = curso;
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
	    if (dados.nome() != null) this.nome = dados.nome();
	    if (dados.RA() != null) this.RA = dados.RA();
	    if (dados.anoIngresso() != null) this.anoIngresso = dados.anoIngresso();
	    if (dados.links() != null) this.links = dados.links();
	    if (dados.comentarioHistorico() != null) this.comentarioHistorico = dados.comentarioHistorico();
	    if (dados.comentarioFatec() != null) this.comentarioFatec = dados.comentarioFatec();
	    if (dados.comentarioLivre() != null) this.comentarioLivre = dados.comentarioLivre();
	    if (dados.numeroCelular() != null) this.numeroCelular = dados.numeroCelular();

	    // Só altera a foto se o usuário realmente enviar uma nova foto
	    if (dados.foto() != null && !dados.foto().isEmpty()) {
	        try {
	            this.foto = new SerialBlob(dados.foto().getBytes());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    // Se dados.foto() == null ou vazio, a foto antiga permanece
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

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
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
