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
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@Component
public class EntradaProController implements UsuarioAware {

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

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;


    private Usuario usuarioLogado;

    @FXML
    public void initialize() {

        List<Fornecedor> fornecedores = fornecedorService.findAll();
        fornecedorCombo.getItems().addAll(fornecedores);
        fornecedorCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Fornecedor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNomeFornecedor());
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

        NumberFormat formatoBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        TextFormatter<Number> precoFormatter = new TextFormatter<>(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                if (object == null) return "";
                return formatoBR.format(object);
            }

            @Override
            public Number fromString(String string) {
                try {
                    return formatoBR.parse(string);
                } catch (ParseException e) {
                    return 0;
                }
            }
        }, 0); // valor inicial aqui


        txtPreco.setTextFormatter(precoFormatter);
    }


    @Override
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }


    private void limparCampos() {

        txtNome.clear();
        txtCodigo.clear();
        txtPreco.clear();
        txtQuantidade.clear();
        fornecedorCombo.getSelectionModel().clearSelection();
        categoriaCombo.getSelectionModel().clearSelection();
    }

    @FXML
    public void salvarProduto() {
        try {
            String codigo = txtCodigo.getText();
            Categoria categoriaSelecionada = categoriaCombo.getValue();
            boolean isUsado = categoriaSelecionada != null && categoriaSelecionada.getNome().equalsIgnoreCase("Usados");

            Produto existente = null;
            if (!isUsado) {
                try {
                    existente = produtoService.findByCodigo(codigo);
                } catch (Exception ignored) {
                    // Produto não existe, será criado
                }
            }

            int quantidade = Integer.parseInt(txtQuantidade.getText());

            if (existente != null) {
                existente.setQuantidade_inicial(quantidade);
                produtoService.insert(existente);
                showAlert(AlertType.INFORMATION, "Produto já existente. Quantidade atualizada com sucesso!");

            } else{
                // Cadastra novo produto
                Produto novo = new Produto();
                novo.setNome(txtNome.getText());
                novo.setCodigo(codigo);
                novo.setQuantidade_inicial(quantidade);
                String precoTexto = txtPreco.getText().trim().replace(".", "").replace(",", ".");
                try {
                    double preco = Double.parseDouble(precoTexto);
                    novo.setPreco_unitario(preco);
                } catch (NumberFormatException e) {
                    showAlert(AlertType.ERROR, "Preço inválido! Use o formato 1.234,56");
                    return;
                }
                novo.setFornecedor(fornecedorCombo.getValue());
                novo.setCategoria(categoriaCombo.getValue());
                produtoService.insert(novo);
                showAlert(AlertType.INFORMATION, "Novo produto cadastrado com sucesso!");
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro ao cadastrar produto: " + e.getMessage());
        }

        limparCampos();
    }

    @FXML
    public void buscarProduto() {
        String nome = txtNome.getText().trim();
        String codigo = txtCodigo.getText().trim();

        if (nome.isEmpty() && codigo.isEmpty()) {
            showAlert(AlertType.WARNING, "Informe o nome ou código para buscar o produto.");
            return;
        }

        try {
            List<Produto> encontrados;

            if (!nome.isEmpty() && !codigo.isEmpty()) {
                // Busca com ambos os filtros (nome ou código)
                String termo = nome + " " + codigo;
                encontrados = produtoService.findByNomeOuCodigo(termo);
            } else if (!codigo.isEmpty()) {
                Produto produto = produtoService.findByCodigo(codigo);
                encontrados = produto != null ? List.of(produto) : List.of();
            } else {
                encontrados = produtoService.findByNomeContaining(nome);
            }

            if (encontrados.isEmpty()) {
                showAlert(AlertType.INFORMATION, "Nenhum produto encontrado.");
            } else {
                Produto produto = encontrados.get(0); // Primeiro resultado
                preencherCampos(produto);
            }

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro ao buscar produto: " + e.getMessage());
        }
    }

    private void preencherCampos(Produto produto) {
        txtNome.setText(produto.getNome());
        txtCodigo.setText(produto.getCodigo());
        txtQuantidade.setText(String.valueOf(produto.getQuantidade_inicial()));
        txtPreco.setText(String.valueOf(produto.getPreco_unitario()));
        fornecedorCombo.setValue(produto.getFornecedor());
        categoriaCombo.setValue(produto.getCategoria());
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    @FXML
    public void cadastroPhome(ActionEvent event) {
        try {
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "SOLLID COMERCIO LTDA", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
