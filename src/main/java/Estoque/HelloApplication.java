package Estoque;

import Estoque.config.AppContextProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class HelloApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(AppSpring.class).run();
        AppContextProvider.setApplicationContext(context);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/org/example/estoque/hello-view.fxml"));

        loader.setControllerFactory(context::getBean);

        stage.getIcons().add(
                new javafx.scene.image.Image(getClass().
                        getResource("/org/example/estoque/imagens/Sollid.png").toExternalForm())
        );

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        AppContextProvider.setStage(stage);
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
