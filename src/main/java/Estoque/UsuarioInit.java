package Estoque;

import Estoque.Entities.Usuario;
import Estoque.UsuarioInit;
import Estoque.repositories.UsuarioRepository;
import Estoque.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInit implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public UsuarioInit(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //Senha criptografada
    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByUsername("gabriel.braga").isEmpty()) {
            String senhaCriptografada = new BCryptPasswordEncoder().encode("s0ll1d");

            Usuario user = new Usuario();
            user.setUsername("gabriel.braga");
            user.setPassword(senhaCriptografada);
            user.setRole("ADMIN");

            usuarioRepository.save(user);

            System.out.println("Usuário admin criado com sucesso.");
        } else {
            System.out.println("Usuário admin já existe.");
        }
    }
}
