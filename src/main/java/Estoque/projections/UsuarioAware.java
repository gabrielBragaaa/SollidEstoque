package Estoque.projections;

import Estoque.entities.Usuario;

public interface UsuarioAware {
    void setUsuarioLogado(Usuario usuario);

}

