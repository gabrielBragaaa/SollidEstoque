<<<<<<< HEAD
package Estoque.repositories;

import Estoque.entities.Categoria;
import Estoque.entities.Fornecedor;
import Estoque.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//uma interface que estenda JpaRepository isso simplifica o acesso ao banco de dados, pois o Spring gera automaticamete implementações para operacões CRUD.
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    //Buscar por nome
    Optional<Produto> findByNome(String nome);

    //Buscar por CODIGO
    Optional<Produto> findByCodigo(String codigo);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaNomeContainingIgnoreCase(String nome);

    @Query("SELECT p FROM Produto p " +
            "WHERE (:texto IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :texto, '%')) " +
            "   OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
            "AND (:categoria IS NULL OR p.categoria = :categoria) " +
            "AND (:fornecedor IS NULL OR p.fornecedor = :fornecedor)")
    List<Produto> buscarComFiltros(@Param("texto") String texto,
                                   @Param("categoria") Categoria categoria,
                                   @Param("fornecedor") Fornecedor fornecedor);

}
=======
package Estoque.repositories;

import Estoque.entities.Categoria;
import Estoque.entities.Fornecedor;
import Estoque.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT p FROM Produto p " +
            "WHERE (:texto IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :texto, '%')) " +
            "   OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
            "AND (:categoria IS NULL OR p.categoria = :categoria) " +
            "AND (:fornecedor IS NULL OR p.fornecedor = :fornecedor)")
    List<Produto> buscarComFiltros(@Param("texto") String texto,
                                   @Param("categoria") Categoria categoria,
                                   @Param("fornecedor") Fornecedor fornecedor);

}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
