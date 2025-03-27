package br.edu.ifsul.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("categoria/detalhes");
        CategoriaDTO dto = categoriaService.findById(id);
        mv.addObject("categoria", dto);
        return mv;
    }

    @GetMapping("/listar")
    public ModelAndView listar(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("categoria/listar");
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<CategoriaDTO> pageResult = categoriaService.findAll(pageable);
            mv.addObject("categorias", pageResult);
        } catch (Exception e) {
            mv.addObject("errorMessage", "Ocorreu um erro ao carregar as categorias.");
            e.printStackTrace();
        }
        return mv;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("categoria/formulario");
        CategoriaDTO categoriaDTO = id == null || id == 0 ? new CategoriaDTO() : categoriaService.findById(id);
        mv.addObject("categoria", categoriaDTO);
        return mv;
    }

    @PostMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, @ModelAttribute CategoriaDTO categoriaDTO) {
        try {
            Categoria categoria;
            if (id == 0) {
                categoria = new Categoria();
            } else {
                categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + id));
            }
            categoria.setNome(categoriaDTO.getNome());
            categoriaRepository.save(categoria);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/categoria/listar"; // Redireciona corretamente
        }
        return "redirect:/categoria/listar"; // Redireciona corretamente
    }


    @PostMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isEmpty()) {
            return "redirect:/categoria/listar"; // Caso a categoria não exista, redireciona para a lista
        }
        categoriaRepository.deleteById(id);
        return "redirect:/categoria/listar"; // Redireciona para a lista de categorias após excluir
    }
    


    @GetMapping("/adicionar")
    public ModelAndView adicionarCategoria() {
        ModelAndView mv = new ModelAndView("categoria/adicionar");
        mv.addObject("categoria", new CategoriaDTO());
        return mv;
    }

    @PostMapping("/adicionar")
    public String adicionarCategoria(@ModelAttribute CategoriaDTO categoriaDTO) {
        try {
            Categoria categoria = new Categoria();
            categoria.setNome(categoriaDTO.getNome());
            categoriaRepository.save(categoria);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/categoria/listar";
        }
        return "redirect:/categoria/listar";
    }
}
