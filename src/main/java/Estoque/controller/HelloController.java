package Estoque.controller;

import Estoque.config.AppContextProvider;
import Estoque.entities.Usuario;
import Estoque.services.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
@Controller
public class HelloController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginError;

    @FXML
    private Label loginSucces;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label msgLabel;

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private Button btnEntrar;


    @FXML
    public void initialize() {
        // ENTER no campo de usuário
        txtUsername.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnEntrar.fire();
            }
        });

        // ENTER no campo de senha
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnEntrar.fire();
            }
        });
    }

    @FXML
    public void onLoginClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String password = passwordField.getText();

        if (usuarioService.autenticar(username, password)) {
            loginError.setText("");
            loginSucces.setText("Login realizado com sucesso!");

            try {
                Usuario usuarioLogado = usuarioService.buscarPorUsername(username);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/estoque/telaInicial.fxml"));
                loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
                Scene scene = new Scene(loader.load());

                TelaInicialController controller = loader.getController();
                controller.setUsuarioLogado(usuarioLogado); // <-- Passa o usuário logado

                Stage stage = AppContextProvider.getStage();
                stage.setScene(scene);
                stage.setTitle("SOLLID COMERCIO LTDA");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                loginError.setText("Erro ao carregar tela de produtos.");
            }

        } else {
            loginSucces.setText("");
            loginError.setText("Usuário ou senha inválidos.");
        }
    }

    @FXML
    public void onChangePasswordClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String senhaAntiga = oldPasswordField.getText();
        String novaSenha = newPasswordField.getText();
        String confirmar = confirmPasswordField.getText();

        if (!novaSenha.equals(confirmar)) {
            msgLabel.setText("Nova senha e confirmação não conferem.");
            return;
        }

        boolean alterado = usuarioService.alterarSenha(username, senhaAntiga, novaSenha);
        if (alterado) {
            msgLabel.setText("Senha alterada com sucesso.");
        } else {
            msgLabel.setText("Usuário ou senha atual incorretos.");
        }
    }
}
