package back_ptitulo.controller;

import back_ptitulo.model.Centroestudio;
import back_ptitulo.repository.CentroEstudiosRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {

    private final CentroEstudiosRepository repository;

    public TestController(CentroEstudiosRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Centroestudio> getCentros() {
        return repository.findAll();
    }
}