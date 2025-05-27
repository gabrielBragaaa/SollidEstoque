package Estoque.repositories;

import Estoque.entities.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

//uma interface que estenda JpaRepository. Isso simplifica o acesso ao banco de dados, pois o Spring gera automaticamente implementações para operações CRUD.
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

}
