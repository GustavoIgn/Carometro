package br.edu.gustavoign.carometro.coordenador;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {
	Coordenador findByTokenValidacao(String token);
}