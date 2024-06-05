package com.pagbet4.pagbet4.controladores;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pagbet4.pagbet4.compraDTO.CompraDTO;
import com.pagbet4.pagbet4.entidades.Compra;
import com.pagbet4.pagbet4.entidades.ItemCompra;
import com.pagbet4.pagbet4.entidades.Produto;
import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.repositorio.RepoCompra;
import com.pagbet4.pagbet4.repositorio.RepoItemCompra;
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;

@Service
public class ServicoCompra {

    @Autowired
    private RepoCompra repoCompra;

    @Autowired
    private RepoItemCompra repoItemCompra;

    @Autowired
    private RepoProduto repoProduto;

    @Autowired
    private RepoUsuario repoUsuario;

    @Transactional
    public ResponseEntity<?> registrarCompra(CompraDTO compraDTO) {
        Optional<Usuario> usuarioOptional = repoUsuario.findById(compraDTO.getUsuarioId());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Compra compra = new Compra();
        compra.setUsuario(usuarioOptional.get());
        compra.setDataCompra(LocalDateTime.now());
        compra.setValorTotal(BigDecimal.ZERO);

        compra = repoCompra.save(compra);

        BigDecimal valorTotalCompra = BigDecimal.ZERO;

        for (CompraDTO.ItemCompraDTO itemDTO : compraDTO.getItens()) {
            Optional<Produto> produtoOptional = repoProduto.findById(itemDTO.getProdutoId());
            if (produtoOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Produto não encontrado. ID: " + itemDTO.getProdutoId());
            }

            Produto produto = produtoOptional.get();

            // Verificar se há quantidade suficiente em estoque
            if (produto.getQuantidade() < itemDTO.getQuantidade()) {
                return ResponseEntity.badRequest()
                        .body("Quantidade insuficiente em estoque para o produto: " + produto.getNomeProduto());
            }

            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setCompra(compra);
            itemCompra.setProduto(produto);
            itemCompra.setQuantidade(itemDTO.getQuantidade());
            repoItemCompra.save(itemCompra);

            // Atualizar quantidade em estoque
            produto.setQuantidade(produto.getQuantidade() - itemDTO.getQuantidade());
            repoProduto.save(produto);

            valorTotalCompra = valorTotalCompra
                    .add(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));
        }

        compra.setValorTotal(valorTotalCompra);
        repoCompra.save(compra);

        return ResponseEntity.status(HttpStatus.CREATED).body("Compra registrada com sucesso!");
    }

}