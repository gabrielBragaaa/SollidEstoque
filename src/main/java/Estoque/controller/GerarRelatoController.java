package Estoque.controller;

import Estoque.entities.Produto;
import Estoque.entities.Usuario;
import Estoque.projections.UsuarioAware;
import Estoque.services.RelatorioService;
import Estoque.util.TelaLoader;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class GerarRelatoController implements UsuarioAware {

    @FXML
    private ComboBox<String> tipoRelatorioComboBox;

    @FXML
    private ComboBox<String> formatoExportacaoComboBox;

    @FXML
    private TableView<Produto> tblRelatorio;
    @FXML
    private Button btnExportar;

    @Autowired
    private RelatorioService relatorioService;

    private Usuario usuarioLogado;

    @FXML
    public void initialize() {
        tipoRelatorioComboBox.setItems(FXCollections.observableArrayList(
                "Produtos com baixo estoque",
                "Todos os produtos do estoque"
        ));

        formatoExportacaoComboBox.setItems(FXCollections.observableArrayList(
                "Visualizar", "PDF"
        ));
    }

    @Override
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
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
                tblRelatorio.setItems(FXCollections.observableArrayList(produtosBaixoEstoque));
                break;

            case "Todos os produtos do estoque":
                carregarColunasProduto();
                List<Produto> todosProdutos = relatorioService.getTodosProdutos();
                tblRelatorio.setItems(FXCollections.observableArrayList(todosProdutos));
                break;

            default:
                mostrarAlerta("Tipo de relatório ainda não implementado.");
                break;
        }
    }

    @FXML
    private void gerarPdfRelatorio(List<Produto> produtos, String tituloRelatorio) throws Exception {
        Document document = new Document();
        String nomeArquivo = "relatorio_" + System.currentTimeMillis() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));

        document.open();

        // Título
        Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph(tituloRelatorio, fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Tabela com 4 colunas
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f,1f, 2f, 0.5f, 1f});

        // Cabeçalhos da tabela
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Stream.of("Código","Fornecedor","Nome\n Descrição do Produto", "Qtd", "Preço Unitário").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell(new Phrase(headerTitle, headerFont));
            header.setBackgroundColor(Color.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(5);
            table.addCell(header);
        });

        // Conteúdo da tabela
        Font cellFont = new Font(Font.HELVETICA, 12);
        for (Produto produto : produtos) {
            table.addCell(new PdfPCell(new Phrase(produto.getCodigo(), cellFont)));
            table.addCell(new PdfPCell((new Phrase(produto.getFornecedor().getNomeFornecedor(), cellFont))));
            table.addCell(new PdfPCell(new Phrase(produto.getNome(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(produto.getQuantidade_inicial()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("R$ %.2f", produto.getPreco_unitario()), cellFont)));
        }

        document.add(table);
        document.close();

        System.out.println("PDF gerado com sucesso: " + nomeArquivo);
    }

    private void carregarColunasProduto() {
        tblRelatorio.getColumns().clear();

        TableColumn<Produto, String> codigoCol = new TableColumn<>("Código");
        codigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Produto, String> fornecedorCol = new TableColumn<>("Fornecedor");
        fornecedorCol.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

        TableColumn<Produto, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produto, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));

        TableColumn<Produto, Double> precoCol = new TableColumn<>("Preço Unitário");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));

        tblRelatorio.getColumns().addAll(codigoCol,fornecedorCol, nomeCol, quantidadeCol, precoCol);
    }
//Implementar Futuramnet
//    private void carregarColunasProdutoComValidade() {
//        tblRelatorio.getColumns().clear();
//
//        TableColumn<Produto, String> nomeCol = new TableColumn<>("Nome");
//        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
//
//        TableColumn<Produto, String> codigoCol = new TableColumn<>("Código");
//        codigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));
//
//        TableColumn<Produto, Integer> quantidadeCol = new TableColumn<>("Quantidade");
//        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade_inicial"));
//
//        TableColumn<Produto, Double> precoCol = new TableColumn<>("Preço Unitário");
//        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco_unitario"));
//
//        TableColumn<Produto, String> validadeCol = new TableColumn<>("Validade");
//        validadeCol.setCellValueFactory(new PropertyValueFactory<>("validade"));
//
//        tblRelatorio.getColumns().addAll(nomeCol, codigoCol, quantidadeCol, precoCol, validadeCol);
//    }

    @FXML
    private void exportarRelatorioPDF() {
        String tipoRelatorio = tipoRelatorioComboBox.getValue();

        if (tipoRelatorio == null) {
            mostrarAlerta("Selecione um tipo de relatório!");
            return;
        }

        List<Produto> produtos = tblRelatorio.getItems();
        if (produtos == null || produtos.isEmpty()) {
            mostrarAlerta("Não há dados para exportar.");
            return;
        }

        try {
            gerarPdfRelatorio(produtos, "Relatório - " + tipoRelatorio);
            mostrarAlerta("PDF gerado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao gerar PDF: " + e.getMessage());
        }
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
            System.out.println("Usuario Atual: " + usuarioLogado);
            TelaLoader.carregarTela("/org/example/estoque/telaInicial.fxml", "SOLLID COMERCIO LTDA", usuarioLogado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
