package br.edu.ifsul.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifsul.dto.CursoDTO;
import br.edu.ifsul.dto.ProfessorDTO;
import br.edu.ifsul.entity.Curso;
import br.edu.ifsul.entity.Professor;
import br.edu.ifsul.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    @Transactional(readOnly = true)
    public ProfessorDTO findById(Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        return new ProfessorDTO(professor.get());
    }

    @Transactional(readOnly = true)
    public Page<ProfessorDTO> findAll(Pageable pageable) {
        Page<Professor> professors = professorRepository.findAll(pageable);
        return professors.map(ProfessorDTO::new);
    }

    @Transactional
    public ProfessorDTO insert(ProfessorDTO dto) {
        Professor professor = new Professor();
        copyDtoToEntity(dto, professor);
        professorRepository.save(professor);
        return new ProfessorDTO(professor);
    }

    @Transactional
    public ProfessorDTO update(Long id, ProfessorDTO dto) {
        try {
            Professor professor = professorRepository.getReferenceById(id);
            copyDtoToEntity(dto, professor);

            professor = professorRepository.save(professor);
            return new ProfessorDTO(professor);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Professor não encontrado");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new EntityNotFoundException("Professor não encontrado");
        }
        try {
            professorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro ao excluir professor. Ele pode estar relacionado a outros registros.");
        }
    }


    private void copyDtoToEntity(ProfessorDTO dto, Professor entity) {
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setFoto(dto.getFoto());
        if(dto.getCursos() != null) {
            for (CursoDTO c : dto.getCursos()) {
                Curso curso = new Curso();
                curso.setNome(c.getNome());
                curso.setDescricao(c.getDescricao());
                curso.setDataInicio(c.getDataInicio());
                curso.setDataFinal(c.getDataFinal());
                entity.getCursos().add(curso);
            }
        }
    }
}
