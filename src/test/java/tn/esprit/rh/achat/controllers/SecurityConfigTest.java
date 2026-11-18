package tn.esprit.rh.achat.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.esprit.rh.achat.util.SecurityConfig;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    @DisplayName("SEC-02 : Le PasswordEncoder doit être BCrypt et hacher de manière sécurisée les mots de passe")
    void testBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        String rawPassword = "MonMotDePasse123";
        String encoded = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded);
        assertTrue(encoded.startsWith("$2a$") || encoded.startsWith("$2b$"), "Le hash doit être un hash BCrypt valide.");
        assertTrue(encoder.matches(rawPassword, encoded), "Le PasswordEncoder doit valider le mot de passe en clair contre le hash.");
        assertFalse(encoder.matches("MauvaisMotDePasse", encoded));
    }
}

