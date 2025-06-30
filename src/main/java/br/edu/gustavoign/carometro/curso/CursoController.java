package br.edu.gustavoign.carometro.curso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.gustavoign.carometro.aluno.AlunoRepository;
import br.edu.gustavoign.carometro.coordenador.Coordenador;
import br.edu.gustavoign.carometro.coordenador.CoordenadorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoRepository repository;
    @Autowired
    private CoordenadorRepository coordenadorRepository;
	@Autowired
	private AlunoRepository alunoRepository;

    // Listar todos os cursos
    @GetMapping("/gerenciar")
    public String listarCursos(Model model) {
        model.addAttribute("cursos", repository.findAll());
        return "curso/gerenciar";
    }

    // Formulário de novo curso ou edição
    @GetMapping("/formulario")
    public String carregarFormulario(@RequestParam(required = false) Long id, Model model) {
        Curso curso = (id != null) ? repository.findById(id).orElse(new Curso()) : new Curso();
        model.addAttribute("curso", curso);
        model.addAttribute("coordenadores", coordenadorRepository.findAll());
        return "curso/formulario";
    }
    
    @GetMapping("/opcoes-curso")
    public String opcoesCurso() {
        return "curso/opcoes-curso";
    }
    

    @PostMapping
    @Transactional
    public String cadastrarOuAtualizar(@RequestParam(required = false) Long id,
                                       @RequestParam String nome, 
                                       @RequestParam String anoImplementacao,
                                       @RequestParam Long coordId) {
        Coordenador coordenador = coordenadorRepository.getReferenceById(coordId);

        if (id != null && repository.existsById(id)) {
            Curso cursoExistente = repository.getReferenceById(id);
            cursoExistente.setCoord(coordenador);
            cursoExistente.atualizarInformacoes(new DadosAtualizacaoCurso(id, nome, anoImplementacao, coordId));
        } else {
            Curso novoCurso = new Curso(new DadosCadastroCurso(nome, anoImplementacao), coordenador);
            repository.save(novoCurso);
        }

        return "redirect:/curso/gerenciar";
    }
    
    @PutMapping
    @Transactional
    public String atualizar(@Valid DadosAtualizacaoCurso dados) {
        var curso = repository.getReferenceById(dados.id());
        Coordenador coordenador = coordenadorRepository.getReferenceById(dados.coordId());
        curso.setCoord(coordenador);
        curso.atualizarInformacoes(dados);
        return "redirect:/curso/gerenciar";
    }
    
    // Excluir curso
    @PostMapping("/excluir")
    @Transactional
    public String deletarCurso(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        long count = alunoRepository.countByCursoIdAndValidadoTrue(id);

        if (count > 0) {
            redirectAttributes.addFlashAttribute("erro", "Este curso possui alunos válidos e não pode ser excluído.");
            return "redirect:/curso/gerenciar";
        }

        // Deleta todos os alunos inválidos associados ao curso
        alunoRepository.deleteByCursoIdAndValidadoFalse(id);

        // Agora sim deleta o curso
        repository.deleteById(id);
        return "redirect:/curso/gerenciar";
    }
}