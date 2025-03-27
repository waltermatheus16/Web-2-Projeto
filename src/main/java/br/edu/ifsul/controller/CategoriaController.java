package br.edu.ifsul.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifsul.dto.CategoriaDTO;
import br.edu.ifsul.entity.Categoria;
import br.edu.ifsul.repository.CategoriaRepository;
import br.edu.ifsul.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Método para exibir os detalhes de uma categoria específica
    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("categoria/detalhes");
        CategoriaDTO dto = categoriaService.findById(id);
        mv.addObject("categoria", dto);
        return mv;
    }

    // Método para listar todas as categoria
    @GetMapping("/listar")
    public ModelAndView listar(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("categoria/listar");
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CategoriaDTO> pageResult = categoriaService.findAll(pageable);
            mv.addObject("categoria", pageResult);
        } catch (Exception e) {
            mv.addObject("errorMessage", "Ocorreu um erro ao carregar as categoria.");
            e.printStackTrace();
        }
        return mv;
    }

    // Método para editar uma categoria
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("categoria/formulario");
        // Se o id for 0, cria um novo DTO de categoria (para adicionar)
        CategoriaDTO categoriaDTO = (id == 0) ? new CategoriaDTO() : categoriaService.findById(id);
        mv.addObject("categoria", categoriaDTO);
        return mv;
    }

    // Método para processar a edição de uma categoria
    @PostMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, @ModelAttribute CategoriaDTO categoriaDTO) {
        try {
            Categoria categoria;

            // Verifica se o id é 0 (criação de nova categoria)
            if (id == 0) {
                categoria = new Categoria(); // Cria uma nova categoria
            } else {
                // Caso seja para editar uma categoria existente
                categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
            }

            // Atualiza os dados da categoria com os valores do DTO
            categoria.setNome(categoriaDTO.getNome());

            // Salva a categoria (nova ou editada)
            categoriaRepository.save(categoria);

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return "redirect:/categoria/listar"; // Redireciona para a lista em caso de erro
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/categoria/listar"; // Em caso de erro não esperado, redireciona para a lista
        }

        return "redirect:/categoria/listar"; // Redireciona para a lista de categorias após salvar
    }

    // Método para excluir uma categoria
    @GetMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) {
            return "redirect:/categoria/listar";
        }
        categoriaRepository.deleteById(id);
        return "redirect:/categoria/listar";
    }

    // Método para adicionar uma nova categoria
    @GetMapping("/adicionar")
    public ModelAndView adicionarCategoria() {
        ModelAndView mv = new ModelAndView("categoria/adicionar");
        mv.addObject("categoria", new CategoriaDTO()); // Passa um objeto vazio para o formulário
        return mv;
    }

    // Método para processar a adição de uma nova categoria
    @PostMapping("/adicionar")
    public String adicionarCategoria(@ModelAttribute CategoriaDTO categoriaDTO) {
        try {
            // Cria uma nova categoria
            Categoria categoria = new Categoria();
            categoria.setNome(categoriaDTO.getNome());

            // Salva a nova categoria
            categoriaRepository.save(categoria);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/categoria/listar"; // Em caso de erro, redireciona para a lista
        }

        return "redirect:/categoria/listar"; // Após adicionar, redireciona para a lista de categorias
    }
}
