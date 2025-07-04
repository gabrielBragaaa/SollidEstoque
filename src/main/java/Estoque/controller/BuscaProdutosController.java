package Estoque.controller;

import Estoque.entities.Categoria;
import Estoque.entities.Fornecedor;
import Estoque.entities.Produto;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.repositories.ProdutoRepository;
import Estoque.services.CategoriaService;
import Estoque.services.FornecedorService;
import Estoque.util.TelaLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BuscaProdutosController implements Initializable , UsuarioAware {

    @Autowired
    private ProdutoRepository repository;

   @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;

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
    private ComboBox<Categoria> categoriaCombo;
    @FXML
    private ComboBox<Fornecedor> fornecedorCombo;

    @FXML
    private TextField txtCampoBuscar;

    @FXML
    private Button btnBuscarProduto;

    private Usuario usuarioLogado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Preenche o ComboBox de categorias
        List<Categoria> categorias = categoriaService.findAll();
        categoriaCombo.getItems().addAll(categorias);

        // Preenche o ComboBox de fornecedores
        List<Fornecedor> fornecedores = fornecedorService.findAll();
        fornecedorCombo.getItems().addAll(fornecedores);

        txtCampoBuscar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
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
        preco_unitario.setCellFactory(column -> new TableCell<>() {
            private final NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formato.format(item));
                }
            }
        });

        carregarProdutos();

    }


    @Override
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    public void buscarProdutos() {
        String textoBusca = txtCampoBuscar.getText();
        Categoria categoriaSelecionada = categoriaCombo.getValue();
        Fornecedor fornecedorSelecionado = fornecedorCombo.getValue();

        // Caso não tenha filtros, retorna todos
        if ((textoBusca == null || textoBusca.trim().isEmpty()) && categoriaSelecionada == null && fornecedorSelecionado == null) {
            carregarProdutos();
            return;
        }

        List<Produto> produtosFiltrados;

        // Filtro principal por texto
        if (textoBusca != null && !textoBusca.trim().isEmpty()) {
            try {
                Long id = Long.parseLong(textoBusca);
                Produto produto = repository.findById(id).orElse(null);
                if (produto != null) {
                    produtosFiltrados = List.of(produto);
                } else {
                    produtosFiltrados = repository.findByNomeContainingIgnoreCase(textoBusca);
                    if (produtosFiltrados.isEmpty()) {
                        produtosFiltrados = repository.findByCategoriaNomeContainingIgnoreCase(textoBusca);
                    }
                }
            } catch (NumberFormatException e) {
                produtosFiltrados = repository.findByNomeContainingIgnoreCase(textoBusca);
                if (produtosFiltrados.isEmpty()) {
                    produtosFiltrados = repository.findByCategoriaNomeContainingIgnoreCase(textoBusca);
                }
            }
        } else { 
            produtosFiltrados = repository.findAll();
        }

        // Aplica filtros adicionais de categoria e fornecedor
        if (categoriaSelecionada != null) {
            produtosFiltrados = produtosFiltrados.stream()
                    .filter(p -> p.getCategoria().equals(categoriaSelecionada))
                    .toList();
        }

        if (fornecedorSelecionado != null) {
            produtosFiltrados = produtosFiltrados.stream()
                    .filter(p -> p.getFornecedor().equals(fornecedorSelecionado))
                    .toList();
        }

        tblProduto.getItems().setAll(produtosFiltrados);
    }

    private void carregarProdutos() {
        tblProduto.getItems().setAll(repository.findAll());
    }

    @FXML
    public void buscarPhome(ActionEvent event){
        System.out.println("Usuário atual: " + usuarioLogado); // debug
        TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "SOLLID COMERCIO LTDA", usuarioLogado);
    }
}

