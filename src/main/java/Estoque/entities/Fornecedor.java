package Estoque.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_fornecedor;
    @Column(name = "nome")
    private String nomeFornecedor;

    @OneToMany(mappedBy = "fornecedor")
    @JsonIgnore
    private List<Produto> produto = new ArrayList<>();

    public Fornecedor() {

    }

    public Fornecedor(int id_fornecedor, String nome) {
        this.id_fornecedor = id_fornecedor;
        this.nomeFornecedor = nome;

    }

    public int getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(int id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {

        this.nomeFornecedor = nomeFornecedor;
    }

    public List<Produto> getProduto() {
        return produto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return id_fornecedor == that.id_fornecedor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_fornecedor);
    }


    @Override
    public String toString() {
        return nomeFornecedor;
    }
}
