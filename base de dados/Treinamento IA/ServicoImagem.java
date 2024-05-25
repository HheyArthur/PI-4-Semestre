package com.pagbet4.pagbet4.servicos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pagbet4.pagbet4.entidades.Imagem;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.imagemDTO.ImagemDTO;
import com.pagbet4.pagbet4.imagemDTO.ImagemUrlDTO;
import com.pagbet4.pagbet4.repositorio.RepoImagem;
import com.pagbet4.pagbet4.repositorio.RepoProduto;

@Service
public class ServicoImagem {

    @Autowired
    private RepoImagem repoImagem;

    @Autowired
    private RepoProduto repoProduto;

    public ResponseEntity<?> cadastrarImagem(ImagemDTO imagemDTO) {
        if (imagemDTO.getIdProduto() == null) {
            return ResponseEntity.badRequest().body("ID do produto não informado");
        }
        // Verificar se o produto existe
        Optional<Produto> produtoOptional = repoProduto.findById(imagemDTO.getIdProduto());
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Produto não encontrado para o ID informado");
        }
    
        // Criar a nova imagem
        Imagem imagem = new Imagem();
        imagem.setUrl(imagemDTO.getUrl());
        imagem.setProduto(produtoOptional.get());
    
        // Salvar a imagem no repositório
        Imagem imagemSalva = repoImagem.save(imagem);
    
        // Criar um novo DTO com a URL da imagem
        ImagemUrlDTO imagemUrlDTO = new ImagemUrlDTO();
        imagemUrlDTO.setUrl(imagemSalva.getUrl());
    
        return ResponseEntity.ok(imagemUrlDTO);
    }
}