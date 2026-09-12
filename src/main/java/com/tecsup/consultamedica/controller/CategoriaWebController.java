package com.tecsup.consultamedica.controller;
import com.tecsup.consultamedica.model.CategoriaMedicamento;
import com.tecsup.consultamedica.service.CategoriaMedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/categorias")
public class CategoriaWebController {

    @Autowired
    private CategoriaMedicamentoService categoriaService;

    @GetMapping
    public String verLista(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("nuevaCategoria", new CategoriaMedicamento());
        return "categorias";
    }

    @PostMapping
    public String registrar(@RequestParam String nombre, @RequestParam(required = false) String descripcion) {
        CategoriaMedicamento categoria = new CategoriaMedicamento();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        categoriaService.registrar(categoria);
        return "redirect:/web/categorias";
    }
}