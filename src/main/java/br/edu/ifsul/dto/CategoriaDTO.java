package br.edu.ifsul.dto;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.entity.Categoria;
import br.edu.ifsul.entity.Curso;

public class CategoriaDTO {
    private Long id;
    private String nome;

    private List<CursoDTO> cursos = new ArrayList<>();

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CategoriaDTO(Categoria entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        for(Curso cur : entity.getCursos()) {
            cursos.add(new CursoDTO(cur));
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<CursoDTO> getCursos() {
        return cursos;
    }    
}

