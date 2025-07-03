<<<<<<< HEAD
package Estoque.services;

import Estoque.entities.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ProdutoService produtoService;

    public List<Produto> getProdutosComBaixoEstoque() {
        return produtoService.findAll().stream()
                .filter(p -> p.getQuantidade_inicial() < 10)
                .collect(Collectors.toList());
    }
    public List<Produto> getTodosProdutos() {
        return produtoService.findAll();
    }
}
=======
package Estoque.services;

import Estoque.entities.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ProdutoService produtoService;

    public List<Produto> getProdutosComBaixoEstoque() {
        return produtoService.findAll().stream()
                .filter(p -> p.getQuantidade_inicial() < 10)
                .collect(Collectors.toList());
    }
    public List<Produto> getTodosProdutos() {
        return produtoService.findAll();
    }
}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
