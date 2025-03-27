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

import br.edu.ifsul.dto.ProfessorDTO;
import br.edu.ifsul.entity.Professor;
import br.edu.ifsul.repository.ProfessorRepository;
import br.edu.ifsul.service.ProfessorService;
import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProfessorRepository professorRepository;

    // Método para exibir os detalhes de um professor específico
    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("professor/detalhes");
        ProfessorDTO dto = professorService.findById(id);
        mv.addObject("professor", dto);
        return mv;
    }

    // Método para listar todos os professores
    @GetMapping("/listar")
    public ModelAndView listar(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("professor/listar");
        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<ProfessorDTO> pageResult = professorService.findAll(pageable);
            mv.addObject("professores", pageResult);
        } catch (Exception e) {
            mv.addObject("errorMessage", "Ocorreu um erro ao carregar os professores.");
            e.printStackTrace();
        }
        return mv;
    }

    // Método para editar um professor
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("professor/formulario");
        ProfessorDTO professorDTO = (id == 0) ? new ProfessorDTO() : professorService.findById(id);
        mv.addObject("professor", professorDTO);
        return mv;
    }

    // Método para processar a edição de um professor
    @PostMapping("/editar/{id}")
    public String editarProfessor(@PathVariable Long id, @ModelAttribute ProfessorDTO professorDTO) {
        try {
            Professor professor;
    
            // Verifica se o id é 0 (criação de novo professor)
            if (id == 0) {
                professor = new Professor(); // Cria um novo professor
            } else {
                // Caso seja para editar um professor existente
                professor = professorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com ID: " + id));
            }
    
            // Atualiza os dados do professor com os valores do DTO
            professor.setNome(professorDTO.getNome());
            professor.setEmail(professorDTO.getEmail());
            professor.setFoto(professorDTO.getFoto());
    
            // Salva o professor (novo ou editado)
            professorRepository.save(professor);
    
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return "redirect:/professor/listar"; // Redireciona para a lista em caso de erro
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/professor/listar"; // Em caso de erro não esperado, redireciona para a lista
        }
    
        return "redirect:/professor/listar"; // Redireciona para a lista de professores após salvar
    }


    @GetMapping("/excluir/{id}")
    public String excluirProfessor(@PathVariable Long id) {
        
        Optional<Professor> professor = professorRepository.findById(id);
        if(professor.isEmpty()){

            return "redirect:professor/listar";
        }
        professorRepository.deleteById(id);
        return "redirect:../../professor/listar";
    }


    @GetMapping("/adicionar")
public ModelAndView adicionarProfessor() {
    ModelAndView mv = new ModelAndView("professor/adicionar");
    mv.addObject("professor", new ProfessorDTO()); // Passa um objeto vazio para o formulário
    return mv;
}

// Método para processar a adição de um novo professor
@PostMapping("/adicionar")
public String adicionarProfessor(@ModelAttribute ProfessorDTO professorDTO) {
    try {
        // Cria um novo professor
        Professor professor = new Professor();
        professor.setNome(professorDTO.getNome());
        professor.setEmail(professorDTO.getEmail());
        professor.setFoto(professorDTO.getFoto());

        // Salva o novo professor
        professorRepository.save(professor);

    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/professor/listar"; // Em caso de erro, redireciona para a lista
    }

    return "redirect:/professor/listar"; // Após adicionar, redireciona para a lista de professores
}
    
    
}