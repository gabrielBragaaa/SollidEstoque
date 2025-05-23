package Estoque.services;

import Estoque.Entities.Produto;
import Estoque.repositories.ProdutoRepository;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private HistoricoAcaoService historicoAcaoService;

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Produto findById(Long id_produto) {
        Optional<Produto> obj = repository.findById(id_produto);
        return obj.get();
    }

    public Produto findByName(String nome) {
        Optional<Produto> obj = repository.findByNome(nome);
        return obj.get();
    }

    public Produto findByCodigo(String codigo) {
        Optional<Produto> obj = repository.findByCodigo(codigo);
        return obj.get();
    }

    public Produto insert(Produto produto) {
        Optional<Produto> obj = repository.findByCodigo(produto.getCodigo());
        if (obj.isPresent()){
            Produto existente = obj.get();
            existente.setQuantidade_inicial(
                    existente.getQuantidade_inicial() + produto.getQuantidade_inicial()
            );
            System.out.println("Quantidade atualizada deste produto.");
            return repository.save(existente);


        }else {
            System.out.println();;
            System.out.println("Novo produto cadastrado.");
            return repository.save(produto);

        }
    }


    public void delete(Long id_produto) {
        repository.deleteById(id_produto);

    }
}