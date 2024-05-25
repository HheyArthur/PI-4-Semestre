package com.pagbet4.pagbet4.controladores;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.servicos.ServicoProduto;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/produtos")
public class ControladorProduto {

    @Autowired
    private RepoProduto repoProduto;

    @Autowired
    private ServicoProduto servicoProduto;

    @GetMapping
    public List<Produto> listarProdutos() {
        Pageable pageable = PageRequest.of(0, 11, Sort.by(Sort.Direction.DESC, "id"));
        Page<Produto> produtosPage = repoProduto.findAll(pageable);
        return produtosPage.getContent();
    }

    @SuppressWarnings("null")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> listarProduto(@PathVariable Long id) {
        Optional<Produto> optionalProduto = repoProduto.findById(id);
        if (optionalProduto.isPresent()) {
            return new ResponseEntity<>(optionalProduto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pesquisa/{nome}")
    public List<Produto> pesquisaProdutoNome(@PathVariable String nome) {
        List<Produto> produtos = repoProduto.findAllByNomeProdutoContaining(nome);
        return produtos;
    }
    

    @PostMapping("/cadastrarProduto")
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto) {
        return servicoProduto.criarProduto(produto);
    }

    @SuppressWarnings("null")
    @PutMapping("/atualizaProduto/{nomeProduto}")
    public ResponseEntity<?> atualizarProduto(@PathVariable String nomeProduto, @RequestBody Produto produto) {
        return servicoProduto.atualizarProduto(nomeProduto, produto);
    }

    @SuppressWarnings("null")
    @PutMapping("/atualizaProdutoPorId/{id}")
    public ResponseEntity<?> atualizarProdutoPorId(@PathVariable Long id, @RequestBody Produto produto) {
        return servicoProduto.atualizarProdutoPorId(id, produto);
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
