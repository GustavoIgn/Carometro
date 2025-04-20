package br.edu.gustavoign.carometro.usuario;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.gustavoign.carometro.aluno.AlunoRepository;
import br.edu.gustavoign.carometro.coordenador.Coordenador;
import br.edu.gustavoign.carometro.coordenador.CoordenadorRepository;
import br.edu.gustavoign.carometro.coordenador.DadosCadastroCoordenador;
import br.edu.gustavoign.carometro.exception.ServiceExc;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ServiceUsuario serviceUsuario;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private CoordenadorRepository coordenadorRepository;

	/*
	 * @Autowired private EmailService emailService;
	 */

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("home/index");
		modelAndView.addObject("lista", alunoRepository.findByValidadoTrue(Sort.by("nome").ascending()));
		return modelAndView;
	}

	@GetMapping("/login")
	public ModelAndView login(@RequestParam(required = false) String tipo) {
		ModelAndView modelAndView;

		try {
			TipoUsuario tipoUsuario = TipoUsuario.valueOf(tipo);
			String caminhoView = "login/" + tipoUsuario.name().toLowerCase() + "/login";
			modelAndView = new ModelAndView(caminhoView);
			modelAndView.addObject("tipoSelecionado", tipoUsuario);
		} catch (Exception e) {
			// tipo inválido ou nulo
			modelAndView = new ModelAndView("redirect:/");
			return modelAndView;
		}

		modelAndView.addObject("usuario", new Usuario());
		return modelAndView;
	}

	@GetMapping("/cadastro")
	public ModelAndView cadastro(@RequestParam String tipo) {
	    if (tipo.equalsIgnoreCase("coordenador")) {
	        ModelAndView mv = new ModelAndView("login/coordenador/cadastro-bloqueado");
	        mv.addObject("mensagem", "No momento, não estamos aceitando novos cadastros de coordenador. Use o usuário e senha fornecidos.");
	        return mv;
	    }

	    ModelAndView modelAndView = new ModelAndView("login/" + tipo.toLowerCase() + "/cadastro");
	    modelAndView.addObject("tipoSelecionado", tipo);
	    modelAndView.addObject("usuario", new Usuario());
	    return modelAndView;
	}

	@PostMapping("/salvarUsuario")
	public ModelAndView cadastrar(@RequestParam("tipo") String tipoStr, Usuario usuario,
			@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "curso", required = false) String curso) throws Exception {

		try {
			// Verifica se o tipo de usuário é válido
			TipoUsuario tipo = TipoUsuario.valueOf(tipoStr);
			usuario.setTipo(tipo);
			serviceUsuario.salvarUsuario(usuario); // Salva o usuário no banco

			// Se for coordenador, cria um coordenador com os dados adicionais
			if (tipo == TipoUsuario.COORDENADOR) {
				// Verifica se os dados do coordenador foram passados
				if (nome != null && curso != null) {
					DadosCadastroCoordenador dados = new DadosCadastroCoordenador(nome, curso);
					Coordenador coordenador = new Coordenador(dados);
					coordenador.setNome(nome); // Nome do coordenador
					coordenador.setCurso(curso); // Curso que o coordenador administra

					// Gerar um token de validação único
					String tokenValidacao = UUID.randomUUID().toString();
					coordenador.setTokenValidacao(tokenValidacao);

					// Salvar coordenador no banco de dados
					coordenadorRepository.save(coordenador);
					System.out.print("cheguei aqui");

					/*
					 * // Enviar o e-mail de notificação para validação do coordenador
					 * emailService.notificarCadastroCoordenador(nome, curso, usuario.getEmail(),
					 * tokenValidacao); System.out.println("email enviado");
					 */
				}
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/cadastro?tipo=" + tipoStr);
		}

		return new ModelAndView("redirect:/login?tipo=" + tipoStr);
	}

	@PostMapping("/login")
	public ModelAndView login(@Valid Usuario usuario, BindingResult br, @RequestParam("tipo") String tipoStr,
			HttpSession session) throws NoSuchAlgorithmException, ServiceExc {
		ModelAndView modelAndView = new ModelAndView("login/" + tipoStr + "/login");

		modelAndView.addObject("usuario", new Usuario());

		TipoUsuario tipo = null;
		try {
			tipo = TipoUsuario.valueOf(tipoStr);
			modelAndView.addObject("tipoSelecionado", tipo);
		} catch (Exception e) {
			modelAndView.addObject("msg", "Tipo de usuário inválido.");
			return modelAndView;
		}

		if (br.hasErrors()) {
			return modelAndView;
		}

		Usuario userLogin = serviceUsuario.loginUser(usuario.getUser(), Util.md5(usuario.getSenha()));

		if (userLogin == null || userLogin.getTipo() != tipo) {
			modelAndView.addObject("msg", "Usuário ou Tipo incorreto. Tente novamente.");
			return modelAndView;
		}

		session.setAttribute("usuarioLogado", userLogin);
		if (userLogin.getTipo() == TipoUsuario.ALUNO) {
			return new ModelAndView("redirect:/aluno/redirecionar");
		}
		if (userLogin.getTipo() == TipoUsuario.COORDENADOR) {
			return new ModelAndView("redirect:/coordenador/index");
		}
		return new ModelAndView("redirect:/");

	}

	@PostMapping("/deletar-conta")
	@Transactional
	public ModelAndView deletarConta(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		if (usuario != null) {
			if (usuario.getTipo() == TipoUsuario.ALUNO) {
				alunoRepository.deleteByUsuarioId(usuario.getId());
			}

			usuarioRepository.deleteById(usuario.getId());
			session.invalidate();
		}

		return new ModelAndView("redirect:/");
	}

	@PostMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
}