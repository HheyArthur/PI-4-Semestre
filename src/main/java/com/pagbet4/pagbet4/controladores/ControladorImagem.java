package com.pagbet4.pagbet4.controladores;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagbet4.pagbet4.imagemDTO.ImagemDTO;

import com.pagbet4.pagbet4.repositorio.RepoImagem;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.servicos.ServicoImagem;

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

    @Autowired
    private ServicoImagem servicoImagem;

    @PostMapping("/cadastrarImagens")
    public ResponseEntity<?> cadastrarImagem(@RequestBody ImagemDTO imagemDTO) {
        return servicoImagem.cadastrarImagem(imagemDTO);
    }
}