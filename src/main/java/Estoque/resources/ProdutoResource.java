<<<<<<< HEAD
package Estoque.resources;

import Estoque.entities.Produto;
import Estoque.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")//Controlador REST
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        List<Produto> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id_produto}")
    public ResponseEntity<Produto> findById(@PathVariable Long id_produto){
        Produto obj =  service.findById(id_produto);
        return ResponseEntity.ok().body(obj);
    }
    @GetMapping(value = "/nome/{nome}")
    public ResponseEntity<Produto> findByName(@PathVariable String nome){
        Produto obj =  service.findByName(nome);
        return ResponseEntity.ok().body(obj);
    }
    @GetMapping(value = "/codigo/{codigo}")
    public ResponseEntity<Produto> findByCodigo(@PathVariable String codigo){
        Produto obj =  service.findByCodigo(codigo);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Produto> insert(@RequestBody Produto produto){
        produto = service.insert(produto);
        return ResponseEntity.ok().body(produto);
    }
    @DeleteMapping(value = "/{id_produto}")
    public ResponseEntity<Void> delete(@PathVariable Long id_produto){
        service.delete(id_produto);
        return ResponseEntity.noContent().build();
    }


}
=======
package Estoque.resources;

import Estoque.entities.Produto;
import Estoque.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")//Controlador REST
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        List<Produto> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id_produto}")
    public ResponseEntity<Produto> findById(@PathVariable Long id_produto){
        Produto obj =  service.findById(id_produto);
        return ResponseEntity.ok().body(obj);
    }
    @GetMapping(value = "/nome/{nome}")
    public ResponseEntity<Produto> findByName(@PathVariable String nome){
        Produto obj =  service.findByName(nome);
        return ResponseEntity.ok().body(obj);
    }
    @GetMapping(value = "/codigo/{codigo}")
    public ResponseEntity<Produto> findByCodigo(@PathVariable String codigo){
        Produto obj =  service.findByCodigo(codigo);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Produto> insert(@RequestBody Produto produto){
        produto = service.insert(produto);
        return ResponseEntity.ok().body(produto);
    }
    @DeleteMapping(value = "/{id_produto}")
    public ResponseEntity<Void> delete(@PathVariable Long id_produto){
        service.delete(id_produto);
        return ResponseEntity.noContent().build();
    }


}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
