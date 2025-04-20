package br.edu.gustavoign.carometro.aluno;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	Aluno findByUsuarioId(Long usuarioId);
	void deleteByUsuarioId(Long usuarioId);
	List<Aluno> findByValidadoTrue(Sort sort);
	List<Aluno> findByValidadoFalse();
}