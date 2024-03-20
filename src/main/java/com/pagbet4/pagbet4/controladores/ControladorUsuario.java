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

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class ControladorUsuario {

    private final RepoUsuario repoUsuario;

    @Autowired
    private ServicoEncriptarSenha servicoEncriptarSenha;

    public ControladorUsuario(RepoUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
    }

    @GetMapping
    public List<Usuario> getAllUsuario() {
        return repoUsuario.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario, HttpSession session) {
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

    @GetMapping("/checkSession")
    public ResponseEntity<List<Usuario>> checkSession(HttpSession session) {
        List<Usuario> usuariosLogados = new ArrayList<>();
        Enumeration<String> sessionAttributeNames = session.getAttributeNames();
        while (sessionAttributeNames.hasMoreElements()) {
            String attributeName = sessionAttributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            if (attributeValue instanceof Usuario) {
                usuariosLogados.add((Usuario) attributeValue);
            }
        }
        return ResponseEntity.ok(usuariosLogados);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> addUsuario(@RequestBody Usuario usuario) {
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

    // Retorna as informações do usuário com base no email
    @GetMapping("/getUser/{email}")
    public Usuario getUsuarioByEmail(@PathVariable @NonNull String email) {
        return repoUsuario.findByEmail(email);
    }

    // Desativa e ativa o usuário, com base no email
    @PutMapping("/desativarAtivarUsuario/{email}")
    public ResponseEntity<?> ativarDesativarUsuario(@PathVariable String email) {

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

    @PutMapping("/updateUser/{email}")
    public ResponseEntity<?> updateUsuario(@PathVariable @NonNull String email,
            @RequestBody @NonNull Usuario updateUsuario) {
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
