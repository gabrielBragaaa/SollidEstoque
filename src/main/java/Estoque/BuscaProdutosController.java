package Estoque;

import Estoque.Entities.Produto;
import Estoque.config.AppContextProvider;
import Estoque.repositories.ProdutoRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BuscaProdutosController implements Initializable {

    @Autowired
    private ProdutoRepository repository;

    @FXML
    private TableColumn<Produto,String> fornecedor;

    @FXML
    private TableView<Produto> produto;

    @FXML
    private TableColumn<Produto, String> codigo;

    @FXML
    private TableColumn<Produto, String> nome;

    @FXML
    private TableColumn<Produto, String> categoria;

    @FXML
    private TableColumn<Produto, Long> quantidade_inicial;

    @FXML
    private TableColumn<Produto, Double> preco_unitario;

    @FXML
    private TextField campoBusca;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura as colunas da tabela
        fornecedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFornecedor().getNome()));
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        categoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria().getNome()));
        quantidade_inicial.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));
        preco_unitario.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));

        carregarProdutos();
    }

    @FXML
    public void buscarProdutos() {
        String textoBusca = campoBusca.getText();

        if (textoBusca != null && !textoBusca.trim().isEmpty()) {
            try {
                Long id = Long.parseLong(textoBusca);
                Produto produtoEncontrado = repository.findById(id).orElse(null);
                if (produtoEncontrado != null) {
                    produto.getItems().setAll(produtoEncontrado);
                    return;
                }
            } catch (NumberFormatException e) {
                // Não é número, segue a busca
            }

            Produto produtoPorCodigo = repository.findByCodigo(textoBusca).orElse(null);
            if (produtoPorCodigo != null) {
                produto.getItems().setAll(produtoPorCodigo);
                return;
            }

            var produtosPorNome = repository.findByNomeContainingIgnoreCase(textoBusca);
            if (!produtosPorNome.isEmpty()) {
                produto.getItems().setAll(produtosPorNome);
                return;
            }

            // Se não encontrou por nome, tenta por categoria
            var produtosPorCategoria = repository.findByCategoriaNomeContainingIgnoreCase(textoBusca);
            produto.getItems().setAll(produtosPorCategoria);produto.getItems().setAll(produtosPorCategoria);

        } else {
            carregarProdutos(); // Se estiver vazio, busca todos
        }
    }



    private void carregarProdutos() {
        produto.getItems().setAll(repository.findAll());
    }

    @FXML
    public void buscarPhome(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/estoque/telaInicial.fxml"));
            loader.setControllerFactory(AppContextProvider.getApplicationContext()::getBean);
            Scene scene = new Scene(loader.load());

            Stage stage = AppContextProvider.getStage();
            stage.setScene(scene);
            stage.setTitle("Voltar");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
