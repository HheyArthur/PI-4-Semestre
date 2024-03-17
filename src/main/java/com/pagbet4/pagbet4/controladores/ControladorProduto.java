package com.pagbet4.pagbet4.controladores;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoProduto;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/produtos")
public class ControladorProduto {

    @Autowired
    private RepoProduto repoProduto;

    @GetMapping
    public List<Produto> listarProdutos() {
        List<Produto> produtos = repoProduto.findAll();
        Collections.reverse(produtos);
        return produtos;
    }

    @GetMapping("/{id}")
    public Produto listarProduto(@PathVariable Long id) {
        return repoProduto.findById(id).get();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto) {

        if (produto.getNomeProduto() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do produto ausente");
        }
        if (produto.getDescricao() == null) {
            produto.setDescricao("Descrição pendente");
        }
        if (produto.getPreco() == null || produto.getPreco().equals(BigDecimal.ZERO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preço inserido inválido");
        }
        if (produto.getQuantidade() == 0 || produto.getQuantidade() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inserida inválida");
        }

        Produto novoProduto = repoProduto.save(produto);
        return ResponseEntity.ok(novoProduto);
    }

    @PutMapping("/{id}")
    public Produto atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return repoProduto.save(produto);
    }

    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id) {
        repoProduto.deleteById(id);
    }
}
