package Estoque.controller;

import Estoque.entities.Categoria;
import Estoque.entities.Fornecedor;
import Estoque.entities.Produto;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.services.CategoriaService;
import Estoque.services.FornecedorService;
import Estoque.services.ProdutoService;
import Estoque.util.TelaLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CadastrarProController implements UsuarioAware {

    @FXML private TextField txtNome;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPreco;
    @FXML private ComboBox<Fornecedor> fornecedorCombo;
    @FXML private ComboBox<Categoria> categoriaCombo;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;

    private Usuario usuarioLogado;

    @FXML
    public void initialize() {
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

    @FXML
    public void salvarProduto() {
        try {
            String codigo = txtCodigo.getText();
            Produto existente = null;

            try {
                existente = produtoService.findByCodigo(codigo);
            } catch (Exception ignored) {
                // Produto não existe, será criado
            }

            int quantidade = Integer.parseInt(txtQuantidade.getText());

            if (existente != null) {
                // Atualiza apenas a quantidade
                existente.setQuantidade_inicial(existente.getQuantidade_inicial() + quantidade);
                produtoService.insert(existente);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produto já existente. Quantidade atualizada com sucesso!");
                alert.showAndWait();

            } else {
                // Cadastra novo produto
                Produto novo = new Produto();
                novo.setNome(txtNome.getText());
                novo.setCodigo(codigo);
                novo.setQuantidade_inicial(quantidade);
                novo.setPreco_unitario(Double.parseDouble(txtPreco.getText()));
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
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "Tela Inicial", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
