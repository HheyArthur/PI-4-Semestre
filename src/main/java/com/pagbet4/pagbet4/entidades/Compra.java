package com.pagbet4.pagbet4.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCompra> produtos;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @SuppressWarnings("deprecation")
    @NotNull
    private LocalDateTime dataCompra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemCompra> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemCompra> produtos) {
        this.produtos = produtos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public void addProduto(ItemCompra itemCompra) {
        if (produtos == null) {
            produtos = new ArrayList<>();
        }
        produtos.add(itemCompra);
        itemCompra.setCompra(this);
    }

}