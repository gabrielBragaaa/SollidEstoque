package Estoque.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID= 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_categoria;
    private String nome;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Produto> produto = new ArrayList<>();

    public Categoria(){

    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id_categoria == categoria.id_categoria;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_categoria);
    }

    @Override
    public String toString() {
        return nome ;
    }
}


