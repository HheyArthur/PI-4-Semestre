package com.pagbet4.pagbet4.controladores;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagbet4.pagbet4.encriptador.ServicoEncriptarSenha;
import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.entidades.UsuarioInfo;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;
import com.pagbet4.pagbet4.servicos.ServicoUsuario;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class ControladorUsuario {

    private final RepoUsuario repoUsuario;
    private final ServicoEncriptarSenha servicoEncriptarSenha;
    private final ServicoUsuario servicoUsuario;

    @Autowired
    public ControladorUsuario(RepoUsuario repoUsuario, ServicoEncriptarSenha servicoEncriptarSenha,
            ServicoUsuario servicoUsuario) {
        this.repoUsuario = repoUsuario;
        this.servicoEncriptarSenha = servicoEncriptarSenha;
        this.servicoUsuario = servicoUsuario;
    }

    @GetMapping
    public List<Usuario> getAllUsuario() {
        return repoUsuario.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario, HttpSession session) {
        return servicoUsuario.login(usuario, session);
    }

    @GetMapping("/checkSession")
    public List<Usuario> checkSession(HttpSession session) {
        List<Usuario> usuariosLogados = new ArrayList<>();
        Enumeration<String> sessionAttributeNames = session.getAttributeNames();
        while (sessionAttributeNames.hasMoreElements()) {
            String attributeName = sessionAttributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            if (attributeValue instanceof Usuario) {
                usuariosLogados.add((Usuario) attributeValue);
            }
        }
        return usuariosLogados;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        return servicoUsuario.logout(session);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> addUsuario(@RequestBody Usuario usuario) {
        return servicoUsuario.cadastrarUsuario(usuario);
    }

    // Retorna as informações do usuário com base no email
    @GetMapping("/getUser/{email}")
    public Usuario getUsuarioByEmail(@PathVariable @NonNull String email) {
        return repoUsuario.findByEmail(email);
    }

    @GetMapping("/pesquisa/{nome}")
    public List<Usuario> pesquisaUsuarioNome(@PathVariable String nome) {
        List<Usuario> usuarios = repoUsuario.findAllByNomeContaining(nome);
        return usuarios;
    }

    // Desativa e ativa o usuário, com base no email
    @PutMapping("/desativarAtivarUsuario/{email}")
    public ResponseEntity<?> ativarDesativarUsuario(@PathVariable String email) {
        return servicoUsuario.ativarDesativarUsuario(email);
    }

    @GetMapping("/usuariosInfo")
    public ResponseEntity<List<UsuarioInfo>> getUsuariosInfo() {
        try {
            List<Usuario> usuarios = repoUsuario.findAll();
            List<UsuarioInfo> usuariosInfo = new ArrayList<>();

            for (Usuario usuario : usuarios) {
                UsuarioInfo usuarioInfo = new UsuarioInfo();
                usuarioInfo.setNome(usuario.getNome());
                usuarioInfo.setEmail(usuario.getEmail());
                usuarioInfo.setFuncao(usuario.getFuncao());
                usuarioInfo.setAtivo(usuario.getAtivo());
                usuariosInfo.add(usuarioInfo);
            }

            return ResponseEntity.ok(usuariosInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Retorna a função do usuário, utilizar para redirecionar para a página correta
    @GetMapping("/funcao/{nome}")
    public ResponseEntity<?> getFuncao(@PathVariable String nome) {
        Usuario usuarioEncontrado = repoUsuario.findByNome(nome);
        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        return ResponseEntity.ok(usuarioEncontrado.getFuncao());

    }

    @SuppressWarnings("null")
    @PutMapping("/alterarSenha/{id}")
    public ResponseEntity<String> alterarSenha(@PathVariable @NonNull Long id,
            @RequestBody @NonNull Usuario updateSenhaUsuario) {
        return servicoUsuario.alterarSenha(id, updateSenhaUsuario);
    }

    @PutMapping("/updateUser/{email}")
    public ResponseEntity<?> updateUsuario(@PathVariable @NonNull String email,
            @RequestBody @NonNull Usuario updateUsuario) {
        return servicoUsuario.updateUsuario(email, updateUsuario);
    }

    @SuppressWarnings("null")
    @DeleteMapping("/deleteUser/{email}")
    public void deletaUsuario(@PathVariable String email) {
        Usuario usuarioDeletar = repoUsuario.findByEmail(email);
        repoUsuario.delete(usuarioDeletar);
    }

    @SuppressWarnings("null")
    @DeleteMapping("/deleteUserId/{id}")
    public void deletaUsuario(@PathVariable Long id) {
        Usuario usuarioDeletar = repoUsuario.findById(id).orElse(null);
        repoUsuario.delete(usuarioDeletar);
    }

    @SuppressWarnings("null")
    @DeleteMapping("/deleteAllSemSenha")
    public void deletaUsuarioSemSenha() {
        List<Usuario> usuarios = getAllUsuario();
        usuarios.stream()
                .filter(usuario -> usuario != null && (usuario.getSenha() == null || usuario.getSenha().isEmpty()))
                .forEach(usuario -> {
                    Usuario usuarioDeletar = repoUsuario.findById(usuario.getId()).orElse(null);
                    repoUsuario.delete(usuarioDeletar);
                });
    }

}