package com.pagbet4.pagbet4.controladores;

import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {

    private final RepoUsuario repoUsuario;

    @Autowired
    public ControladorUsuario(RepoUsuario repoUsuario){
        this.repoUsuario = repoUsuario;
    }

    @GetMapping
    public List<Usuario> getAllUsuario(){
        return repoUsuario.findAll();
    }

    @PostMapping
    public Usuario addUsuario(@RequestBody Usuario usuario){
        return repoUsuario.save(usuario);
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id){
        return repoUsuario.findById(id).orElse(null);
    }

    @PutMapping("/atualizausuario/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario updateUsuario){
        if (repoUsuario.existsById(id)){
            updateUsuario.setId(id);
            return repoUsuario.save(updateUsuario);
        }
        return null;
    }

    @DeleteMapping("/deletausuario/{id}")
    public void deletaUsuario(@PathVariable Long id){
        repoUsuario.deleteById(id);
    }
}
