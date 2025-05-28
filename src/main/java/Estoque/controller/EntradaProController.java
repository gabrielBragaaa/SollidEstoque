package Estoque.controller;

import Estoque.entities.Produto;
import Estoque.entities.Fornecedor;
import Estoque.entities.Categoria;
import Estoque.config.AppContextProvider;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class EntradaProController implements Initializable, UsuarioAware {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @FXML
    private TextField buscaField;

    private Long id_produto;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField codigoField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TextField precoField;

    @FXML
    private ComboBox<Fornecedor> fornecedorCombo;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;

    @FXML
    private ComboBox<Categoria> categoriaCombo;

    private Produto produtoAtual;

    private Usuario usuarioLogado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    @Override
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void carregarProdutoParaEdicao(Produto produto) {
        this.produtoAtual = produto;
        nomeField.setText(produto.getNome());
        codigoField.setText(produto.getCodigo());
        quantidadeField.setText(String.valueOf(produto.getQuantidade_inicial()));
        precoField.setText(String.valueOf(produto.getPreco_unitario()));
        fornecedorCombo.setValue(produto.getFornecedor());
        categoriaCombo.setValue(produto.getCategoria());
    }

    @FXML
    public void atualizarProduto(ActionEvent event) {
        if (produtoAtual == null) {
            produtoAtual = new Produto();
        }

        produtoAtual.setNome(nomeField.getText());
        produtoAtual.setCodigo(codigoField.getText());
        produtoAtual.setQuantidade_inicial(Integer.parseInt(quantidadeField.getText()));
        produtoAtual.setPreco_unitario(Double.parseDouble(precoField.getText()));
        produtoAtual.setFornecedor(fornecedorCombo.getValue());
        produtoAtual.setCategoria(categoriaCombo.getValue());

        boolean isAtualizacao = produtoAtual.getId_produto() != 0;

        produtoRepository.save(produtoAtual);

        if (isAtualizacao) {
            System.out.println("Produto atualizado com sucesso!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produto atualizado com sucesso!");
            alert.showAndWait();
        }
    }

    @FXML
    public void buscarProduto() {
        String codigoBusca = buscaField.getText();

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
            TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "Tela Inicial", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
