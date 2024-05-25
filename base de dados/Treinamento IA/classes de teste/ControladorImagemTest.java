package com.pagbet4.pagbet4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbet4.pagbet4.controladores.ControladorImagem;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.imagemDTO.ImagemDTO;
import com.pagbet4.pagbet4.repositorio.RepoImagem;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.servicos.ServicoImagem;

@WebMvcTest(ControladorImagem.class)
public class ControladorImagemTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicoImagem servicoImagem;

    @MockBean
    private RepoImagem repoImagem;

    @MockBean
    private RepoProduto repoProduto;

    @Test
    public void cadastrarImagem_ComIdProdutoValido_DeveRetornarStatus200() throws Exception {
        ImagemDTO imagemDTO = new ImagemDTO();
        imagemDTO.setUrl("http://example.com/image.jpg");
        imagemDTO.setIdProduto(1L);

        Produto produto = mock(Produto.class);
        when(repoProduto.findById(1L)).thenReturn(Optional.of(produto));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(imagemDTO);

        mockMvc.perform(post("/imagens/cadastrarImagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void cadastrarImagem_ComIdProdutoInvalido_DeveRetornarStatus400() throws Exception {
        ImagemDTO imagemDTO = new ImagemDTO();
        imagemDTO.setUrl("http://example.com/image.jpg");
        imagemDTO.setIdProduto(null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(imagemDTO);

        mockMvc.perform(post("/imagens/cadastrarImagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}