package com.pagbet4.pagbet4.controladores;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @SuppressWarnings("null")
    @GetMapping("/{id}")
    public Produto listarProduto(@PathVariable Long id) {
        return repoProduto.findById(id).get();
    }

    @GetMapping("/pesquisa/{nome}")
    public List<Produto> pesquisaProdutoNome(@PathVariable String nome) {
        List<Produto> produtos = repoProduto.findAllByNomeProdutoContaining(nome);
        return produtos;
    }
    

    @PostMapping("/cadastrarProduto")
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

    @SuppressWarnings("null")
    @PutMapping("/atualizaProduto/{nomeProduto}")
    public ResponseEntity<?> atualizarProduto(@PathVariable String nomeProduto, @RequestBody Produto produto) {

        Optional<Produto> produtoExistenteOpt = repoProduto.findByNomeProduto(nomeProduto);

        if (produtoExistenteOpt.isPresent()) {
            Produto produtoExistente = produtoExistenteOpt.get();

            boolean isUpdated = false;

            if (produto.getNomeProduto() != null && !produto.getNomeProduto().isEmpty() && !produto.getNomeProduto().equals(produtoExistente.getNomeProduto())) {
                produtoExistente.setNomeProduto(produto.getNomeProduto());
                isUpdated = true;
            }

            if (produto.getDescricao() != null && !produto.getDescricao().isEmpty() && !produto.getDescricao().equals(produtoExistente.getDescricao())) {
                produtoExistente.setDescricao(produto.getDescricao());
                isUpdated = true;
            }

            if (produto.getPreco() != null && !produto.getPreco().equals(BigDecimal.ZERO) && !produto.getPreco().equals(produtoExistente.getPreco())) {
                produtoExistente.setPreco(produto.getPreco());
                isUpdated = true;
            }

            if (produto.getQuantidade() != 0 && produto.getQuantidade() != produtoExistente.getQuantidade()) {
                produtoExistente.setQuantidade(produto.getQuantidade());
                isUpdated = true;
            }

            if (produto.getImagem() != null && !produto.getImagem().isEmpty() && !produto.getImagem().equals(produtoExistente.getImagem())) {
                produtoExistente.setImagem(produto.getImagem());
                isUpdated = true;
            }

            if (isUpdated) {
                Produto produtoAtualizado = repoProduto.save(produtoExistente);
                return ResponseEntity.ok(produtoAtualizado);
            } else {
                return ResponseEntity.ok(produtoExistente);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @SuppressWarnings("null")
    @PutMapping("/atualizaProdutoPorId/{id}")
    public ResponseEntity<?> atualizarProdutoPorId(@PathVariable Long id, @RequestBody Produto produto) {

        Optional<Produto> produtoExistenteOpt = repoProduto.findById(id);

        if (produtoExistenteOpt.isPresent()) {
            Produto produtoExistente = produtoExistenteOpt.get();

            boolean isUpdated = false;

            if (produto.getNomeProduto() != null && !produto.getNomeProduto().isEmpty() && !produto.getNomeProduto().equals(produtoExistente.getNomeProduto())) {
                produtoExistente.setNomeProduto(produto.getNomeProduto());
                isUpdated = true;
            }

            if (produto.getDescricao() != null && !produto.getDescricao().isEmpty() && !produto.getDescricao().equals(produtoExistente.getDescricao())) {
                produtoExistente.setDescricao(produto.getDescricao());
                isUpdated = true;
            }

            if (produto.getPreco() != null && !produto.getPreco().equals(BigDecimal.ZERO) && !produto.getPreco().equals(produtoExistente.getPreco())) {
                produtoExistente.setPreco(produto.getPreco());
                isUpdated = true;
            }

            if (produto.getQuantidade() != 0 && produto.getQuantidade() != produtoExistente.getQuantidade()) {
                produtoExistente.setQuantidade(produto.getQuantidade());
                isUpdated = true;
            }

            if (produto.getImagem() != null && !produto.getImagem().isEmpty() && !produto.getImagem().equals(produtoExistente.getImagem())) {
                produtoExistente.setImagem(produto.getImagem());
                isUpdated = true;
            }

            if (isUpdated) {
                Produto produtoAtualizado = repoProduto.save(produtoExistente);
                return ResponseEntity.ok(produtoAtualizado);
            } else {
                return ResponseEntity.ok(produtoExistente);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("ativarDesativarProduto/{id}")
    public ResponseEntity<?> ativarDesativarProutos(@PathVariable Long id) {
        Optional<Produto> produto = repoProduto.findById(id);
        if (produto.isPresent()) {
            Produto produtoStatusAlterado = produto.get();
            produtoStatusAlterado.setAtivo(!produtoStatusAlterado.isAtivo());
            repoProduto.save(produtoStatusAlterado);
            return ResponseEntity.ok(produtoStatusAlterado);
        }
        return ResponseEntity.notFound().build();
    }

    @SuppressWarnings("null")
    @DeleteMapping("/deletePorId/{id}")
    public void deletarProduto(@PathVariable Long id) {
        repoProduto.deleteById(id);
    }
    
}
