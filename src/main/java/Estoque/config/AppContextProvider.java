package Estoque.config;

import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppContextProvider {

    private static ApplicationContext context;
    private static Stage stage;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    public static Stage getStage() {
        return stage;
    }
}

