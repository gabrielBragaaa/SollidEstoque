package Estoque.services;

import Estoque.entities.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Relatorio {

    @Autowired
    private ProdutoService produtoService;

    public List<Produto> getProdutosComBaixoEstoque() {
        return produtoService.findAll().stream()
                .filter(p -> p.getQuantidade_inicial() < 10)
                .collect(Collectors.toList());
    }

    public List<Produto> getProdutosComValidadeProxima() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30); // produtos com validade nos próximos 30 dias

        return produtoService.findAll().stream()
                .filter(p -> p.getValidade() != null && !p.getValidade().isBefore(hoje) && !p.getValidade().isAfter(limite))
                .collect(Collectors.toList());
    }



    // Aqui virão os próximos relatórios
}
