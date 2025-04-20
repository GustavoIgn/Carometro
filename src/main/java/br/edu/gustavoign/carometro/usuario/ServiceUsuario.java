package br.edu.gustavoign.carometro.usuario;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.gustavoign.carometro.exception.CriptoExistsException;
import br.edu.gustavoign.carometro.exception.EmailExistsException;
import br.edu.gustavoign.carometro.exception.ServiceExc;

@Service
public class ServiceUsuario {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public void salvarUsuario(Usuario user) throws Exception {

		try {
			if (usuarioRepository.findByEmail(user.getEmail()) != null) {
				throw new EmailExistsException("Este email já está cadastrado: " + user.getEmail());
			}

			if (usuarioRepository.findByUser(user.getUser()) != null) {
				throw new CriptoExistsException("Este nome de usuário já está em uso: " + user.getUser());
			}

			user.setSenha(Util.md5(user.getSenha()));

		} catch (NoSuchAlgorithmException e) {
			throw new CriptoExistsException("Erro na criptografia da senha");
		}

		usuarioRepository.save(user);
	}

	public Usuario loginUser(String user, String senha) throws ServiceExc {

		return usuarioRepository.buscarLogin(user, senha);
	}
}
