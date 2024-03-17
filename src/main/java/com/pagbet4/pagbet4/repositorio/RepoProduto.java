package com.pagbet4.pagbet4.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagbet4.pagbet4.entidades.Produto;

public interface RepoProduto extends JpaRepository<Produto, Long>{
    
}
