package com.mycompany.teamproject9.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/")
    public String index() {


        return "suksuk/home";
    }

    @GetMapping("/create")
    public String create() {

        return "create";

    }
}
