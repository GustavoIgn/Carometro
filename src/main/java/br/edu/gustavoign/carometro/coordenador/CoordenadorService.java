package br.edu.gustavoign.carometro.coordenador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CoordenadorService {

	@Autowired
	private CoordenadorRepository repository;
	
	public List<Coordenador> getAllCoordenadors() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Coordenador getCoordenadorById (Long id) {
		return repository.getReferenceById(id);
	}
}