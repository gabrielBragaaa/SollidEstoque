package Estoque.controller;

import Estoque.entities.Produto;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.repositories.ProdutoRepository;
import Estoque.services.ProdutoService;
import Estoque.util.NotaFiscalUtil;
import Estoque.util.TelaLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class SaidaProController implements Initializable, UsuarioAware {

    @Autowired
    private ProdutoRepository repository;

    @FXML
    private TableColumn<Produto, String> fornecedor;

    @FXML
    private TableView<Produto> tblProdutos;

    @FXML
    private TableView<Produto> tblExcluirProdutos;

    @FXML
    private TableView<Produto> tblSelecionados;

    @FXML
    private TableColumn<Produto, Long> id_produto;

    @FXML
    private TableColumn<Produto, Long> codigo;

    @FXML
    private TableColumn<Produto, String> nome;

    @FXML
    private TableColumn<Produto, Long> quantidade_inicial;

    @FXML
    private TableColumn<Produto, Double> preco_unitario;

    @FXML
    private TableColumn<Produto, String> nomeSelecionado;

    @FXML
    private TableColumn<Produto, Integer> quantidadeSelecionada;

    @FXML
    private TableColumn<Produto, Double> precoSelecionado;

    @FXML
    private TableColumn<Produto, String> fornecedorSelecionado;

    @FXML
    private TableColumn<Produto, String> nomeProdutoExcluir;

    @FXML
    private TableColumn<Produto, String> fornecedorProdutoExcluir;

    @FXML
    private TableColumn<Produto, Long> codigoProdutoExcluir;

    @FXML
    private TableColumn<Produto, Integer> quantidadeInicialExcluir;

    @FXML
    private TableColumn<Produto, Double> precoUnitarioExcluir;


    @FXML
    private TextField txtCampoBuscaVenda;

    @FXML
    private TextField txtCampoQuantidadeSaida;

    @FXML
    private TextField txtCampoBuscaExcluir;
    @FXML
    private Label labelProduto;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label codigoLabel;
    @FXML
    private Label quantidadeLabel;
    @FXML
    private Label precoLabel;
    @FXML
    private Label fornecedorlabel;

    @FXML
    private Label labelTotal;

    @FXML
    private ComboBox<String> tipoSaidaCombo;

    private ObservableList<Produto> produtosSelecionados = FXCollections.observableArrayList();

    @Autowired
    private ProdutoService produtoService;

    private Produto produtoSelecionado;

    @FXML
    private Button btnBuscarVenda;

    //Botao de excluir permanentemente do banco
    @FXML
    private Button btnExcluirProduto;

    //Botao da segunda tabela
    @FXML
    private Button btnBuscarExcluir;

    private Produto produtoExcluir;

    private Usuario usuarioLogado;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

       txtCampoBuscaVenda.setOnKeyPressed(event -> {
           if (event.getCode() == KeyCode.ENTER){
               btnBuscarVenda.fire();
           }
       });

        txtCampoBuscaExcluir.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                btnBuscarExcluir.fire();
            }
        });

        // Configura as colunas da tabela

        fornecedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFornecedor().getNomeFornecedor()));
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        quantidade_inicial.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));
        preco_unitario.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));
        fornecedorSelecionado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFornecedor() != null
                        ? cellData.getValue().getFornecedor().getNomeFornecedor() : "Não informado"));
        nomeSelecionado.setCellValueFactory(new PropertyValueFactory<>("nome"));
        quantidadeSelecionada.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));
        precoSelecionado.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));

        tblSelecionados.setItems(produtosSelecionados);

        // Evento de duplo clique para adicionar produto
        tblProdutos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Duplo clique
                Produto selecionado = tblProdutos.getSelectionModel().getSelectedItem();
                if (selecionado != null) {
                    adicionarProdutoSelecionado(selecionado);
                }
            }
            tblSelecionados.setEditable(true);
            quantidadeSelecionada.setCellFactory(TextFieldTableCell.<Produto, Integer>forTableColumn(new IntegerStringConverter()));
        });
        quantidadeSelecionada.setOnEditCommit(editEvent -> {
            Produto produtoEditado = editEvent.getRowValue();
            int novaQuantidade = editEvent.getNewValue();

            // Busca o produto original no banco de dados para verificar o estoque real
            Produto produtoOriginal = repository.findById(Long.valueOf(produtoEditado.getId_produto())).orElse(null);

            if (produtoOriginal != null) {
                if (novaQuantidade > produtoOriginal.getQuantidade_inicial()) {
                    mostrarAlerta("Quantidade excede o estoque disponível (" + produtoOriginal.getQuantidade_inicial() + ").", Alert.AlertType.WARNING);
                    // Força a tabela a voltar ao valor antigo
                    tblSelecionados.refresh();
                } else {
                    produtoEditado.setQuantidade_inicial(novaQuantidade);
                    atualizarTotal(); // Atualiza o total após alteração válida
                }
            }
        });

        produtosSelecionados.clear(); // Limpa a lista ao abrir a tela
        tblSelecionados.refresh(); // Atualiza a tabela visualmente

        //Seguda Tabela De Excluir

        if (btnExcluirProduto != null) { // NullPointerException
            btnExcluirProduto.setVisible(false);
        }
        nomeProdutoExcluir.setCellValueFactory(new PropertyValueFactory<>("nome"));

        fornecedorProdutoExcluir.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFornecedor() != null
                        ? cellData.getValue().getFornecedor().getNomeFornecedor()
                        : "Não informado"));

        codigoProdutoExcluir.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        quantidadeInicialExcluir.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));
        precoUnitarioExcluir.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));


//        btnExcluirProduto.setOnAction(event -> removerProdutoSelecionado());

        //Botao de slecionar produto para excluir permanentemente do banco
        tblExcluirProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                produtoExcluir = newSelection;
                btnExcluirProduto.setVisible(true);
            }
        });

    }

    //Remover visibilidade de botao para usurio sem permissao
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        System.out.println("Usuario logado na SaidaProController: " + usuario.getUsername() + ", Role: " + usuario.getRole());

        if (!"ADMIN".equalsIgnoreCase(usuario.getRole())) {
            btnExcluirProduto.setVisible(false);
        } else {
            btnExcluirProduto.setVisible(true);
        }
    }

    private void carregarProdutos() {
        tblProdutos.getItems().setAll(repository.findAll());
    }


    @FXML
    public void buscarProdutos() {
        String textoBusca = txtCampoBuscaVenda.getText();

        if (textoBusca != null && !textoBusca.trim().isEmpty()) {
            try {
                // Tenta buscar por ID
                Long id = Long.parseLong(textoBusca);
                Produto produtoEncontrado = repository.findById(id).orElse(null);
                if (produtoEncontrado != null) {
                    tblProdutos.getItems().setAll(produtoEncontrado);
                    return;
                }
            } catch (NumberFormatException e) {
                // Ignora se não for número
            }

            // Se não encontrou pelo ID, tenta buscar pelo Código
            Produto produtoPorCodigo = repository.findByCodigo(textoBusca).orElse(null);
            if (produtoPorCodigo != null) {
                tblProdutos.getItems().setAll(produtoPorCodigo);
                return;
            }

            // Se não encontrou pelo Código, tenta buscar pelo Nome
            var produtosPorNome = repository.findByNomeContainingIgnoreCase(textoBusca);
            tblProdutos.getItems().setAll(produtosPorNome);

        } else {
            carregarProdutos(); // Busca padrão se não digitou nada
        }
    }

    @FXML
    public void realizarSaida() {
        if (produtoSelecionado == null) {
            mostrarAlerta("Selecione um produto antes de realizar a saída!", Alert.AlertType.WARNING);
            return;
        }

        String tipoSaida = tipoSaidaCombo.getValue();

        if (tipoSaida == null) {
            mostrarAlerta("Escolha o tipo de saída antes de continuar.", Alert.AlertType.WARNING);
            return;
        }

        if (tipoSaida.equals("Saída por Venda")) {
            try {
                int quantidade = Integer.parseInt(txtCampoQuantidadeSaida.getText());

                if (quantidade <= 0) {
                    mostrarAlerta("A quantidade precisa ser maior que zero.", Alert.AlertType.WARNING);
                    return;
                }

                if (produtoSelecionado.getQuantidade_inicial() < quantidade) {
                    mostrarAlerta("Estoque insuficiente para essa quantidade.", Alert.AlertType.WARNING);
                    return;
                }

                produtoSelecionado.setQuantidade_inicial(produtoSelecionado.getQuantidade_inicial() - quantidade);
                produtoService.insert(produtoSelecionado);

                mostrarAlerta("Saída registrada com sucesso!", Alert.AlertType.INFORMATION);
                exibirDetalhesProduto(produtoSelecionado);

            } catch (NumberFormatException e) {
                mostrarAlerta("Informe uma quantidade válida.", Alert.AlertType.ERROR);
            }

        } else if (tipoSaida.equals("Remover Produto")) {
            removerProduto();  // Reaproveita seu método de remoção!
        } else {
            mostrarAlerta("Tipo de saída inválido.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void removerProduto() {
        if (produtoSelecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Remoção");
            alert.setHeaderText("Deseja remover o produto do estoque?");
            alert.setContentText("Produto: " + produtoSelecionado.getNome());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        produtoService.delete((long) produtoSelecionado.getId_produto());
                        mostrarAlerta("Produto removido com sucesso!", Alert.AlertType.INFORMATION);
                        limparTela();
                    } catch (Exception e) {
                        mostrarAlerta("Erro ao remover produto: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            mostrarAlerta("Nenhum produto selecionado.", Alert.AlertType.WARNING);
        }
    }

    private void exibirDetalhesProduto(Produto produto) {
        fornecedorlabel.setText("Fonecedor: " + produto.getNome());
        labelProduto.setText("Produto encontrado:");
        nomeLabel.setText("Nome: " + produto.getNome());
        codigoLabel.setText("Código: " + produto.getCodigo());
        quantidadeLabel.setText("Quantidade: " + produto.getQuantidade_inicial());
        precoLabel.setText("Preço Unitário: " + produto.getPreco_unitario());
    }

    private void limparTela() {
        txtCampoBuscaVenda.clear();
        txtCampoQuantidadeSaida.clear();
        tipoSaidaCombo.getSelectionModel().clearSelection();
        labelProduto.setText("Produto não encontrado");
        nomeLabel.setText("Nome: ");
        codigoLabel.setText("Código: ");
        quantidadeLabel.setText("Quantidade: ");
        precoLabel.setText("Preço Unitário: ");
        produtoSelecionado = null;
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void adicionarProdutoSelecionado(Produto produto) {
        // Verifica se já foi adicionado
        boolean jaAdicionado = produtosSelecionados.stream()
                .anyMatch(p -> Objects.equals(p.getId_produto(), produto.getId_produto()));

        if (!jaAdicionado) {
            Produto copiaProduto = new Produto();
            copiaProduto.setId_produto(produto.getId_produto());
            copiaProduto.setNome(produto.getNome());
            copiaProduto.setPreco_unitario(produto.getPreco_unitario());
            copiaProduto.setQuantidade_inicial(1); // Começa com 1 de quantidade
            copiaProduto.setFornecedor(produto.getFornecedor()); // <-- Adiciona o fornecedor

            produtosSelecionados.add(copiaProduto);
            atualizarTotal();  // Atualiza o total sempre que um produto for adicionado
        } else {
            mostrarAlerta("Produto já adicionado!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void removerProdutoSelecionado() {
        Produto produtoSelecionadoParaRemover = tblSelecionados.getSelectionModel().getSelectedItem();

        if (produtoSelecionadoParaRemover != null) {
            // Remove o produto da lista de produtos selecionados
            produtosSelecionados.remove(produtoSelecionadoParaRemover);

            // Exibe um alerta para confirmar a remoção
            mostrarAlerta("Produto removido da seleção.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selecione um produto para remover.", Alert.AlertType.WARNING);
        }

        // Atualiza o total sempre que um produto for removido
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = 0.0;
        for (Produto produto : produtosSelecionados) {
            total += produto.getPreco_unitario() * produto.getQuantidade_inicial();
        }
        labelTotal.setText("Total: R$ " + String.format("%.2f", total));
    }

    //Pre visualização de notas
    private String gerarPreviewNota(int numeroNota) {
        StringBuilder sb = new StringBuilder();
        sb.append("● SOLLID COMERCIO LTDA ●\n");
        sb.append("Praça da Bandeira\nCNPJ: 11.489.912/0001-95\n\n");
        sb.append("Nota Fiscal Nº: ").append(numeroNota).append("\n");
        sb.append("CUPOM FISCAL NÃO ELETRÔNICO\n");
        sb.append("--------------------------------------------------\n");

        double total = 0.0;
        int count = 1;

        for (Produto p : produtosSelecionados) {
            double subtotal = p.getPreco_unitario() * p.getQuantidade_inicial();
            total += subtotal;

            sb.append(String.format("Item %02d\n", count++));
            sb.append("Produto: ").append(p.getNome()).append("\n");
            sb.append(p.getFornecedor().getNomeFornecedor()).append("\n");
            sb.append("Quantidade: ").append(p.getQuantidade_inicial()).append("\n");
            sb.append(String.format("Preço Unitário: R$ %.2f\n", p.getPreco_unitario()));
            sb.append(String.format("Subtotal: R$ %.2f\n", subtotal));
            sb.append("--------------------------------------------------\n");
        }

        sb.append(String.format("TOTAL DO PEDIDO: R$ %.2f\n", total));
        sb.append("Data: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        sb.append("\n* Sollid Comercio LTDA *\n");

        return sb.toString();
    }

    private boolean mostrarPreview(String conteudo) {
        TextArea area = new TextArea(conteudo);
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefSize(600, 400);

        ButtonType salvar = new ButtonType("Salvar PDF", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pré-visualização da Nota");
        alert.setHeaderText("Confira a Nota Fiscal antes de salvar:");
        alert.getDialogPane().setContent(area);
        alert.getButtonTypes().setAll(salvar, cancelar);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == salvar;
    }

    @FXML
    public void finalizarSaida() {
        if (produtosSelecionados.isEmpty()) {
            mostrarAlerta("Nenhum produto selecionado para saída.", Alert.AlertType.WARNING);
            return;
        }

        for (Produto produtoSelecionado : produtosSelecionados) {
            Produto produtoBanco = repository.findById(produtoSelecionado.getId_produto()).orElse(null);
            if (produtoBanco != null) {
                int quantidadeAtual = produtoBanco.getQuantidade_inicial();
                int quantidadeSaida = produtoSelecionado.getQuantidade_inicial();

                if (quantidadeSaida <= quantidadeAtual) {
                    produtoBanco.setQuantidade_inicial(quantidadeAtual - quantidadeSaida);
                    repository.save(produtoBanco);
                } else {
                    mostrarAlerta("Estoque insuficiente para o produto: " + produtoBanco.getNome(), Alert.AlertType.ERROR);
                    return;
                }
            } else {
                mostrarAlerta("Produto não encontrado no banco de dados: " + produtoSelecionado.getNome(), Alert.AlertType.ERROR);
                return;
            }
        }

        produtosSelecionados.clear();
        tblSelecionados.setItems(FXCollections.observableArrayList(produtosSelecionados));
        mostrarAlerta("Saída de produtos finalizada com sucesso!", Alert.AlertType.INFORMATION);


        int numeroNota = NotaFiscalUtil.getProximaNota();
        String preview = gerarPreviewNota(numeroNota);

        boolean confirmar = mostrarPreview(preview);
        if (!confirmar) {
            return; // Usuário cancelou
        }

        try {
            String nomeArquivo = "NotaFiscal_" + String.format("%03d", numeroNota) + ".pdf";
            String diretorio = "C:/NotasFiscais/";
            File pasta = new File(diretorio);
            if (!pasta.exists()) pasta.mkdirs();

            File file = new File(diretorio + nomeArquivo);

            // A geração do PDF (igual à sua lógica anterior)
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font regularFont = new Font(Font.HELVETICA, 12);
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            Paragraph header = new Paragraph("● SOLLID COMERCIO LTDA ●", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph endereco = new Paragraph("Praça da Bandeira\nCNPJ: 11.489.912/0001-95\n\n", regularFont);
            endereco.setAlignment(Element.ALIGN_CENTER);
            document.add(endereco);

            Paragraph numeroNotaParagrafo = new Paragraph("Nota Fiscal Nº: " + numeroNota + "\n\n", boldFont);
            numeroNotaParagrafo.setAlignment(Element.ALIGN_CENTER);
            document.add(numeroNotaParagrafo);

            document.add(new Paragraph("CUPOM FISCAL NÃO ELETRÔNICO", boldFont));
            document.add(new Paragraph("--------------------------------------------------"));

            double total = 0.0;
            int count = 1;
            for (Produto p : produtosSelecionados) {
                double subtotal = p.getPreco_unitario() * p.getQuantidade_inicial();
                total += subtotal;

                document.add(new Paragraph(String.format("Item %02d", count++), boldFont));
                document.add(new Paragraph("Produto: " + p.getNome(), regularFont));
                document.add(new Paragraph("Fornecedor: " + p.getFornecedor().getNomeFornecedor(), regularFont));
                document.add(new Paragraph("Quantidade: " + p.getQuantidade_inicial(), regularFont));
                document.add(new Paragraph(String.format("Preço Unitário: R$ %.2f", p.getPreco_unitario()), regularFont));
                document.add(new Paragraph(String.format("Subtotal: R$ %.2f", subtotal), regularFont));
                document.add(new Paragraph("--------------------------------------------------"));
            }

            document.add(new Paragraph(String.format("TOTAL DO PEDIDO: R$ %.2f\n", total), boldFont));
            document.add(new Paragraph("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), regularFont));
            document.add(new Paragraph("\n* Sollid Comercio LTDA *", regularFont));
            document.close();

            NotaFiscalUtil.IncrementarNota();
            mostrarAlerta("PDF gerado com sucesso como " + nomeArquivo, Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao gerar PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    //Tela para remover produto permanente do banco


    @FXML
    public void buscarProdutoParaExcluir() {
        String textoBusca = txtCampoBuscaExcluir.getText().trim();

        if (textoBusca == null || textoBusca.isEmpty()) {
            mostrarAlerta("Digite o ID, Código, Nome ou Categoria do produto!", Alert.AlertType.WARNING);
            return;
        }

        produtoExcluir = null;
        btnExcluirProduto.setDisable(true); // Desativa o botão por padrão

        List<Produto> produtosEncontrados = new ArrayList<>();

        // Busca por código
        Optional<Produto> porCodigo = repository.findByCodigo(textoBusca);
        porCodigo.ifPresent(produtosEncontrados::add);

        // Busca por ID, se for numérico
        if (produtosEncontrados.isEmpty()) {
            try {
                Long id = Long.parseLong(textoBusca);
                Optional<Produto> porId = repository.findById(id);
                porId.ifPresent(produtosEncontrados::add);
            } catch (NumberFormatException ignored) {
                // Não é um número
            }
        }

        // Busca por nome parcial
        if (produtosEncontrados.isEmpty()) {
            produtosEncontrados = repository.findByNomeContainingIgnoreCase(textoBusca);
        }

        // Busca por categoria
        if (produtosEncontrados.isEmpty()) {
            produtosEncontrados = repository.findByCategoriaNomeContainingIgnoreCase(textoBusca);
        }

        // Resultado final
        if (!produtosEncontrados.isEmpty()) {
            ObservableList<Produto> dados = FXCollections.observableArrayList(produtosEncontrados);
            tblExcluirProdutos.setItems(dados);

            if (produtosEncontrados.size() == 1) {
                produtoExcluir = produtosEncontrados.get(0);
                tblExcluirProdutos.getSelectionModel().select(0); // Seleciona o item na tabela
                btnExcluirProduto.setDisable(false); // Habilita botão
            } else {
                mostrarAlerta("Foram encontrados múltiplos produtos. Selecione um na tabela para excluir.", Alert.AlertType.INFORMATION);
            }
        } else {
            mostrarAlerta("Nenhum produto encontrado com esse termo.", Alert.AlertType.WARNING);
            tblExcluirProdutos.setItems(FXCollections.observableArrayList()); // limpa tabela
        }
    }


    @FXML
    public void selecionarProdutoDaTabela() {
        Produto selecionado = tblExcluirProdutos.getSelectionModel().getSelectedItem();
        System.out.println("Produto selecionado na tabela: " + selecionado);
        if (selecionado != null) {
            produtoExcluir = selecionado;
            btnExcluirProduto.setVisible(true);
        }
    }

    @FXML
    public void ExcluirProduto() {
        if (!"ADMIN".equalsIgnoreCase(usuarioLogado.getRole())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Acesso Negado");
            alert.setHeaderText("Permissão Insuficiente");
            alert.setContentText("Você não tem permissão para remover produtos.");
            alert.showAndWait();
            return;
        }

        if (produtoExcluir != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Remoção");
            alert.setHeaderText("Remover Produto");
            alert.setContentText("Deseja remover permanentemente o produto: " + produtoExcluir.getNome() + "?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        produtoService.delete((long) produtoExcluir.getId_produto());
                        mostrarAlerta("Produto removido com sucesso!", Alert.AlertType.INFORMATION);
                        limparCamposRemover();
                    } catch (Exception e) {
                        mostrarAlerta("Erro ao remover produto: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            mostrarAlerta("Nenhum produto selecionado para remoção.", Alert.AlertType.WARNING);
        }
    }

    private void limparCamposRemover() {
        txtCampoBuscaExcluir.clear();
        nomeProdutoExcluir.setText("Produto:");
        codigoProdutoExcluir.setText("Código:");
        fornecedorProdutoExcluir.setText("Fornecedor:");
        quantidadeInicialExcluir.setText(("Quantidade:"));
        precoUnitarioExcluir.setText("Preço Unitario:");
        produtoExcluir = null;
    }

    @FXML
    public void voltar(ActionEvent event) {
        try {
            System.out.println("Usuário atual: " + usuarioLogado); // debug
            TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "Tela Inicial", usuarioLogado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
