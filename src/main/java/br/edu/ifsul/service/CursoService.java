package br.edu.ifsul.service;

import br.edu.ifsul.dto.CursoDTO;
import br.edu.ifsul.entity.Categoria;
import br.edu.ifsul.entity.Curso;
import br.edu.ifsul.entity.Professor;
import br.edu.ifsul.repository.CategoriaRepository;
import br.edu.ifsul.repository.CursoRepository;
import br.edu.ifsul.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public CursoDTO findById(Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        return new CursoDTO(curso.get());
    }

    @Transactional(readOnly = true)
    public Page<CursoDTO> findAll(Pageable pageable) {
        Page<Curso> cursos = cursoRepository.findAll(pageable);
        return cursos.map(CursoDTO::new);
    }

    @Transactional
    public CursoDTO insert(CursoDTO dto) {
        Curso curso = new Curso();
        copyDtoToEntity(dto, curso);
        cursoRepository.save(curso);
        return new CursoDTO(curso);
    }

    @Transactional
    public CursoDTO update(Long id, CursoDTO dto) {
        try {
            Curso curso = cursoRepository.getReferenceById(id);
            copyDtoToEntity(dto, curso);

            curso = cursoRepository.save(curso);
            return new CursoDTO(curso);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Curso nao encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso nao encontrado");
        }
        try {
            cursoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Violacion en la integridad de los datos");
        }
    }

    private void copyDtoToEntity(CursoDTO dto, Curso entity) {
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFinal(dto.getDataFinal());
        entity.setFoto(dto.getFoto());

        Professor professor = professorRepository.getReferenceById(dto.getProfessor().getId());
        Categoria categoria = categoriaRepository.getReferenceById(dto.getCategoria().getId());

        entity.setProfessor(professor);
        entity.setCategoria(categoria);
    }
}
