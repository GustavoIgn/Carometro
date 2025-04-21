package br.edu.gustavoign.carometro.curso;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCurso(@NotNull Long id, String nome, String anoImplementacao, Long coordId) {}