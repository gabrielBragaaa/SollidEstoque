package Estoque.controller;
import Estoque.services.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AlterarSenhaController {

    @FXML
    private TextField txtUsername;

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
    public void onChangePasswordClicked(ActionEvent event) {
        String username = txtUsername.getText().trim();
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
            msgLabel.setStyle("-fx-text-fill: green;");
        } else {
            msgLabel.setText("Usuário ou senha atual incorretos.");
            msgLabel.setStyle("-fx-text-fill: red;");
        }
    }

}