package com.pagbet4.pagbet4.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagbet4.pagbet4.compraDTO.CompraDTO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/compras")
public class ControladorCompra {
    @Autowired
    private ServicoCompra servicoCompra;

    @GetMapping("/listarCompras")
    public ResponseEntity<?> listarCompras() {
        return servicoCompra.listarCompras();
    }

    @PostMapping("/criarCompra")
    public ResponseEntity<?> criarCompra(@RequestBody CompraDTO compraDTO) {
        return servicoCompra.registrarCompra(compraDTO);
    }
}