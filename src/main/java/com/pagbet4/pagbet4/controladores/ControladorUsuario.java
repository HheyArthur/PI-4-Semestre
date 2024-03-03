package com.pagbet4.pagbet4.controladores;

import com.pagbet4.pagbet4.encriptador.ServicoEncriptarSenha;
import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
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
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        Usuario usuarioEncontrado = repoUsuario.findByNome(usuario.getNome());
        if (usuarioEncontrado != null && usuario.getAtivo() == true) {
            if (servicoEncriptarSenha.verificarSenha(usuario.getSenha(), usuarioEncontrado.getSenha())) {
                return ResponseEntity.ok("Usuário logado com sucesso");
            }
        }
        if (usuario.getAtivo() == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário inativo");

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");

    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> addUsuario(@RequestBody Usuario usuario) {
        if (usuario.getSenha() != null || !usuario.getSenha().isEmpty() && usuario.getCpf() != null
                || String.valueOf(usuario.getCpf()).length() == 11 && usuario.getNome() != null
                || !usuario.getNome().isEmpty() && usuario.getEmail() != null
                || !usuario.getEmail().isEmpty() && usuario.getAtivo() != null) {
            String senhaEncoded = servicoEncriptarSenha.encriptarSenha(usuario.getSenha());
            usuario.setSenha(senhaEncoded);

            return ResponseEntity.ok(repoUsuario.save(usuario));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha não pode ser nula ou vazia");
        }
    }

    @GetMapping("/getUser/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return repoUsuario.findById(id).orElse(null);
    }

    @PutMapping("/updateUser/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @PathVariable Long cpf, @PathVariable String nome, @RequestBody Usuario updateUsuario) {
        if (repoUsuario.existsById(id)) {
            updateUsuario.setId(id);
            updateUsuario.setCpf(cpf);
            updateUsuario.setNome(nome);
            return repoUsuario.save(updateUsuario);
        }
        return null;
    }

    @PutMapping("/desativaUsuario/{id}")
    public Usuario desativaUsuario(@PathVariable Long id, @RequestBody Usuario desativaUsuario) {
        if (repoUsuario.existsById(id)) {

            desativaUsuario.setAtivo(false);

            desativaUsuario.setId(id);
            desativaUsuario.setNome(desativaUsuario.getNome());
            desativaUsuario.setCpf(desativaUsuario.getCpf());
            desativaUsuario.setEmail(desativaUsuario.getEmail());
            desativaUsuario.setSenha(desativaUsuario.getSenha());
            desativaUsuario.setFuncao(desativaUsuario.getFuncao());

            return repoUsuario.save(desativaUsuario);
        }
        return null;
    }

    @PutMapping("/ativarUsuario/{id}")
    public Usuario ativarUsuario(@PathVariable Long id, @RequestBody Usuario ativaUsuario) {
        if (repoUsuario.existsById(id)) {

            ativaUsuario.setAtivo(true);

            ativaUsuario.setNome(ativaUsuario.getNome());
            ativaUsuario.setCpf(ativaUsuario.getCpf());
            ativaUsuario.setEmail(ativaUsuario.getEmail());
            ativaUsuario.setSenha(ativaUsuario.getSenha());
            ativaUsuario.setFuncao(ativaUsuario.getFuncao());

            return repoUsuario.save(ativaUsuario);
        }

        return null;
    }

    @DeleteMapping("/deleteUser/{id}")

    public void deletaUsuario(@PathVariable Long id) {
        repoUsuario.deleteById(id);
    }

    @DeleteMapping("/deleteAllSemSenha")
    public void deletaUsuarioSemSenha() {
        List<Usuario> usuarios = getAllUsuario();
        for (Usuario usuario : usuarios) {
            if (usuario != null && usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                Usuario usuarioDeletar = repoUsuario.findById(usuario.getId()).orElse(null);
                repoUsuario.delete(usuarioDeletar);
            }
        }
    }

}
