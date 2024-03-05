package com.pagbet4.pagbet4.repositorio;

import com.pagbet4.pagbet4.entidades.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Long> {

    Usuario findByNome(String nome);

    Usuario findByEmail(String email);

    List<Usuario> findByAtivo(boolean b);

}
