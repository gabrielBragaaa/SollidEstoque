package Estoque.services;


import Estoque.entities.Fornecedor;
import Estoque.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    public List<Fornecedor> findAll() {
        return repository.findAll();
    }

    public Fornecedor findById(Long id_fornecedor) {
        Optional<Fornecedor> obj = repository.findById(id_fornecedor);
        return obj.get();
    }


}
