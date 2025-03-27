package br.edu.ifsul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifsul.entity.Categoria;   

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
