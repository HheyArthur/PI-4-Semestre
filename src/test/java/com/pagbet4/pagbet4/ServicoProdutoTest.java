package com.pagbet4.pagbet4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.servicos.ServicoProduto;

@SpringBootTest
public class ServicoProdutoTest {

    @Autowired
    private ServicoProduto servicoProduto;

    @MockBean
    private RepoProduto repoProduto;

    @Test
    public void criarProduto_ComDadosValidos_DeveRetornarStatus200() {
        Produto produto = new Produto();
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        when(repoProduto.save(produto)).thenReturn(produto);

        ResponseEntity<?> response = servicoProduto.criarProduto(produto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produto, response.getBody());
    }

    @Test
    public void criarProduto_ComNomeProdutoNulo_DeveRetornarStatus400() {
        Produto produto = new Produto();
        produto.setNomeProduto(null);
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        ResponseEntity<?> response = servicoProduto.criarProduto(produto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nome do produto ausente", response.getBody());
    }

    @Test
    public void criarProduto_ComPrecoInvalido_DeveRetornarStatus400() {
        Produto produto = new Produto();
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(BigDecimal.ZERO);
        produto.setQuantidade(10);

        ResponseEntity<?> response = servicoProduto.criarProduto(produto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Preço inserido inválido", response.getBody());
    }

    @Test
    public void criarProduto_ComQuantidadeInvalida_DeveRetornarStatus400() {
        Produto produto = new Produto();
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(0);

        ResponseEntity<?> response = servicoProduto.criarProduto(produto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Quantidade inserida inválida", response.getBody());
    }

    @Test
    public void atualizarProduto_ComNomeValido_DeveRetornarStatus200() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNomeProduto("Teste");
        produtoExistente.setDescricao("Descrição Teste");
        produtoExistente.setPreco(new BigDecimal(10.00));
        produtoExistente.setQuantidade(10);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Novo Teste");
        produtoAtualizado.setDescricao("Nova Descrição Teste");
        produtoAtualizado.setPreco(new BigDecimal(15.00));
        produtoAtualizado.setQuantidade(15);

        when(repoProduto.findByNomeProduto("Teste")).thenReturn(Optional.of(produtoExistente));
        when(repoProduto.save(produtoExistente)).thenReturn(produtoAtualizado);

        ResponseEntity<?> response = servicoProduto.atualizarProduto("Teste", produtoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtoAtualizado, response.getBody());
    }

    @Test
    public void atualizarProduto_ComNomeInvalido_DeveRetornarStatus404() {
        when(repoProduto.findByNomeProduto("Teste")).thenReturn(Optional.empty());

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Novo Teste");
        produtoAtualizado.setDescricao("Nova Descrição Teste");
        produtoAtualizado.setPreco(new BigDecimal(15.00));
        produtoAtualizado.setQuantidade(15);

        ResponseEntity<?> response = servicoProduto.atualizarProduto("Teste", produtoAtualizado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void atualizarProdutoPorId_ComIdValido_DeveRetornarStatus200() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNomeProduto("Teste");
        produtoExistente.setDescricao("Descrição Teste");
        produtoExistente.setPreco(new BigDecimal(10.00));
        produtoExistente.setQuantidade(10);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Novo Teste");
        produtoAtualizado.setDescricao("Nova Descrição Teste");
        produtoAtualizado.setPreco(new BigDecimal(15.00));
        produtoAtualizado.setQuantidade(15);

        when(repoProduto.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(repoProduto.save(produtoExistente)).thenReturn(produtoAtualizado);

        ResponseEntity<?> response = servicoProduto.atualizarProdutoPorId(1L, produtoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtoAtualizado, response.getBody());
    }

    @Test
    public void atualizarProdutoPorId_ComIdInvalido_DeveRetornarStatus404() {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Novo Teste");
        produtoAtualizado.setDescricao("Nova Descrição Teste");
        produtoAtualizado.setPreco(new BigDecimal(15.00));
        produtoAtualizado.setQuantidade(15);

        ResponseEntity<?> response = servicoProduto.atualizarProdutoPorId(1L, produtoAtualizado);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}