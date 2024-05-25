package com.pagbet4.pagbet4.entidades;

import java.math.BigDecimal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeProduto;
    private String descricao;

    private String imagemPrincipal = "..\\img\\imagem_indisponivel.png";// imagem indisponivel por padrão
    
    @JsonManagedReference
    @OneToMany(mappedBy = "produto", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Imagem> imagens;

    private long codigo = 1000 + (Math.round(Math.random() * 9000));

    private long quantidade;

    private long avaliacao = 0;
    private long qtdAvaliacoes = 0;

    private boolean ativo = true;

    @Column(name = "preco", precision = 10, scale = 2)
    private BigDecimal preco;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    public long getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(long avaliacao) {
        this.avaliacao = avaliacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public long getQuatidadeAvaliacoes() {
        return qtdAvaliacoes;
    }

    public void setQuatidadeAvaliacoes(long qtdAvaliacoes) {
        this.qtdAvaliacoes = qtdAvaliacoes;
    }

    public String getImagemPrincipal() {
        return imagemPrincipal != null ? imagemPrincipal : "";
    }

    public void setImagemPrincipal(String imagem) {
        this.imagemPrincipal = imagem;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }

}
