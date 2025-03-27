package br.edu.ifsul.service;

import java.util.List;
import java.util.Optional;

import br.edu.ifsul.entity.Curso;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifsul.dto.CategoriaDTO;
import br.edu.ifsul.dto.CursoDTO;
import br.edu.ifsul.entity.Categoria;
import br.edu.ifsul.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return new CategoriaDTO(categoria.get());
    }

    @Transactional(readOnly = true)
    public Page<CategoriaDTO> findAll(Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findAll(pageable);
        return categorias.map(CategoriaDTO::new);   
    }

    @Transactional
    public CategoriaDTO insert(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        copyDtoToEntity(dto, categoria);
        categoriaRepository.save(categoria);
        return new CategoriaDTO(categoria);
    }

    @Transactional
    public CategoriaDTO update(Long id, CategoriaDTO dto) {
        try {
            Categoria categoria = categoriaRepository.getReferenceById(id);
            copyDtoToEntity(dto, categoria);

            categoria = categoriaRepository.save(categoria);
            return new CategoriaDTO(categoria);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Categoria nao encontrada");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria nao encontrada");
        }
        try {
            categoriaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Violacion en la integridad de los datos");
        }
    }

    private void copyDtoToEntity(CategoriaDTO dto, Categoria entity) {
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
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
