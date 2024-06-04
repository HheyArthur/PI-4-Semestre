package com.pagbet4.pagbet4.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pagbet4.pagbet4.entidades.Compra;
import com.pagbet4.pagbet4.repositorio.RepoCompra;

@Service
public class ServicoCompra {

    @Autowired
    private RepoCompra repositorioCompra;

    public List<Compra> listarCompras() {
        return repositorioCompra.findAll();
    }

    public ResponseEntity<Compra> cadastrarCompra(Compra compra) {
        Compra novaCompra = repositorioCompra.save(compra);
        return ResponseEntity.ok(novaCompra);
    }

    public ResponseEntity<Compra> obterCompra(Long id) {
        Optional<Compra> compra = repositorioCompra.findById(id);
        if (compra.isPresent()) {
            return ResponseEntity.ok(compra.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
