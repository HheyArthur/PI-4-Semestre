package com.pagbet4.pagbet4;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pagbet4.pagbet4.encriptador.ServicoEncriptarSenha;

public class ServicoEncriptarSenhaTest {

    @Test
    public void encriptarSenha_DeveRetornarSenhaEncriptadaDiferenteDaOriginal() {
        ServicoEncriptarSenha encriptador = new ServicoEncriptarSenha();
        String senhaOriginal = "senha123";
        String senhaEncriptada = encriptador.encriptarSenha(senhaOriginal);

        assertFalse(senhaOriginal.equals(senhaEncriptada));
    }

    @Test
    public void verificarSenha_ComSenhaCorreta_DeveRetornarTrue() {
        ServicoEncriptarSenha encriptador = new ServicoEncriptarSenha();
        String senhaOriginal = "senha123";
        String senhaEncriptada = encriptador.encriptarSenha(senhaOriginal);

        assertTrue(encriptador.verificarSenha(senhaOriginal, senhaEncriptada));
    }

    @Test
    public void verificarSenha_ComSenhaIncorreta_DeveRetornarFalse() {
        ServicoEncriptarSenha encriptador = new ServicoEncriptarSenha();
        String senhaOriginal = "senha123";
        String senhaEncriptada = encriptador.encriptarSenha(senhaOriginal);

        assertFalse(encriptador.verificarSenha("senhaErrada", senhaEncriptada));
    }
}