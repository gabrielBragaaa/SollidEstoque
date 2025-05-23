package Estoque.controller;

import Estoque.Entities.Produto;
import Estoque.config.AppContextProvider;
import Estoque.services.Relatorio;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GerarRelatoController {

    @FXML
    private ComboBox<String> tipoRelatorioComboBox;

    @FXML
    private ComboBox<String> formatoExportacaoComboBox;

    @FXML
    private TableView<Produto> tabelaRelatorio;


    @Autowired
    private Relatorio relatorioService;

    @FXML
    public void initialize() {
        tipoRelatorioComboBox.setItems(FXCollections.observableArrayList(
                "Produtos com baixo estoque",
                "Validade dos produtos",
                "Histórico de entrada e saída",
                "Resumo financeiro"
        ));

        formatoExportacaoComboBox.setItems(FXCollections.observableArrayList(
                "Visualizar", "PDF", "Excel", "Word"
        ));
    }

    @FXML
    private void visualizarRelatorio() {
        String tipoRelatorio = tipoRelatorioComboBox.getValue();

        if (tipoRelatorio == null) {
            mostrarAlerta("Selecione um tipo de relatório!");
            return;
        }

        switch (tipoRelatorio) {
            case "Produtos com baixo estoque":
                carregarColunasProduto();
                List<Produto> produtosBaixoEstoque = relatorioService.getProdutosComBaixoEstoque();
                tabelaRelatorio.setItems(FXCollections.observableArrayList(produtosBaixoEstoque));
                break;

            case "Validade dos produtos":
                carregarColunasProdutoComValidade();
                List<Produto> produtosComValidade = relatorioService.getProdutosComValidadeProxima();
                tabelaRelatorio.setItems(FXCollections.observableArrayList(produtosComValidade));
                break;


            default:
                mostrarAlerta("Tipo de relatório ainda não implementado.");
                break;
        }
    }

    @FXML
    private void exportarRelatorio() {
        String tipoRelatorio = tipoRelatorioComboBox.getValue();
        String formato = formatoExportacaoComboBox.getValue();

        if (tipoRelatorio == null || formato == null) {
            mostrarAlerta("Selecione o tipo de relatório e o formato de exportação.");
            return;
        }

        System.out.println("Exportando " + tipoRelatorio + " como " + formato);

        // Aqui você pode implementar as lógicas de exportação específicas.
    }

    private void carregarColunasProduto() {
        tabelaRelatorio.getColumns().clear();

        TableColumn<Produto, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produto, String> codigoCol = new TableColumn<>("Código");
        codigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Produto, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));

        TableColumn<Produto, Double> precoCol = new TableColumn<>("Preço Unitário");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));

        tabelaRelatorio.getColumns().addAll(nomeCol, codigoCol, quantidadeCol, precoCol);
    }

    private void carregarColunasProdutoComValidade() {
        tabelaRelatorio.getColumns().clear();

        TableColumn<Produto, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produto, String> codigoCol = new TableColumn<>("Código");
        codigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Produto, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));

        TableColumn<Produto, Double> precoCol = new TableColumn<>("Preço Unitário");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));

        TableColumn<Produto, String> validadeCol = new TableColumn<>("Validade");
        validadeCol.setCellValueFactory(new PropertyValueFactory<>("validade"));

        tabelaRelatorio.getColumns().addAll(nomeCol, codigoCol, quantidadeCol, precoCol, validadeCol);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/estoque/telaInicial.fxml"));
            loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
            Scene scene = new Scene(loader.load());

            Stage stage = AppContextProvider.getStage();
            stage.setScene(scene);
            stage.setTitle("SOLLID COMERCIO LTDA");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
