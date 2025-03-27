package br.edu.ifsul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Mapeamento para /entrada/home
    @GetMapping("/entrada/home")
    public String home() {
        return "entrada/home"; // Retorna a página home.html que está dentro da pasta entrada
    }
}
