package com.pagbet4.pagbet4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.encriptador.ServicoEncriptarSenha;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;
import com.pagbet4.pagbet4.servicos.ServicoUsuario;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class ServicoUsuarioTest {

    @Autowired
    private ServicoUsuario servicoUsuario;

    @MockBean
    private RepoUsuario repoUsuario;

    @MockBean
    private ServicoEncriptarSenha servicoEncriptarSenha;

    @Test
    public void login_ComCredenciaisValidas_DeveRetornarMensagemDeSucesso() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        // Define o ID do usuário no mock
        usuario.setId(1L);

        // Mock do repoUsuario
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);

        // Mock do servicoEncriptarSenha
        when(servicoEncriptarSenha.verificarSenha("senha123", usuario.getSenha())).thenReturn(true); // Verifique se a
                                                                                                     // senha está sendo
                                                                                                     // comparada
                                                                                                     // corretamente

        // Criando um mock de HttpSession válido
        HttpSession session = mock(HttpSession.class);

        // Configurando o mock para retornar um atributo válido
        when(session.getAttributeNames()).thenReturn(Collections.enumeration(Arrays.asList("usuario")));
        when(session.getAttribute("usuario")).thenReturn(usuario);

        ResponseEntity<String> response = servicoUsuario.login(usuario, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário logado com sucesso", response.getBody());
    }

    @Test
    public void login_ComEmailInvalido_DeveRetornarStatus401() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        HttpSession session = mock(HttpSession.class);

        ResponseEntity<String> response = servicoUsuario.login(usuario, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    public void login_ComSenhaInvalida_DeveRetornarStatus401() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);
        when(servicoEncriptarSenha.verificarSenha("senha123", usuario.getSenha())).thenReturn(false);

        HttpSession session = mock(HttpSession.class);

        ResponseEntity<String> response = servicoUsuario.login(usuario, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Senha incorreta", response.getBody());
    }

    @Test
    public void cadastrarUsuario_ComDadosValidos_DeveRetornarUsuarioCadastrado() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        usuario.setCpf(12345678901L);
        usuario.setFuncao("Administrador");

        when(repoUsuario.findAllByEmail("teste@email.com")).thenReturn(new ArrayList<>());
        when(servicoEncriptarSenha.encriptarSenha("senha123")).thenReturn("senhaEncriptada");
        when(repoUsuario.save(usuario)).thenReturn(usuario);

        ResponseEntity<?> response = servicoUsuario.cadastrarUsuario(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    public void cadastrarUsuario_ComEmailExistente_DeveRetornarStatus400() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        usuario.setCpf(12345678901L);
        usuario.setFuncao("Administrador");

        List<Usuario> usuariosComMesmoEmail = new ArrayList<>();
        usuariosComMesmoEmail.add(new Usuario());
        when(repoUsuario.findAllByEmail("teste@email.com")).thenReturn(usuariosComMesmoEmail);

        ResponseEntity<?> response = servicoUsuario.cadastrarUsuario(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Já existe um usuário cadastrado com esse email", response.getBody());
    }

    @Test
    public void ativarDesativarUsuario_ComEmailValido_DeveRetornarStatus202() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setAtivo(false);

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);
        when(repoUsuario.save(usuario)).thenReturn(usuario);

        ResponseEntity<?> response = servicoUsuario.ativarDesativarUsuario("teste@email.com");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Usuário ativado", response.getBody());
    }

    @Test
    public void ativarDesativarUsuario_ComEmailInvalido_DeveRetornarStatus404() {
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        ResponseEntity<?> response = servicoUsuario.ativarDesativarUsuario("teste@email.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    public void alterarSenha_ComIdValido_DeveRetornarMensagemDeSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("senha123");

        when(repoUsuario.findById(1L)).thenReturn(Optional.of(usuario));
        when(servicoEncriptarSenha.encriptarSenha("novaSenha123")).thenReturn("novaSenhaEncriptada");

        Usuario updateSenhaUsuario = new Usuario();
        updateSenhaUsuario.setSenha("novaSenha123");

        ResponseEntity<String> response = servicoUsuario.alterarSenha(1L, updateSenhaUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha alterada com sucesso.", response.getBody());
    }

    @Test
    public void alterarSenha_ComIdInvalido_DeveRetornarStatus404() {
        when(repoUsuario.findById(1L)).thenReturn(Optional.empty());

        Usuario updateSenhaUsuario = new Usuario();
        updateSenhaUsuario.setSenha("novaSenha123");

        ResponseEntity<String> response = servicoUsuario.alterarSenha(1L, updateSenhaUsuario);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    public void alterarSenha_ComSenhaNulaOuVazia_DeveRetornarStatus400() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("senha123");

        when(repoUsuario.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario updateSenhaUsuario = new Usuario();
        updateSenhaUsuario.setSenha("");

        ResponseEntity<String> response = servicoUsuario.alterarSenha(1L, updateSenhaUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("A senha não pode ser nula ou vazia", response.getBody());
    }

    @Test
    public void updateUsuario_ComDadosValidos_DeveRetornarUsuarioAtualizado() {
        Usuario existingUsuario = new Usuario();
        existingUsuario.setEmail("teste@email.com");
        existingUsuario.setNome("Teste");
        existingUsuario.setCpf(12345678901L);
        existingUsuario.setFuncao("Administrador");

        Usuario updateUsuario = new Usuario();
        updateUsuario.setNome("Novo Teste");
        updateUsuario.setCpf(98765432101L);
        updateUsuario.setFuncao("Gerente");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(existingUsuario);
        when(repoUsuario.save(existingUsuario)).thenReturn(updateUsuario);

        ResponseEntity<?> response = servicoUsuario.updateUsuario("teste@email.com", updateUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateUsuario, response.getBody());
    }

    @Test
    public void updateUsuario_ComEmailInvalido_DeveRetornarStatus404() {
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        Usuario updateUsuario = new Usuario();
        updateUsuario.setNome("Novo Teste");
        updateUsuario.setCpf(98765432101L);
        updateUsuario.setFuncao("Gerente");

        ResponseEntity<?> response = servicoUsuario.updateUsuario("teste@email.com", updateUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID ou Usuario não podem ser nulos", response.getBody());
    }

    @Test
    public void updateUsuario_ComDadosInvalidos_DeveRetornarStatus400() {
        Usuario existingUsuario = new Usuario();
        existingUsuario.setEmail("teste@email.com");
        existingUsuario.setNome("Teste");
        existingUsuario.setCpf(12345678901L);
        existingUsuario.setFuncao("Administrador");

        Usuario updateUsuario = new Usuario();
        updateUsuario.setNome("");
        updateUsuario.setCpf(null);
        updateUsuario.setFuncao("");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(existingUsuario);

        ResponseEntity<?> response = servicoUsuario.updateUsuario("teste@email.com", updateUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
    }

    @Test
    public void updateUsuario_ComIdNulo_DeveRetornarStatus400() {
        Usuario updateUsuario = new Usuario();
        updateUsuario.setNome("Novo Teste");
        updateUsuario.setCpf(98765432101L);
        updateUsuario.setFuncao("Gerente");

        ResponseEntity<?> response = servicoUsuario.updateUsuario(null, updateUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID ou Usuario não podem ser nulos", response.getBody());
    }
}