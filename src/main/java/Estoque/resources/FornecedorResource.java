package Estoque.resources;

import Estoque.entities.Fornecedor;
import Estoque.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/fornecedores")
public class FornecedorResource {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public ResponseEntity<List<Fornecedor>> findAll() {
        List<Fornecedor> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id_fornecedor}")
    public ResponseEntity<Fornecedor> findById(@PathVariable Long id_fornecedor) {
        Fornecedor obj = service.findById(id_fornecedor);
        return ResponseEntity.ok().body(obj);
    }


}
