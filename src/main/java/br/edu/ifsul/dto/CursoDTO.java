package br.edu.ifsul.dto;

import java.time.LocalDate;

import br.edu.ifsul.entity.Categoria;
import br.edu.ifsul.entity.Curso;
import br.edu.ifsul.entity.Professor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class CursoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFinal;
    private String foto;

    @JsonIgnoreProperties("cursos")
    private ProfessorCursoDTO professor;
    private CategoriaCursoDTO categoria;

    public CursoDTO() {
    }

    public CursoDTO(Long id, String nome, String descricao, LocalDate dataInicio, LocalDate dataFinal, String foto) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.foto = foto;
    }

    public CursoDTO(Curso entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.dataInicio = entity.getDataInicio();
        this.dataFinal = entity.getDataFinal();
        this.foto = entity.getFoto();
        professor = new ProfessorCursoDTO(entity.getProfessor());
        categoria = new CategoriaCursoDTO(entity.getCategoria());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public String getFoto() {
        return foto;
    }

    public ProfessorCursoDTO getProfessor() {
        return professor;
    }

    public CategoriaCursoDTO getCategoria() {
        return categoria;
    }
}
