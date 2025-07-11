package br.edu.gustavoign.carometro.aluno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlunoService {

	@Autowired
	private AlunoRepository repository;
	
	public List<Aluno> getAllAlunos() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Aluno getAlunoById (Long id) {
		return repository.getReferenceById(id);
	}
}