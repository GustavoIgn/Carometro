/*package br.edu.gustavoign.carometro.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	private String emailAdministrador = "gustavoignacio280@gmail.com";

	public void notificarCadastroCoordenador(String nome, String curso, String emailUsuario, String tokenValidacao) {
		try {
			SimpleMailMessage mensagem = new SimpleMailMessage();
			mensagem.setTo(emailAdministrador);
			mensagem.setSubject("Novo Coordenador aguardando aprovação");

			// Construir o link com o token de validação
			String link = "http://localhost:8080/coordenador/validar?token=" + tokenValidacao;

			mensagem.setText("Novo coordenador cadastrado:\n\n" + "Nome: " + nome + "\n" + "Curso: " + curso + "\n\n"
					+ "Clique no link abaixo para validar o acesso:\n" + link);

			mailSender.send(mensagem);
			System.out.println("E-mail enviado com sucesso.");
		} catch (Exception e) {
			System.err.println("Erro ao enviar e-mail: " + e.getMessage());
			e.printStackTrace();
		}
	}
}*/