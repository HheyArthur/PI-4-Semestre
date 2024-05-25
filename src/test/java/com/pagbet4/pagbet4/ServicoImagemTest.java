package com.pagbet4.pagbet4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pagbet4.pagbet4.entidades.Imagem;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.imagemDTO.ImagemDTO;
import com.pagbet4.pagbet4.imagemDTO.ImagemUrlDTO;
import com.pagbet4.pagbet4.repositorio.RepoImagem;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.servicos.ServicoImagem;

@SpringBootTest
public class ServicoImagemTest {

    @Autowired
    private ServicoImagem servicoImagem;

    @MockBean
    private RepoImagem repoImagem;

    @MockBean
    private RepoProduto repoProduto;

    @SuppressWarnings("null")
    @Test
    public void cadastrarImagem_ComIdProdutoValido_DeveRetornarImagemUrlDTO() {
        ImagemDTO imagemDTO = new ImagemDTO();
        imagemDTO.setUrl("http://example.com/image.jpg");
        imagemDTO.setIdProduto(1L);

        Produto produto = mock(Produto.class);
        when(repoProduto.findById(1L)).thenReturn(Optional.of(produto));

        // Criando um objeto Imagem com URL válida para ser retornado pelo mock
        Imagem imagemMock = new Imagem();
        imagemMock.setUrl("http://example.com/image.jpg");

        // Configurando o mock do repositório para retornar a imagem mockada
        when(repoImagem.save(any(Imagem.class))).thenReturn(imagemMock);

        ResponseEntity<?> response = servicoImagem.cadastrarImagem(imagemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ImagemUrlDTO imagemUrlDTO = (ImagemUrlDTO) response.getBody();
        assertEquals("http://example.com/image.jpg", imagemUrlDTO.getUrl());
    }

    @Test
    public void cadastrarImagem_ComIdProdutoInvalido_DeveRetornarStatus400() {
        ImagemDTO imagemDTO = new ImagemDTO();
        imagemDTO.setUrl("http://example.com/image.jpg");
        imagemDTO.setIdProduto(null);

        ResponseEntity<?> response = servicoImagem.cadastrarImagem(imagemDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID do produto não informado", response.getBody());
    }

    @Test
    public void cadastrarImagem_ComProdutoNaoEncontrado_DeveRetornarStatus400() {
        ImagemDTO imagemDTO = new ImagemDTO();
        imagemDTO.setUrl("http://example.com/image.jpg");
        imagemDTO.setIdProduto(1L);

        when(repoProduto.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = servicoImagem.cadastrarImagem(imagemDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Produto não encontrado para o ID informado", response.getBody());
    }
}