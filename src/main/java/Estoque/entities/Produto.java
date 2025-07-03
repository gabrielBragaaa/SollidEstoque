<<<<<<< HEAD
package Estoque.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_produto;

    private String nome;
    private String codigo;
    private int quantidade_inicial;
    private Double preco_unitario;

    @Column(name = "validade")
    private LocalDate validade;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Produto() {}

    public Produto(Categoria categoria, String codigo, Fornecedor fornecedor, Long id_produto, String nome,
                   Double preco_unitario, int quantidade_inicial, LocalDate validade) {
        this.categoria = categoria;
        this.codigo = codigo;
        this.fornecedor = fornecedor;
        this.id_produto = id_produto;
        this.nome = nome;
        this.preco_unitario = preco_unitario;
        this.quantidade_inicial = quantidade_inicial;
        this.validade = validade;
    }

    // Getters e Setters

    public Long getId_produto() {
        return id_produto;
    }

    public void setId_produto(Long id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade_inicial() {
        return quantidade_inicial;
    }

    public void setQuantidade_inicial(int quantidade_inicial) {
        this.quantidade_inicial = quantidade_inicial;
    }

    public Double getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(Double preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // equals, hashCode e toString

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id_produto == produto.id_produto &&
                Objects.equals(nome, produto.nome) &&
                Objects.equals(codigo, produto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_produto, nome, codigo);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "codigo='" + codigo + '\'' +
                ", id_produto=" + id_produto +
                ", nome='" + nome + '\'' +
                ", quantidade_inicial=" + quantidade_inicial +
                ", preco_unitario=" + preco_unitario +
                ", validade=" + validade +
                ", categoria=" + categoria +
                ", fornecedor=" + fornecedor +
                '}';
    }
}
=======
package Estoque.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_produto;

    private String nome;
    private String codigo;
    private int quantidade_inicial;
    private Double preco_unitario;

    @Column(name = "validade")
    private LocalDate validade;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Produto() {}

    public Produto(Categoria categoria, String codigo, Fornecedor fornecedor, Long id_produto, String nome,
                   Double preco_unitario, int quantidade_inicial, LocalDate validade) {
        this.categoria = categoria;
        this.codigo = codigo;
        this.fornecedor = fornecedor;
        this.id_produto = id_produto;
        this.nome = nome;
        this.preco_unitario = preco_unitario;
        this.quantidade_inicial = quantidade_inicial;
        this.validade = validade;
    }

    // Getters e Setters

    public Long getId_produto() {
        return id_produto;
    }

    public void setId_produto(Long id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade_inicial() {
        return quantidade_inicial;
    }

    public void setQuantidade_inicial(int quantidade_inicial) {
        this.quantidade_inicial = quantidade_inicial;
    }

    public Double getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(Double preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // equals, hashCode e toString

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id_produto == produto.id_produto &&
                Objects.equals(nome, produto.nome) &&
                Objects.equals(codigo, produto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_produto, nome, codigo);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "codigo='" + codigo + '\'' +
                ", id_produto=" + id_produto +
                ", nome='" + nome + '\'' +
                ", quantidade_inicial=" + quantidade_inicial +
                ", preco_unitario=" + preco_unitario +
                ", validade=" + validade +
                ", categoria=" + categoria +
                ", fornecedor=" + fornecedor +
                '}';
    }
}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
