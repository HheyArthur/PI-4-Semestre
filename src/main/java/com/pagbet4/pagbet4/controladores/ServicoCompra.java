package com.pagbet4.pagbet4.controladores;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pagbet4.pagbet4.entidades.Compra;
import com.pagbet4.pagbet4.entidades.ItemCompra;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoCompra;
import com.pagbet4.pagbet4.repositorio.RepoProduto;

@Service
public class ServicoCompra {

    @Autowired
    private RepoCompra repositorioCompra;

    @Autowired
    private RepoProduto repoProduto;

    public List<Compra> listarCompras() {
        return repositorioCompra.findAll();
    }

    @Transactional
    public ResponseEntity<?> cadastrarCompra(Compra compra) {
        // Validações
        if (compra.getUsuario() == null) {
            return ResponseEntity.badRequest().body("Usuário não informado.");
        }

        // Calcula o valor total da compra
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemCompra item : compra.getProdutos()) {
            if (item.getProduto() == null || item.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("Produto inválido ou quantidade inválida.");
            }
            Optional<Produto> produtoOptional = repoProduto.findById(item.getProduto().getId());
            if (produtoOptional.isPresent()) {
                Produto produto = produtoOptional.get();
                item.setPrecoUnitario(produto.getPreco().doubleValue());
                valorTotal = valorTotal.add(BigDecimal.valueOf(item.getQuantidade() * item.getPrecoUnitario()));

                // Associar o ItemCompra à Compra
                item.setCompra(compra); // Adiciona esta linha para associar o item à compra
            } else {
                return ResponseEntity.badRequest().body("Produto não encontrado.");
            }
        }

        // Define a data da compra
        compra.setDataCompra(LocalDateTime.now());

        // Salva a compra no banco de dados
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