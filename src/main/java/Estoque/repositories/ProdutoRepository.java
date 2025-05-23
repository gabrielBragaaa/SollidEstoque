package Estoque.repositories;

import Estoque.Entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpOr;

import java.util.List;
import java.util.Optional;

//uma interface que estenda JpaRepository. Isso simplifica o acesso ao banco de dados, pois o Spring gera automaticamente implementações para operações CRUD.
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    //Buscar por nome
    Optional<Produto> findByNome(String nome);

    //Buscar por CODIGO
    Optional<Produto> findByCodigo(String codigo);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaNomeContainingIgnoreCase(String nome);




}
