package Estoque.util;

import Estoque.config.AppContextProvider;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaLoader {

    public static void carregarTela(String caminhoFxml, String titulo, Usuario usuarioLogado){
        try {
            FXMLLoader loader = new FXMLLoader(TelaLoader.class.getResource(caminhoFxml));
            loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
            Scene scene = new Scene(loader.load());

            // Passa o usuário, se o controller permitir
            Object controller = loader.getController();
            if (controller instanceof UsuarioAware) {
                ((UsuarioAware) controller).setUsuarioLogado(usuarioLogado);
            }

            Stage stage = AppContextProvider.getStage();
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
