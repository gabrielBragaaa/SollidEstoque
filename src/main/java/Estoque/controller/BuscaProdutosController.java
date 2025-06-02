package Estoque.controller;

import Estoque.entities.Produto;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.repositories.ProdutoRepository;
import Estoque.util.TelaLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BuscaProdutosController implements Initializable , UsuarioAware {

    @Autowired
    private ProdutoRepository repository;

    @FXML
    private TableColumn<Produto,String> fornecedor;

    @FXML
    private TableView<Produto> tblProduto;

    @FXML
    private TableColumn<Produto, String> codigo;

    @FXML
    private TableColumn<Produto, String> nomeProduto;

    @FXML
    private TableColumn<Produto, String> categoria;

    @FXML
    private TableColumn<Produto, Long> quantidade_inicial;

    @FXML
    private TableColumn<Produto, Double> preco_unitario;

    @FXML
    private TextField txtCampoBuscar;

    @FXML
    private Button btnBuscarProduto;

    private Usuario usuarioLogado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtCampoBuscar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                btnBuscarProduto.fire();
            }
        });

        // Configura as colunas da tabela
        fornecedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFornecedor().getNomeFornecedor()));
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        categoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria().getNome()));
        quantidade_inicial.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));
        preco_unitario.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));

        carregarProdutos();
    }

    @Override
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    public void buscarProdutos() {
        String textoBusca = txtCampoBuscar.getText();

        if (textoBusca != null && !textoBusca.trim().isEmpty()) {
            try {
                Long id = Long.parseLong(textoBusca);
                Produto produtoEncontrado = repository.findById(id).orElse(null);
                if (produtoEncontrado != null) {
                    tblProduto.getItems().setAll(produtoEncontrado);
                    return;
                }
            } catch (NumberFormatException e) {
                // Não é número, segue a busca
            }

            Produto produtoPorCodigo = repository.findByCodigo(textoBusca).orElse(null);
            if (produtoPorCodigo != null) {
                tblProduto.getItems().setAll(produtoPorCodigo);
                return;
            }

            var produtosPorNome = repository.findByNomeContainingIgnoreCase(textoBusca);
            if (!produtosPorNome.isEmpty()) {
                tblProduto.getItems().setAll(produtosPorNome);
                return;
            }

            // Se não encontrou por nome, tenta por categoria
            var produtosPorCategoria = repository.findByCategoriaNomeContainingIgnoreCase(textoBusca);
            tblProduto.getItems().setAll(produtosPorCategoria);
            tblProduto.getItems().setAll(produtosPorCategoria);

        } else {
            carregarProdutos(); // Se estiver vazio, busca todos
        }
    }



    private void carregarProdutos() {
        tblProduto.getItems().setAll(repository.findAll());
    }

    @FXML
    public void buscarPhome(ActionEvent event){
        System.out.println("Usuário atual: " + usuarioLogado); // debug
        TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "Tela Inicial", usuarioLogado);
    }
}
