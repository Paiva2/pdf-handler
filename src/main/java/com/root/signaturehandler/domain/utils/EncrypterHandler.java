package com.root.signaturehandler.domain.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncrypterHandler {
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(6);

    public String encrypt(String toEncrypt) {
        return this.bcrypt.encode(toEncrypt);
    }
}
