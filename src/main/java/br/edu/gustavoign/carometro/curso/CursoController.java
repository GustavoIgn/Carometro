package br.edu.gustavoign.carometro.curso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoRepository repository;
    @Autowired
    private CursoService service;

    // Listar todos os cursos
    @GetMapping("/gerenciar")
    public String listarCursos(Model model) {
        model.addAttribute("cursos", repository.findAll());
        return "curso/gerenciar-cursos";
    }

    // Formulário de novo curso ou edição
    @GetMapping("/formulario")
    public String carregarFormulario(@RequestParam(required = false) Long id, Model model) {
        Curso curso = (id != null) ? repository.findById(id).orElse(new Curso()) : new Curso();
        model.addAttribute("curso", curso);
        return "curso/formulario";
    }

    @PostMapping
	@Transactional
	public String cadastrar(@Valid DadosCadastroCurso dados) {
		Curso curso = new Curso(dados);

		Curso existente = service.getCursoById(curso.getId());

		if (existente != null) {
			atualizar(new DadosAtualizacaoCurso(existente.getId(), dados.nome(), dados.anoImplementacao()));
			return "redirect:/curso/gerenciar";
		} else {
			repository.save(existente);
			return "redirect:/curso/gerenciar";
		}
	}

	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoCurso dados) {
		var curso = repository.getReferenceById(dados.id());
		curso.atualizarInformacoes(dados);
		return "redirect:curso/gerenciar";
	}

    // Excluir curso
    @PostMapping("/excluir")
    @Transactional
    public String excluirCurso(@RequestParam Long id) {
        repository.deleteById(id);
        return "redirect:/curso/gerenciar";
    }
}