package Estoque.services;


import Estoque.Entities.Categoria;
import Estoque.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Categoria findById(Long id_categoria) {
        Optional<Categoria> obj = repository.findById(id_categoria);
        return obj.get();
    }
}
