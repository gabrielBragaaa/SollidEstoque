package Estoque.controller;

import Estoque.entities.HistoricoAcao;
import Estoque.config.AppContextProvider;
import Estoque.repositories.HistoricoAcaoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class HistoricoController implements Initializable {

    @FXML
    private TableView<HistoricoAcao> tblHistorico;

    @FXML
    private TableColumn<HistoricoAcao, String> colunaUsuario;

    @FXML
    private TableColumn<HistoricoAcao, String> colunaAcao;

    @FXML
    private TableColumn<HistoricoAcao, String> colunaEntidade;

    @FXML
    private TableColumn<HistoricoAcao, String> colunaDetalhes;

    @FXML
    private TableColumn<HistoricoAcao, String> colunaDataHora;

    @FXML
    private ComboBox<String> filtroUsuario;

//    @FXML
//    private DatePicker filtroDataInicio;
//
//    @FXML
//    private DatePicker filtroDataFim;


    @Autowired
    private HistoricoAcaoRepository historicoRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colunaUsuario.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsuario().getUsername()));
        colunaAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colunaEntidade.setCellValueFactory(new PropertyValueFactory<>("entidadeAfetada"));
        colunaDetalhes.setCellValueFactory(new PropertyValueFactory<>("detalhes"));
        colunaDataHora.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getDataHora().toString()));

        carregarHistorico();
    }

    private void carregarHistorico() {
        List<HistoricoAcao> historico = historicoRepository.findAll();
        ObservableList<HistoricoAcao> dados = FXCollections.observableArrayList(historico);
        tblHistorico.setItems(dados);
    }

    @FXML
    private void onFiltrarClicked(ActionEvent event) {
        // TODO: lógica de filtragem
        System.out.println("Filtro aplicado");
    }

    @FXML
    private void onLimparFiltrosClicked(ActionEvent event) {
        // TODO: lógica para limpar os filtros
        System.out.println("Filtros limpos");
    }

    @FXML
    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/estoque/telaInicial.fxml"));
            loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
            Scene scene = new Scene(loader.load());

            Stage stage = AppContextProvider.getStage();
            stage.setScene(scene);
            stage.setTitle("Voltar para tela Inicial");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
