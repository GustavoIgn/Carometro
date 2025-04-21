package br.edu.gustavoign.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.edu.gustavoign.carometro.curso.Curso;
import br.edu.gustavoign.carometro.curso.CursoRepository;
import br.edu.gustavoign.carometro.usuario.Usuario;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

	@Autowired
	private AlunoRepository repository;
	
	@Autowired
	private CursoRepository cursoRepository;

	// Após o Login do Aluno, formulário de dados para preencher a entidade Aluno.
	// Caso já tenha um registro de aluno para este usuário, ele traz o aluno
	// existente com o mesmo id.
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model, HttpSession session) {
	    Aluno aluno;
	    if (id != null) {
	        aluno = repository.findById(id).orElse(new Aluno());
	    } else {
	        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
	        aluno = new Aluno();
	        aluno.setUsuario(usuarioLogado);
	    }

	    List<Curso> cursos = cursoRepository.findAll();
	    model.addAttribute("aluno", aluno);
	    model.addAttribute("cursos", cursos); // Envia a lista de cursos para o HTML

	    return "aluno/formulario";
	}

	// Ao clicar em cadastrar, o sistema busca se já existe aluno e faz update, caso
	// contrário faz insert e chama a página meus dados.
	@PostMapping
	@Transactional
	public String cadastrar(@Valid DadosCadastroAluno dados, HttpSession session) {
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
		Curso curso = cursoRepository.findById(dados.cursoId()).orElse(null);

		if (curso == null) {
			// pode redirecionar com mensagem de erro ou lançar exceção
			return "redirect:/aluno/formulario?erro=curso_nao_encontrado";
		}

		Aluno existente = repository.findByUsuarioId(usuarioLogado.getId());

		if (existente != null) {
			existente.atualizarInformacoes(new DadosAtualizacaoAluno(
					existente.getId(), dados.nome(), dados.RA(), dados.cursoId(),
					dados.anoIngresso(), dados.numeroCelular(), dados.links(),
					dados.comentarioHistorico(), dados.comentarioFatec(),
					dados.comentarioLivre(), dados.foto()));

			existente.setCurso(curso);
			return "redirect:/aluno/meus-dados";
		} else {
			Aluno novoAluno = new Aluno(dados, usuarioLogado, curso);
			repository.save(novoAluno);
			return "redirect:/aluno/meus-dados";
		}
	}

	// Remove o Aluno vinculado ao usuario e volta ao formulário para preencher
	// novamente.
	@DeleteMapping
	@Transactional
	public String removeAluno(Long id) {
		repository.deleteById(id);
		return "redirect:aluno/formulario";
	}

	//Leva até a pagina que o usuario pode visualizar seus dados de aluno.
	@GetMapping("/meus-dados")
	public String meusDados(Model model, HttpSession session) {
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

		Aluno aluno = repository.findByUsuarioId(usuarioLogado.getId());
		model.addAttribute("aluno", aluno);

		return "aluno/meus-dados";
	}

	// Redireciona para as devidas telas de acordo com o usuario, seja login, novo
	// formulário e tela de meus dados.
	@GetMapping("/redirecionar")
	public String redirecionar(HttpSession session) {
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

		if (usuarioLogado == null) {
			return "redirect:/login?tipo=ALUNO";
		}

		Aluno aluno = repository.findByUsuarioId(usuarioLogado.getId());

		if (aluno == null) {
			return "redirect:/aluno/formulario";
		} else {
			return "redirect:/aluno/meus-dados";
		}
	}

	// Alunos pendentes ou dito como inválidos pelo coordenador.
	@GetMapping("/pendentes")
	public List<Aluno> listarPendentes() {
		return repository.findByValidadoFalse();
	}

	// Validar o aluno, alterar o status da entidade e salvar novamente.
	@PutMapping("/validar/{id}")
	public ResponseEntity<?> validarAluno(@PathVariable Long id) {
		Optional<Aluno> alunoOptional = repository.findById(id);
		if (alunoOptional.isPresent()) {
			Aluno aluno = alunoOptional.get();
			aluno.setValidado(true);
			repository.save(aluno);
			return ResponseEntity.ok("Aluno validado com sucesso.");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Alunos validados e ordenados por nome.
	@GetMapping("/validados")
	public List<Aluno> listarValidados() {
		return repository.findByValidadoTrue(Sort.by("nome").ascending());
	}

	// Conversão da imagem do alunos em bytes aceitaveis pelo html.
	@GetMapping("/foto/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> exibirFoto(@PathVariable Long id) {
		Aluno aluno = repository.findById(id).orElse(null);

		if (aluno == null || aluno.getFoto() == null) {
			return ResponseEntity.notFound().build();
		}

		try {
			byte[] imagemBytes = aluno.getFoto().getBytes(1, (int) aluno.getFoto().length());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE).body(imagemBytes);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/listar")
	public String listarAlunos(@RequestParam(required = false) Long cursoId, Model model) {
	    List<Curso> cursos = cursoRepository.findAll();
	    List<Aluno> alunos;

	    // Se o cursoId for fornecido, buscamos alunos do curso específico e que estão validados
	    if (cursoId != null) {
	        alunos = repository.findByCursoIdAndValidadoTrueOrderByNomeAsc(cursoId);
	    } else {
	        // Se não, buscamos todos os alunos validados
	        alunos = repository.findByValidadoTrueOrderByNomeAsc();
	    }

	    model.addAttribute("lista", alunos);
	    model.addAttribute("cursos", cursos);
	    model.addAttribute("cursoSelecionado", cursoId); // Mantém o curso selecionado para exibição no dropdown
	    return "home/index";
	}
}
