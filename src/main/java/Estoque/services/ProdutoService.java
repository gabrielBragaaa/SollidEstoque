
package Estoque.services;

import Estoque.entities.Categoria;
import Estoque.entities.Fornecedor;
import Estoque.entities.Produto;
import Estoque.entities.Usuario;
import Estoque.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //Filtro de buscar
    public List<Produto> buscarComFiltros(String texto, Categoria categoria, Fornecedor fornecedor) {
        return repository.buscarComFiltros(
                (texto != null && !texto.trim().isEmpty()) ? texto.trim() : null,
                categoria,
                fornecedor
        );
    }

    @Transactional
    public void realizarSaida(Produto produto, int quantidade, Usuario usuario) {
        if (produto.getQuantidade_inicial() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para a saída");
        }

        produto.setQuantidade_inicial(produto.getQuantidade_inicial() - quantidade);
        repository.save(produto);

        historicoAcaoService.registrarAcao(
                "Saída por Venda",
                "Produto",
                "Produto '" + produto.getNome() + "' - Código: " + produto.getCodigo() + " | Quantidade retirada: " + quantidade,
                usuario
        );
    }

    public List<Produto> findByNomeOuCodigo(String termo) {
        return repository.findByNomeOrCodigo(termo);
    }
    public List<Produto> findByNomeContaining(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

   }