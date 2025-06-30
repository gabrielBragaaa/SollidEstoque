package Estoque.services;

import Estoque.entities.HistoricoAcao;
import Estoque.entities.Usuario;
import Estoque.repositories.HistoricoAcaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class HistoricoAcaoService {

    @Autowired
    private HistoricoAcaoRepository historicoRepository;

    @Transactional
    public void registrarAcao(String acao, String entidadeAfetada, String detalhes, Usuario usuario) {
        HistoricoAcao historico = new HistoricoAcao(
                acao,
                entidadeAfetada,
                detalhes,
                LocalDateTime.now(),
                usuario
        );
        historicoRepository.save(historico);
    }
}
