package com.pagbet4.pagbet4.repositorio;

import com.pagbet4.pagbet4.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RepoUsuario extends JpaRepository<Usuario, Long> {

    Usuario findByNome(String nome);

}
