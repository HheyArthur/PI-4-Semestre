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
import com.pagbet4.pagbet4.repositorio.RepoProduto;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;

@Service
public class ServicoCompra {

    @Autowired
    private RepoCompra repoCompra;

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

        // Validação: Garante que haja itens na compra
        if (compraDTO.getItens() == null || compraDTO.getItens().isEmpty()) {
            return ResponseEntity.badRequest().body("A compra deve conter pelo menos um item.");
        }

        BigDecimal valorTotalCalculado = BigDecimal.ZERO;

        Compra compra = new Compra();
        compra.setUsuario(usuarioOptional.get());
        compra.setDataCompra(LocalDateTime.now());
        compra.setValorTotal(compraDTO.getValorTotal());

        for (CompraDTO.ItemCompraDTO itemDTO : compraDTO.getItens()) {
            Optional<Produto> produtoOptional = repoProduto.findById(itemDTO.getProdutoId());
            if (produtoOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Produto não encontrado. ID: " + itemDTO.getProdutoId());
            }

            Produto produto = produtoOptional.get();

            // Validação: Quantidade válida
            if (itemDTO.getQuantidade() <= 0) {
                return ResponseEntity.badRequest().body("A quantidade do item deve ser maior que zero. Produto: " + produto.getNomeProduto());
            }

            // Verificar se há quantidade suficiente em estoque
            if (produto.getQuantidade() < itemDTO.getQuantidade()) {
                return ResponseEntity.badRequest().body("Quantidade insuficiente em estoque para o produto: " + produto.getNomeProduto());
            }

            // Calcula o valor total dos itens
            valorTotalCalculado = valorTotalCalculado.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setCompra(compra);
            itemCompra.setProduto(produto);
            itemCompra.setQuantidade(itemDTO.getQuantidade());

            compra.getItens().add(itemCompra);

            // Atualiza a quantidade em estoque, mas NÃO salva o produto ainda
            produto.setQuantidade(produto.getQuantidade() - itemDTO.getQuantidade());
        }

        // Validação do valor total
        if (compraDTO.getValorTotal().compareTo(valorTotalCalculado) != 0) {
            return ResponseEntity.badRequest().body("Valor total da compra inconsistente. Valor recebido: " + compraDTO.getValorTotal() + ", Valor calculado: " + valorTotalCalculado);
        }

        // Salva a compra com os itens
        repoCompra.save(compra);

        // Salva os produtos com a quantidade atualizada
        for (CompraDTO.ItemCompraDTO itemDTO : compraDTO.getItens()) {
            Produto produto = repoProduto.findById(itemDTO.getProdutoId()).get();
            repoProduto.save(produto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Compra registrada com sucesso!");
    }

    @Transactional
    public ResponseEntity<?> listarCompras() {
        return ResponseEntity.ok(repoCompra.findAll());
    }

}