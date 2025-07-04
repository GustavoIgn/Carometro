package br.edu.gustavoign.carometro.curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Curso findByCoordId(Long id);
}