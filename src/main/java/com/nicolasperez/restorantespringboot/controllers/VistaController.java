package com.nicolasperez.restorantespringboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/menu")
    public String menu() {
        return "menu";          // → templates/menu.html
    }

    @GetMapping("/despensa")
    public String despensa() {
        return "despensa";      // → templates/despensa.html
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/menu";
    }
}