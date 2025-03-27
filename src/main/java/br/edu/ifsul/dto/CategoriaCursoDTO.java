package br.edu.ifsul.dto;

import br.edu.ifsul.entity.Categoria;

public class CategoriaCursoDTO {
    private Long id;
    private String nome;

    public CategoriaCursoDTO() {
    }

    public CategoriaCursoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CategoriaCursoDTO(Categoria entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}