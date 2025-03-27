package br.edu.ifsul.dto;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.entity.Categoria;
import br.edu.ifsul.entity.Curso;

public class CategoriaDTO {
    private Long id;
    private String nome;
    private List<CursoDTO> cursos = new ArrayList<>();

    // Construtor sem argumentos
    public CategoriaDTO() {
    }

    // Construtor com argumentos
    public CategoriaDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Construtor que recebe uma entidade Categoria
    public CategoriaDTO(Categoria entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        // Mapeia os cursos da Categoria para a lista de CursoDTOs
        for(Curso cur : entity.getCursos()) {
            cursos.add(new CursoDTO(cur));
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoDTO> cursos) {
        this.cursos = cursos;
    }
}
