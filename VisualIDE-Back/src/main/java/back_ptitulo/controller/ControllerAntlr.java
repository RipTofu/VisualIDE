package back_ptitulo.controller;

import back_ptitulo.service.ServiceParseador;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class ControllerAntlr {

    @PostMapping("/api/parse")
    public String parsear(@RequestBody String content) {
        System.out.println("Conectado a controlador...");
        ServiceParseador parseadorPruebas = new ServiceParseador();
        return parseadorPruebas.parse(content);

    }
}