package com.pagbet4.pagbet4.servicos;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoProduto;

@Service
public class ServicoProduto {

    @Autowired
    private RepoProduto repoProduto;

    public ResponseEntity<?> criarProduto(Produto produto) {
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

    public ResponseEntity<?> atualizarProduto(String nomeProduto, Produto produto) {
        Optional<Produto> produtoExistenteOpt = repoProduto.findByNomeProduto(nomeProduto);

        if (produtoExistenteOpt.isPresent()) {
            Produto produtoExistente = produtoExistenteOpt.get();
            atualizarProduto(produtoExistente, produto);
            Produto produtoAtualizado = repoProduto.save(produtoExistente);
            return ResponseEntity.ok(produtoAtualizado);
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> atualizarProdutoPorId(Long id, Produto produto) {
        Optional<Produto> produtoExistenteOpt = repoProduto.findById(id);

        if (produtoExistenteOpt.isPresent()) {
            Produto produtoExistente = produtoExistenteOpt.get();
            atualizarProduto(produtoExistente, produto);
            Produto produtoAtualizado = repoProduto.save(produtoExistente);
            return ResponseEntity.ok(produtoAtualizado);
        }

        return ResponseEntity.notFound().build();
    }

    private void atualizarProduto(Produto produtoExistente, Produto produto) {
        if (produto.getNomeProduto() != null && !produto.getNomeProduto().isEmpty() && !produto.getNomeProduto().equals(produtoExistente.getNomeProduto())) {
            produtoExistente.setNomeProduto(produto.getNomeProduto());
        }

        if (produto.getDescricao() != null && !produto.getDescricao().isEmpty() && !produto.getDescricao().equals(produtoExistente.getDescricao())) {
            produtoExistente.setDescricao(produto.getDescricao());
        }

        if (produto.getPreco() != null && !produto.getPreco().equals(BigDecimal.ZERO) && !produto.getPreco().equals(produtoExistente.getPreco())) {
            produtoExistente.setPreco(produto.getPreco());
        }

        if (produto.getQuantidade() != 0 && produto.getQuantidade() != produtoExistente.getQuantidade()) {
            produtoExistente.setQuantidade(produto.getQuantidade());
        }

        if (produto.getImagemPrincipal() != null && !produto.getImagemPrincipal().isEmpty() && !produto.getImagemPrincipal().equals(produtoExistente.getImagemPrincipal())) {
            produtoExistente.setImagemPrincipal(produto.getImagemPrincipal());
        }
    }
}