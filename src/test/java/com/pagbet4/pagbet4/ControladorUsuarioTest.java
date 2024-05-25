package com.pagbet4.pagbet4;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagbet4.pagbet4.controladores.ControladorUsuario;
import com.pagbet4.pagbet4.entidades.Usuario;
import com.pagbet4.pagbet4.encriptador.ServicoEncriptarSenha;
import com.pagbet4.pagbet4.repositorio.RepoUsuario;
import com.pagbet4.pagbet4.servicos.ServicoUsuario;


@WebMvcTest(ControladorUsuario.class)
public class ControladorUsuarioTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepoUsuario repoUsuario;

    @MockBean
    private ServicoEncriptarSenha servicoEncriptarSenha;

    @MockBean
    private ServicoUsuario servicoUsuario;

    @Test
    public void getAllUsuario_DeveRetornarStatus200() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    public void login_ComCredenciaisValidas_DeveRetornarStatus200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);
        when(servicoEncriptarSenha.verificarSenha("senha123", usuario.getSenha())).thenReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void login_ComEmailInvalido_DeveRetornarStatus401() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void login_ComSenhaInvalida_DeveRetornarStatus401() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);
        when(servicoEncriptarSenha.verificarSenha("senha123", usuario.getSenha())).thenReturn(false);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void addUsuario_ComDadosValidos_DeveRetornarStatus200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        usuario.setCpf(12345678901L);
        usuario.setFuncao("Administrador");

        when(repoUsuario.findAllByEmail("teste@email.com")).thenReturn(new ArrayList<>());
        when(servicoEncriptarSenha.encriptarSenha("senha123")).thenReturn("senhaEncriptada");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(post("/usuarios/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void addUsuario_ComEmailExistente_DeveRetornarStatus400() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        usuario.setCpf(12345678901L);
        usuario.setFuncao("Administrador");

        List<Usuario> usuariosComMesmoEmail = new ArrayList<>();
        usuariosComMesmoEmail.add(new Usuario());
        when(repoUsuario.findAllByEmail("teste@email.com")).thenReturn(usuariosComMesmoEmail);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(post("/usuarios/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsuarioByEmail_ComEmailValido_DeveRetornarStatus200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/getUser/teste@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUsuarioByEmail_ComEmailInvalido_DeveRetornarStatus404() throws Exception {
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        mockMvc.perform(get("/usuarios/getUser/teste@email.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void ativarDesativarUsuario_ComEmailValido_DeveRetornarStatus202() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setAtivo(false);
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/desativarAtivarUsuario/teste@email.com"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void ativarDesativarUsuario_ComEmailInvalido_DeveRetornarStatus404() throws Exception {
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        mockMvc.perform(put("/usuarios/desativarAtivarUsuario/teste@email.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUsuariosInfo_DeveRetornarStatus200() throws Exception {
        mockMvc.perform(get("/usuarios/usuariosInfo"))
                .andExpect(status().isOk());
    }

    @Test
    public void getFuncao_ComNomeValido_DeveRetornarStatus200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setFuncao("Administrador");
        when(repoUsuario.findByNome("Teste")).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/funcao/Teste"))
                .andExpect(status().isOk());
    }

    @Test
    public void getFuncao_ComNomeInvalido_DeveRetornarStatus404() throws Exception {
        when(repoUsuario.findByNome("Teste")).thenReturn(null);

        mockMvc.perform(get("/usuarios/funcao/Teste"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void alterarSenha_ComIdValido_DeveRetornarStatus200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("senha123");

        when(repoUsuario.findById(1L)).thenReturn(Optional.of(usuario));
        when(servicoEncriptarSenha.encriptarSenha("novaSenha123")).thenReturn("novaSenhaEncriptada");

        Usuario updateSenhaUsuario = new Usuario();
        updateSenhaUsuario.setSenha("novaSenha123");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(updateSenhaUsuario);

        mockMvc.perform(put("/usuarios/alterarSenha/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void alterarSenha_ComIdInvalido_DeveRetornarStatus404() throws Exception {
        when(repoUsuario.findById(1L)).thenReturn(Optional.empty());

        Usuario updateSenhaUsuario = new Usuario();
        updateSenhaUsuario.setSenha("novaSenha123");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(updateSenhaUsuario);

        mockMvc.perform(put("/usuarios/alterarSenha/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUsuario_ComDadosValidos_DeveRetornarStatus200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setNome("Teste");
        usuario.setCpf(12345678901L);
        usuario.setFuncao("Administrador");

        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);

        Usuario updateUsuario = new Usuario();
        updateUsuario.setNome("Novo Teste");
        updateUsuario.setCpf(98765432101L);
        updateUsuario.setFuncao("Gerente");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(updateUsuario);

        mockMvc.perform(put("/usuarios/updateUser/teste@email.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUsuario_ComEmailInvalido_DeveRetornarStatus404() throws Exception {
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(null);

        Usuario updateUsuario = new Usuario();
        updateUsuario.setNome("Novo Teste");
        updateUsuario.setCpf(98765432101L);
        updateUsuario.setFuncao("Gerente");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(updateUsuario);

        mockMvc.perform(put("/usuarios/updateUser/teste@email.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletaUsuario_ComEmailValido_DeveExecutarSemErros() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        when(repoUsuario.findByEmail("teste@email.com")).thenReturn(usuario);

        mockMvc.perform(put("/usuarios/deleteUser/teste@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletaUsuario_ComIdValido_DeveExecutarSemErros() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(repoUsuario.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(put("/usuarios/deleteUserId/1"))
                .andExpect(status().isOk());
    }
}