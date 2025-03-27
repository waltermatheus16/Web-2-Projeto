package br.edu.ifsul.dto;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsul.entity.Professor;

public class ProfessorDTO {
    private Long id;
    private String nome;
    private String email;
    private String foto;
    private List <CursoDTO> cursos = new ArrayList<>();

    public List<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoDTO> cursos) {
        this.cursos = cursos;
    }

    public ProfessorDTO() {}

    public ProfessorDTO(Long id, String nome, String email, String foto) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.foto = foto;
    }

    public ProfessorDTO(Professor entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        this.foto = entity.getFoto();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
