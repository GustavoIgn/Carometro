package br.edu.gustavoign.carometro.coordenador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.gustavoign.carometro.aluno.AlunoRepository;
import br.edu.gustavoign.carometro.curso.CursoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/coordenador")
public class CoordenadorController {

	@Autowired
	private CoordenadorRepository repository;
	@Autowired
	private CoordenadorService coordenadorService;
	@Autowired
	private AlunoRepository alunoRepository;
	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping("/index")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("coordenador", coordenadorService.getAllCoordenadors());
		if (id != null) {
			var coordenador = repository.getReferenceById(id);
			model.addAttribute("coordenador", coordenador);
		}
		return "coordenador/index";
	}

	@GetMapping("/coordenador/validar")
	public ModelAndView validarCoordenador(@RequestParam("token") String token) {
		Coordenador coordenador = repository.findByTokenValidacao(token);

		if (coordenador != null && !coordenador.isValido()) {
			coordenador.setValido(true); // Marcar como validado
			coordenador.setTokenValidacao(null); // Remover o token para evitar reuso
			repository.save(coordenador);

			// Página de confirmação
			return new ModelAndView("coordenador/validado");
		}

		// Caso o token seja inválido ou já usado
		return new ModelAndView("coordenador/invalido");
	}

	@GetMapping("/listagem-alunos")
	public String listarAlunos(@RequestParam(value = "cursoId", required = false) Long cursoId, Model model) {
	    if (cursoId != null) {
	        // Filtra os alunos pelo curso selecionado
	        model.addAttribute("lista", alunoRepository.findByCursoIdOrderByNomeAsc(cursoId));
	    } else {
	        // Exibe todos os alunos
	        model.addAttribute("lista", alunoRepository.findAll(Sort.by("nome").ascending()));
	    }
	    model.addAttribute("cursos", cursoRepository.findAll(Sort.by("nome").ascending())); // adiciona os cursos
	    model.addAttribute("cursoSelecionado", cursoId); // adiciona o curso selecionado no modelo
	    return "coordenador/listagem-alunos";
	}
	
	@PostMapping("/alterar-status")
	@Transactional
	public String alterarStatus(@RequestParam("id") Long id, Model model) {
	    var aluno = alunoRepository.findById(id).orElseThrow();
	    aluno.setValidado(!aluno.isValidado()); // Inverte o status do aluno
	    alunoRepository.save(aluno);
	    return "redirect:/coordenador/listagem-alunos";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid DadosCadastroCoordenador dados) {
		Coordenador coordenador = new Coordenador(dados);
		repository.save(coordenador);
		return "redirect:coordenador";
	}

	@DeleteMapping
	@Transactional
	public String removeCoordenador(Long id) {
		repository.deleteById(id);
		return "redirect:coordenador";
	}
}
