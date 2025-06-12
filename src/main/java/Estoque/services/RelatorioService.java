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
}
