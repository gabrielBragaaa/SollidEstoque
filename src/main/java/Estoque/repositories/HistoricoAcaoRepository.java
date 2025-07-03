<<<<<<< HEAD
package Estoque.repositories;

import Estoque.entities.HistoricoAcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoAcaoRepository extends JpaRepository<HistoricoAcao, Long> {

    @Query("SELECT DISTINCT u.username FROM HistoricoAcao h JOIN h.usuario u")
    List<String> findDistinctUsernames();

    @Query("SELECT h FROM HistoricoAcao h JOIN FETCH h.usuario u WHERE u.username = :usuario")
    List<HistoricoAcao> findByUsuario(@Param("usuario") String usuario);

    @Query("SELECT h FROM HistoricoAcao h JOIN FETCH h.usuario u WHERE h.dataHora BETWEEN :inicio AND :fim")
    List<HistoricoAcao> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT h FROM HistoricoAcao h JOIN FETCH h.usuario u WHERE u.username = :usuario AND h.dataHora BETWEEN :inicio AND :fim")
    List<HistoricoAcao> findByUsuarioAndDataBetween(@Param("usuario") String usuario, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);


}

=======
package Estoque.repositories;

import Estoque.entities.HistoricoAcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoAcaoRepository extends JpaRepository<HistoricoAcao, Long> {
}

>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
