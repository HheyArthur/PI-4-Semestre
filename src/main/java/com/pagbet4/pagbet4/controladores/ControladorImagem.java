package com.pagbet4.pagbet4.controladores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagbet4.pagbet4.entidades.Imagem;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.imagemDTO.ImagemDTO;
import com.pagbet4.pagbet4.imagemDTO.ImagemUrlDTO;
import com.pagbet4.pagbet4.repositorio.RepoImagem;
import com.pagbet4.pagbet4.repositorio.RepoProduto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/imagens")
public class ControladorImagem {

    @Autowired
    private RepoImagem repoImagem;

    @Autowired
    private RepoProduto repoProduto;

    @PostMapping("/cadastrarImagens")
    public ResponseEntity<?> cadastrarImagem(@RequestBody ImagemDTO imagemDTO) {

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
