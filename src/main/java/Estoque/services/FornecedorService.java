<<<<<<< HEAD
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
    private FornecedorRepository fornecedorRepository;

    public List<Fornecedor> findAll() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor findById(Long id_fornecedor) {
        Optional<Fornecedor> obj = fornecedorRepository.findById(id_fornecedor);
        return obj.get();
    }


}
=======
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
    private FornecedorRepository fornecedorRepository;

    public List<Fornecedor> findAll() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor findById(Long id_fornecedor) {
        Optional<Fornecedor> obj = fornecedorRepository.findById(id_fornecedor);
        return obj.get();
    }


}
>>>>>>> 8938a1b0403e078427565c1ae42d2318d6681d57
