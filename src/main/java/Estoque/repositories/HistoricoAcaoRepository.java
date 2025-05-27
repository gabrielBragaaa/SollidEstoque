package Estoque.repositories;

import Estoque.entities.HistoricoAcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoAcaoRepository extends JpaRepository<HistoricoAcao, Long> {
}

