package com.pagbet4.pagbet4.controladores;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoProduto;

@WebMvcTest(ControladorProduto.class)
public class ControladorProdutoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepoProduto repoProduto;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Produto produtoTeste;

    @BeforeEach
    public void setUp() {
        produtoTeste = new Produto();
        produtoTeste.setId(1L);
        produtoTeste.setNomeProduto("Produto Teste");
        produtoTeste.setDescricao("Descrição do Produto Teste");
        produtoTeste.setPreco(new BigDecimal("10.00"));
        produtoTeste.setQuantidade(10);
    }

    @Test
    public void testListarProdutos() throws Exception {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(produtoTeste);
        when(repoProduto.findAll()).thenReturn(produtos);

        mockMvc.perform(get("/produtos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testListarProdutoPorId() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.of(produtoTeste));

        mockMvc.perform(get("/produtos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testListarProdutoPorIdNaoEncontrado() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/produtos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCriarProduto() throws Exception {
        when(repoProduto.save(Mockito.any(Produto.class))).thenReturn(produtoTeste);

        mockMvc.perform(post("/produtos/cadastrarProduto").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoTeste)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCriarProdutoNomeNulo() throws Exception {
        Produto produtoInvalido = new Produto();
        produtoInvalido.setDescricao("Descrição");
        produtoInvalido.setPreco(new BigDecimal("10.00"));
        produtoInvalido.setQuantidade(10);

        mockMvc.perform(post("/produtos/cadastrarProduto").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCriarProdutoPrecoInvalido() throws Exception {
        Produto produtoInvalido = new Produto();
        produtoInvalido.setNomeProduto("Produto Inválido");
        produtoInvalido.setDescricao("Descrição");
        produtoInvalido.setPreco(BigDecimal.ZERO);
        produtoInvalido.setQuantidade(10);

        mockMvc.perform(post("/produtos/cadastrarProduto").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCriarProdutoQuantidadeInvalida() throws Exception {
        Produto produtoInvalido = new Produto();
        produtoInvalido.setNomeProduto("Produto Inválido");
        produtoInvalido.setDescricao("Descrição");
        produtoInvalido.setPreco(new BigDecimal("10.00"));
        produtoInvalido.setQuantidade(0);

        mockMvc.perform(post("/produtos/cadastrarProduto").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAtualizarProduto() throws Exception {
        when(repoProduto.findByNomeProduto("Produto Teste")).thenReturn(Optional.of(produtoTeste));
        when(repoProduto.save(Mockito.any(Produto.class))).thenReturn(produtoTeste);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Produto Teste Atualizado");
        produtoAtualizado.setDescricao("Descrição Atualizada");
        produtoAtualizado.setPreco(new BigDecimal("15.00"));
        produtoAtualizado.setQuantidade(15);

        mockMvc.perform(put("/produtos/atualizaProduto/Produto Teste").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAtualizarProdutoNaoEncontrado() throws Exception {
        when(repoProduto.findByNomeProduto("Produto Teste")).thenReturn(Optional.empty());

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Produto Teste Atualizado");
        produtoAtualizado.setDescricao("Descrição Atualizada");
        produtoAtualizado.setPreco(new BigDecimal("15.00"));
        produtoAtualizado.setQuantidade(15);

        mockMvc.perform(put("/produtos/atualizaProduto/Produto Teste").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAtualizarProdutoPorId() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.of(produtoTeste));
        when(repoProduto.save(Mockito.any(Produto.class))).thenReturn(produtoTeste);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Produto Teste Atualizado");
        produtoAtualizado.setDescricao("Descrição Atualizada");
        produtoAtualizado.setPreco(new BigDecimal("15.00"));
        produtoAtualizado.setQuantidade(15);

        mockMvc.perform(put("/produtos/atualizaProdutoPorId/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAtualizarProdutoPorIdNaoEncontrado() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNomeProduto("Produto Teste Atualizado");
        produtoAtualizado.setDescricao("Descrição Atualizada");
        produtoAtualizado.setPreco(new BigDecimal("15.00"));
        produtoAtualizado.setQuantidade(15);

        mockMvc.perform(put("/produtos/atualizaProdutoPorId/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAtivarDesativarProduto() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.of(produtoTeste));
        when(repoProduto.save(Mockito.any(Produto.class))).thenReturn(produtoTeste);

        mockMvc.perform(put("/produtos/ativarDesativarProduto/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAtivarDesativarProdutoNaoEncontrado() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/produtos/ativarDesativarProduto/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletarProduto() throws Exception {
        mockMvc.perform(delete("/produtos/deletePorId/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}