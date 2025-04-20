package br.edu.gustavoign.carometro.coordenador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.edu.gustavoign.carometro.usuario.TipoUsuario;
import br.edu.gustavoign.carometro.usuario.Usuario;
import br.edu.gustavoign.carometro.usuario.UsuarioRepository;
import br.edu.gustavoign.carometro.usuario.Util;

@Component
public class CoordenadorInitializer implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CoordenadorRepository coordenadorRepository;

	@Override
	public void run(String... args) throws Exception {
		String userPadrao = "admincoord";
		
		// Verifica se o usuário padrão já existe
		if (usuarioRepository.findByUser(userPadrao) != null) {
			System.out.println("⚠ Coordenador padrão já existe. Nenhuma ação necessária.");
			return;
		}
		
		String senhaPadrao = Util.md5("123456");
		Usuario usuario = new Usuario();
		usuario.setUser(userPadrao);
		usuario.setSenha(senhaPadrao);
		usuario.setTipo(TipoUsuario.COORDENADOR);

		usuarioRepository.save(usuario);

		Coordenador coordenador = new Coordenador();
		coordenador.setCurso("Curso Padrão");
		coordenador.setNome("Coordenador Padrão");
		coordenador.setValido(true);

		coordenadorRepository.save(coordenador);

		System.out.println("✔ Coordenador padrão criado com sucesso.");
	}
}
