package br.edu.ifsul.dto;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.entity.Curso;
import br.edu.ifsul.entity.Professor;

public class ProfessorCursoDTO {
    private Long id;
    private String nome;
    private String email;
    private String foto;


    public ProfessorCursoDTO() {
    }

    public ProfessorCursoDTO(Long id, String nome, String email, String foto) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.foto = foto;
    }

    public ProfessorCursoDTO(Professor entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        this.foto = entity.getFoto();
    }


    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }
}
