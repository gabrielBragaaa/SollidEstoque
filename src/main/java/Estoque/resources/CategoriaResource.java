package Estoque.resources;

import Estoque.Entities.Categoria;
import Estoque.Entities.Fornecedor;
import Estoque.services.CategoriaService;
import Estoque.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categoria")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll() {
        List<Categoria> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id_categoria}")
    public ResponseEntity<Categoria> findById(@PathVariable Long id_categoria) {
        Categoria obj = service.findById(id_categoria);
        return ResponseEntity.ok().body(obj);
    }

}
