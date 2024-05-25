package com.pagbet4.pagbet4.servicos;

import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.encriptador.ServicoEncriptarSenha;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;

import jakarta.servlet.http.HttpSession;

@Service
public class ServicoUsuario {

    @Autowired
    private RepoUsuario repoUsuario;

    @Autowired
    private ServicoEncriptarSenha servicoEncriptarSenha;

    public ResponseEntity<String> login(Usuario usuario, HttpSession session) {
        Usuario usuarioEncontrado = repoUsuario.findByEmail(usuario.getEmail());
        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
        }
        if (!usuarioEncontrado.getAtivo()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário inativo");
        }
        if (isUsuarioLogado(usuarioEncontrado, session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário já está em uma sessão ativa");
        }
        if (servicoEncriptarSenha.verificarSenha(usuario.getSenha(), usuarioEncontrado.getSenha())) {
            session.setAttribute("usuario", usuarioEncontrado); // Armazena o usuário na sessão
            return ResponseEntity.ok("Usuário logado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }
    }

    private boolean isUsuarioLogado(Usuario usuario, HttpSession session) {
        Enumeration<String> sessionAttributeNames = session.getAttributeNames();
        while (sessionAttributeNames.hasMoreElements()) {
            String attributeName = sessionAttributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            if (attributeValue instanceof Usuario && ((Usuario) attributeValue).getId().equals(usuario.getId())) {
                return true;
            }
        }
        return false;
    }

    public ResponseEntity<?> cadastrarUsuario(Usuario usuario) {
        List<Usuario> usuariosComMesmoEmail = repoUsuario.findAllByEmail(usuario.getEmail());
        if (!usuariosComMesmoEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um usuário cadastrado com esse email");
        }
        if (usuario.getSenha() != null || !usuario.getSenha().isEmpty() && usuario.getCpf() != null
                || String.valueOf(usuario.getCpf()).length() == 11 && usuario.getNome() != null
                || !usuario.getNome().isEmpty() && usuario.getEmail() != null
                || !usuario.getEmail().isEmpty() && usuario.getFuncao() != null || !usuario.getFuncao().isEmpty()) {
            String senhaEncoded = servicoEncriptarSenha.encriptarSenha(usuario.getSenha());
            usuario.setSenha(senhaEncoded);

            return ResponseEntity.ok(repoUsuario.save(usuario));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha não pode ser nula ou vazia");
        }
    }

    public ResponseEntity<?> ativarDesativarUsuario(String email) {

        Usuario usuarioEncontrado = repoUsuario.findByEmail(email);
        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        } else if (usuarioEncontrado.getAtivo() == false) {
            usuarioEncontrado.setAtivo(true);
            repoUsuario.save(usuarioEncontrado);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Usuário ativado");
        } else {
            usuarioEncontrado.setAtivo(false);
            repoUsuario.save(usuarioEncontrado);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Usuário desativado");
        }
    }

    public ResponseEntity<String> alterarSenha(Long id, Usuario updateSenhaUsuario) {
        Usuario usuarioEncontrado = repoUsuario.findById(id).orElse(null);
        String novaSenha = updateSenhaUsuario.getSenha();

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O ID não pode ser nulo");
        } else if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        } else if (novaSenha == null || novaSenha.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha não pode ser nula ou vazia");
        }

        usuarioEncontrado.setSenha(servicoEncriptarSenha.encriptarSenha(novaSenha));
        repoUsuario.save(usuarioEncontrado);

        return ResponseEntity.ok("Senha alterada com sucesso.");
    }

    public ResponseEntity<?> updateUsuario(String email, Usuario updateUsuario) {
        if (email == null || updateUsuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID ou Usuario não podem ser nulos");
        }
        Usuario existingUsuario = repoUsuario.findByEmail(email);
        if (existingUsuario != null && updateUsuario.getNome() != null && !updateUsuario.getNome().isEmpty()
                && updateUsuario.getCpf() != null && !String.valueOf(updateUsuario.getCpf()).isEmpty()
                && updateUsuario.getFuncao() != null && !updateUsuario.getFuncao().isEmpty()) {
            existingUsuario.setFuncao(updateUsuario.getFuncao());
            existingUsuario.setCpf(updateUsuario.getCpf());
            existingUsuario.setAtivo(updateUsuario.getAtivo());
            existingUsuario.setNome(updateUsuario.getNome());
            return ResponseEntity.ok(repoUsuario.save(existingUsuario));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos");
    }
}