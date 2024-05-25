package com.pagbet4.pagbet4;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbet4.pagbet4.controladores.ControladorProduto;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.servicos.ServicoProduto;

@WebMvcTest(ControladorProduto.class)
public class ControladorProdutoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepoProduto repoProduto;

    @MockBean
    private ServicoProduto servicoProduto;

    @Test
    public void listarProdutos_DeveRetornarStatus200() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    public void listarProduto_ComIdValido_DeveRetornarStatus200() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        when(repoProduto.findById(1L)).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void listarProduto_ComIdInvalido_DeveRetornarStatus404() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void pesquisaProdutoNome_DeveRetornarListaDeProdutos() throws Exception {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto());
        when(repoProduto.findAllByNomeProdutoContaining("Teste")).thenReturn(produtos);

        mockMvc.perform(get("/produtos/pesquisa/Teste"))
                .andExpect(status().isOk());
    }

    @Test
    public void criarProduto_ComDadosValidos_DeveRetornarStatus200() throws Exception {
        Produto produto = new Produto();
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(post("/produtos/cadastrarProduto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void criarProduto_ComNomeProdutoNulo_DeveRetornarStatus400() throws Exception {
        Produto produto = new Produto();
        produto.setNomeProduto(null);
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(post("/produtos/cadastrarProduto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void criarProduto_ComPrecoInvalido_DeveRetornarStatus400() throws Exception {
        Produto produto = new Produto();
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(BigDecimal.ZERO);
        produto.setQuantidade(10);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(post("/produtos/cadastrarProduto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void criarProduto_ComQuantidadeInvalida_DeveRetornarStatus400() throws Exception {
        Produto produto = new Produto();
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(0);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(post("/produtos/cadastrarProduto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void atualizarProduto_ComNomeValido_DeveRetornarStatus200() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        when(repoProduto.findByNomeProduto("Teste")).thenReturn(Optional.of(produto));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(put("/produtos/atualizaProduto/Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void atualizarProduto_ComNomeInvalido_DeveRetornarStatus404() throws Exception {
        when(repoProduto.findByNomeProduto("Teste")).thenReturn(Optional.empty());

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(put("/produtos/atualizaProduto/Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void atualizarProdutoPorId_ComIdValido_DeveRetornarStatus200() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        when(repoProduto.findById(1L)).thenReturn(Optional.of(produto));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(put("/produtos/atualizaProdutoPorId/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void atualizarProdutoPorId_ComIdInvalido_DeveRetornarStatus404() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNomeProduto("Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(new BigDecimal(10.00));
        produto.setQuantidade(10);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(put("/produtos/atualizaProdutoPorId/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void ativarDesativarProduto_ComIdValido_DeveRetornarStatus200() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setAtivo(true);

        when(repoProduto.findById(1L)).thenReturn(Optional.of(produto));

        mockMvc.perform(put("/produtos/ativarDesativarProduto/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void ativarDesativarProduto_ComIdInvalido_DeveRetornarStatus404() throws Exception {
        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/produtos/ativarDesativarProduto/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletarProduto_ComIdValido_DeveExecutarSemErros() throws Exception {
        mockMvc.perform(put("/produtos/deletePorId/1"))
                .andExpect(status().isOk());
    }

}