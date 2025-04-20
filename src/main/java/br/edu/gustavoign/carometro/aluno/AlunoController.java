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
import org.springframework.web.bind.annotation.ResponseBody;

import br.edu.gustavoign.carometro.usuario.Usuario;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

	@Autowired
	private AlunoRepository repository;

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

		model.addAttribute("aluno", aluno);
		return "aluno/formulario";
	}

	@PostMapping
	@Transactional
	public String cadastrar(@Valid DadosCadastroAluno dados, HttpSession session) {
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
		Aluno aluno = new Aluno(dados, usuarioLogado);

		Aluno existente = repository.findByUsuarioId(usuarioLogado.getId());

		if (existente != null) {
			existente.atualizarInformacoes(new DadosAtualizacaoAluno(existente.getId(), dados.nome(), dados.RA(),
					dados.curso(), dados.anoIngresso(), dados.numeroCelular(), dados.links(),
					dados.comentarioHistorico(), dados.comentarioFatec(), dados.comentarioLivre(), dados.foto()));
			return "redirect:/aluno/meus-dados";
		} else {
			repository.save(aluno);
			return "redirect:/aluno/meus-dados";
		}
	}

	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAluno dados) {
		var aluno = repository.getReferenceById(dados.id());
		aluno.atualizarInformacoes(dados);
		return "redirect:aluno/meus-dados";
	}

	@DeleteMapping
	@Transactional
	public String removeAluno(Long id) {
		repository.deleteById(id);
		return "redirect:aluno/formulario";
	}

	@GetMapping("/meus-dados")
	public String meusDados(Model model, HttpSession session) {
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

		Aluno aluno = repository.findByUsuarioId(usuarioLogado.getId());
		model.addAttribute("aluno", aluno);

		return "aluno/meus-dados";
	}

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
	
    @GetMapping("/pendentes")
    public List<Aluno> listarPendentes() {
        return repository.findByValidadoFalse();
    }

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

    @DeleteMapping("/negar/{id}")
    public ResponseEntity<?> negarAluno(@PathVariable Long id) {
        Optional<Aluno> alunoOptional = repository.findById(id);
        if (alunoOptional.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok("Cadastro negado e aluno excluído.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/validados")
    public List<Aluno> listarValidados() {
        return repository.findByValidadoTrue(Sort.by("nome").ascending());
    }

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
}
