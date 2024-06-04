package com.pagbet4.pagbet4.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagbet4.pagbet4.entidades.Compra;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/compras")
public class ControladorCompra {
    @Autowired
    private ServicoCompra servicoCompra;

    @GetMapping("/listarCompras")
    public List<Compra> listarCompras() {
        return servicoCompra.listarCompras();
    }

    @PostMapping
    public ResponseEntity<Compra> cadastrarCompra(@RequestBody Compra compra) {
        return servicoCompra.cadastrarCompra(compra);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obterCompra(@PathVariable Long id) {
        return servicoCompra.obterCompra(id);
    }
}
