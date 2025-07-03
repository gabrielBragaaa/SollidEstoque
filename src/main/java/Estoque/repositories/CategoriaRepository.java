<<<<<<< HEAD
package Estoque.repositories;

import Estoque.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

//uma interface que estenda JpaRepository. Isso simplifica o acesso ao banco de dados, pois o Spring gera automaticamente implementações para operações CRUD.
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
=======
package Estoque.repositories;

import Estoque.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

//uma interface que estenda JpaRepository. Isso simplifica o acesso ao banco de dados, pois o Spring gera automaticamente implementações para operações CRUD.
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
