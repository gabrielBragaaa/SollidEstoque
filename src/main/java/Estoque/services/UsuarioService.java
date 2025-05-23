package Estoque.services;

import Estoque.Entities.Usuario;
import Estoque.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private HistoricoAcaoService historicoAcaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public boolean autenticar(String username, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return new BCryptPasswordEncoder().matches(senha, usuario.getPassword());

        }
        return false;
    }

    //Altarar senha
    public boolean alterarSenha(String username, String senhaAntiga, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (new BCryptPasswordEncoder().matches(senhaAntiga, usuario.getPassword())) {
                usuario.setPassword(new BCryptPasswordEncoder().encode(novaSenha));
                usuarioRepository.save(usuario);

                historicoAcaoService.registrarAcao(
                        "ALTERAÇÃO DE SENHA",
                        "Usuario",
                        "Senha alterada com sucesso",
                        usuario
                );
                return true;
            }
        }
        return false;
    }
}