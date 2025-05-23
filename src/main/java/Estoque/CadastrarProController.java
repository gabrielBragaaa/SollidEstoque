package Estoque;

import Estoque.Entities.Categoria;
import Estoque.Entities.Fornecedor;
import Estoque.Entities.Produto;
import Estoque.config.AppContextProvider;
import Estoque.services.CategoriaService;
import Estoque.services.FornecedorService;
import Estoque.services.ProdutoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CadastrarProController {

    @FXML private TextField nomeField;
    @FXML private TextField codigoField;
    @FXML private TextField quantidadeField;
    @FXML private TextField precoField;
    @FXML private ComboBox<Fornecedor> fornecedorCombo;
    @FXML private ComboBox<Categoria> categoriaCombo;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;

    @FXML
    public void initialize() {
        // Carrega os fornecedores no ComboBox e exibe seus nomes
        List<Fornecedor> fornecedores = fornecedorService.findAll();
        fornecedorCombo.getItems().addAll(fornecedores);
        fornecedorCombo.setCellFactory(param -> new javafx.scene.control.ListCell<Fornecedor>() {
            @Override
            protected void updateItem(Fornecedor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });

        // Carrega as categorias no ComboBox e exibe seus nomes
        List<Categoria> categorias = categoriaService.findAll();
        categoriaCombo.getItems().addAll(categorias);
        categoriaCombo.setCellFactory(param -> new javafx.scene.control.ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());

            }
        });


        fornecedorCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Fornecedor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
                setStyle("-fx-font-weight: bold;");
            }
        });

        categoriaCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
                setStyle("-fx-font-weight: bold;");
            }
        });
    }

    @FXML
    public void salvarProduto() {
        try {
            String codigo = codigoField.getText();
            Produto existente = null;

            try {
                existente = produtoService.findByCodigo(codigo);
            } catch (Exception ignored) {
                // Produto não existe, será criado
            }

            int quantidade = Integer.parseInt(quantidadeField.getText());

            if (existente != null) {
                // Atualiza apenas a quantidade
                existente.setQuantidade_inicial(existente.getQuantidade_inicial() + quantidade);
                produtoService.insert(existente);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produto já existente. Quantidade atualizada com sucesso!");
                alert.showAndWait();

            } else {
                // Cadastra novo produto
                Produto novo = new Produto();
                novo.setNome(nomeField.getText());
                novo.setCodigo(codigo);
                novo.setQuantidade_inicial(quantidade);
                novo.setPreco_unitario(Double.parseDouble(precoField.getText()));
                novo.setFornecedor(fornecedorCombo.getValue());
                novo.setCategoria(categoriaCombo.getValue());

                produtoService.insert(novo);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Novo produto cadastrado com sucesso!");
                alert.showAndWait();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar produto: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    public void cadatroPhome(ActionEvent event) {
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
