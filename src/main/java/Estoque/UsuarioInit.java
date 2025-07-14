package Estoque;

import Estoque.entities.Usuario;
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
        if (usuarioRepository.findByUsername("erika.ferreira").isEmpty()) {
            String senhaCriptografada = new BCryptPasswordEncoder().encode("1234");

            Usuario user = new Usuario();
            user.setUsername("erika.ferreira");
            user.setPassword(senhaCriptografada);
            user.setRole("GUEST");//Tipos de users ADMIN ou GUEST

            usuarioRepository.save(user);

            System.out.println("Usuário criado com sucesso.");
        } else {
            System.out.println("Usuário já existe.");
        }
    }
}

