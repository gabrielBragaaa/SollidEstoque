package Estoque.controller;

import Estoque.entities.Produto;
import Estoque.entities.Fornecedor;
import Estoque.entities.Categoria;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.repositories.ProdutoRepository;
import Estoque.repositories.FornecedorRepository;
import Estoque.repositories.CategoriaRepository;
import Estoque.services.CategoriaService;
import Estoque.services.FornecedorService;
import Estoque.util.TelaLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class EntradaProController implements Initializable, UsuarioAware {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @FXML
    private TextField txtBuscar;

    private Long id_produto;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtPreco;

    @FXML
    private ComboBox<Fornecedor> fornecedorCombo;

    @FXML
    private ComboBox<Categoria> categoriaCombo;

    @FXML
    private Button btnBuscar;

    private Produto produtoAtual;

    private Usuario usuarioLogado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtBuscar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnBuscar.fire();
            }
        } );

        // Carrega os fornecedores no ComboBox e exibe seus nomes
        List<Fornecedor> fornecedores = fornecedorService.findAll();
        fornecedorCombo.getItems().addAll(fornecedores);
        fornecedorCombo.setCellFactory(param -> new javafx.scene.control.ListCell<Fornecedor>() {
            @Override
            protected void updateItem(Fornecedor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNomeFornecedor());
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
                setText(empty || item == null ? "" : item.getNomeFornecedor());
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

    @Override
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    private void limparProdutosEntrada(){
        txtNome.clear();
        txtCodigo.clear();
        txtQuantidade.clear();
        txtPreco.clear();
        fornecedorCombo.getSelectionModel().clearSelection();
        categoriaCombo.getSelectionModel().clearSelection();
    }

    public void carregarProdutoParaEdicao(Produto produto) {
        this.produtoAtual = produto;
        txtNome.setText(produto.getNome());
        txtCodigo.setText(produto.getCodigo());
        txtQuantidade.setText(String.valueOf(produto.getQuantidade_inicial()));
        txtPreco.setText(String.valueOf(produto.getPreco_unitario()));
        fornecedorCombo.setValue(produto.getFornecedor());
        categoriaCombo.setValue(produto.getCategoria());
    }

    @FXML
    public void atualizarProduto(ActionEvent event) {
        if (produtoAtual == null) {
            produtoAtual = new Produto();
        }

        produtoAtual.setNome(txtNome.getText());
        produtoAtual.setCodigo(txtCodigo.getText());
        produtoAtual.setQuantidade_inicial(Integer.parseInt(txtQuantidade.getText()));
        produtoAtual.setPreco_unitario(Double.parseDouble(txtPreco.getText()));
        produtoAtual.setFornecedor(fornecedorCombo.getValue());
        produtoAtual.setCategoria(categoriaCombo.getValue());

        boolean isAtualizacao = produtoAtual.getId_produto() != 0;

        produtoRepository.save(produtoAtual);

        if (isAtualizacao) {
            System.out.println("Produto atualizado com sucesso!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produto atualizado com sucesso!");
            alert.showAndWait();
        }
        limparProdutosEntrada();
    }

    @FXML
    public void buscarProduto() {
        String codigoBusca = txtBuscar.getText();

        if (codigoBusca == null || codigoBusca.isBlank()) {
            System.out.println("Campo de busca vazio.");
            return;
        }

        Optional<Produto> optionalProduto = produtoRepository.findByCodigo(codigoBusca);

        if (optionalProduto.isPresent()) {
            Produto produto = optionalProduto.get();
            carregarProdutoParaEdicao(produto); // Método que popula os campos
        } else {
            System.out.println("Produto não encontrado com o código informado.");
        }
    }



    @FXML
    public void EntradaHome(ActionEvent event) {
        try {
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "SOLLID COMERCIO LTDA", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
