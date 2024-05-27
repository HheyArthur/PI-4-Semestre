package com.pagbet4.pagbet4.ProdutoPaginadoDTO;

import java.util.List;

import com.pagbet4.pagbet4.entidades.Produto;

public class ProdutoPaginadoDTO {
    private List<Produto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    // Construtor, getters e setters
    public ProdutoPaginadoDTO(List<Produto> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    // Getters
    public List<Produto> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    // Setters
    public void setContent(List<Produto> content) {
        this.content = content;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
