package Estoque.controller;

import Estoque.config.AppContextProvider;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.util.TelaLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class TelaInicialController implements Initializable, UsuarioAware {

    @FXML
    private ImageView minhaImagem;

    private Usuario usuarioLogado;

    @FXML
    private Button btnHistoricos;

    @FXML
    private Button btnAtualizarPro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Image imagem = new Image(getClass().getResource("/org/example/estoque/imagens/Sollid.png").toExternalForm());
        minhaImagem.setImage(imagem);

        // Deixa o Imageredondo
        double raio = Math.min(minhaImagem.getFitWidth(), minhaImagem.getFitHeight()) / 2;
        Circle clip = new Circle(raio, raio, raio);
        minhaImagem.setClip(clip);

    }
    //Filtro de acesso por tipo de Users
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;

        if (usuario == null) {
            System.out.println("Usuário está nulo no setUsuarioLogado da TelaInicialController.");
            // Opcional: desabilitar ou esconder elementos sensíveis, ou mostrar mensagem
            btnHistoricos.setVisible(false);
            btnAtualizarPro.setVisible(false);
            return;  // sai do método para evitar erros
        }

        System.out.println("Usuário logado na telaInicial: " + usuario.getUsername() + " (ROLE: " + usuario.getRole() + ")");
        if (!"ADMIN".equalsIgnoreCase(usuario.getRole())) {
            btnHistoricos.setVisible(false);
            btnAtualizarPro.setVisible(false);
        } else {
            btnHistoricos.setVisible(true);
            btnAtualizarPro.setVisible(true);
        }
    }

    @FXML
    public void sairDaAplicacao(ActionEvent event) {
        TelaLoader.carregarTela("/org/example/estoque/login.fxml", "Login - Estoque", null);
    }

    @FXML
    public void buscarProd(ActionEvent event) {
        try {
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/consultar-estoque.fxml", "Consultar Estoque", usuarioLogado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tela de cadastro de produto
    @FXML
    public void entradaProd(ActionEvent event) {
        try {
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/entrada-produto.fxml", "Entrada de Produtos", usuarioLogado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tela de Remover produto
    @FXML
    public void saidaProd(ActionEvent event) {
        try {
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/saida-produto.fxml", "Saida de Produtos", usuarioLogado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tela de entrada de produtos
    @FXML
    public void atualizarPro(ActionEvent event) {
        try {
            System.out.println("Usuario Atual: " + usuarioLogado);
            TelaLoader.carregarTela("/org/example/estoque/atualizar-produto.fxml","Atualizar Produtos", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Ação da tela de entrada de produtos
    @FXML
    public void gerarRelato(ActionEvent event) {
        try {
            System.out.println("Usuario Logado: " + usuarioLogado);
            TelaLoader.carregarTela("/org/example/estoque/gerar-relatorios.fxml","Gerar Relatorio", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Historico() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/estoque/historico.fxml"));
            loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
            Scene scene = new Scene(loader.load());

            Stage stage = AppContextProvider.getStage();
            stage.setScene(scene);
            stage.setTitle("Histórico de Ações dos Usuários");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void alterarSenha(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/estoque/alterar-senha.fxml"));
            loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("SOLLID COMERCIO LTDA");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}