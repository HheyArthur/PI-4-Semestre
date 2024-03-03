package com.pagbet4.pagbet4.encriptador;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicoEncriptarSenha {

    private final BCryptPasswordEncoder encoderSenha;

    public ServicoEncriptarSenha(){
        this.encoderSenha = new BCryptPasswordEncoder();
    }
    
    public String encriptarSenha(String senha){
        return encoderSenha.encode(senha);
    }

    public boolean verificarSenha(String senha, String senhaEncriptada){
        return encoderSenha.matches(senha, senhaEncriptada);
    }

}
