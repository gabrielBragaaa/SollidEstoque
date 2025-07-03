<<<<<<< HEAD
package Estoque.services;

import Estoque.entities.Usuario;
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

    // Slavamneto de usuario
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
    }
}
=======
package Estoque.services;

import Estoque.entities.Usuario;
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

    // Slavamneto de usuario
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
    }
}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
