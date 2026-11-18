package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.rh.achat.entities.Operateur;

import static org.junit.jupiter.api.Assertions.*;

class OperateurPasswordSecurityTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("SEC-01 : Le mot de passe ne doit jamais être présent dans le JSON sérialisé (réponse sortante)")
    void testPasswordExcludedFromSerialization() throws Exception {
        Operateur op = new Operateur();
        op.setIdOperateur(10L);
        op.setNom("Ben Salah");
        op.setPrenom("Sami");
        op.setPassword("SuperSecretPassword123!");

        String json = objectMapper.writeValueAsString(op);

        assertFalse(json.contains("SuperSecretPassword123!"), "Le mot de passe en clair ne doit pas apparaître dans le JSON !");
        assertFalse(json.contains("\"password\""), "Le champ password ne doit pas être présent dans le JSON de sortie !");
        assertTrue(json.contains("\"idOperateur\":10"));
        assertTrue(json.contains("\"nom\":\"Ben Salah\""));
    }

    @Test
    @DisplayName("SEC-01 : Le mot de passe doit pouvoir être reçu lors d'une désérialisation (requête entrante POST/PUT)")
    void testPasswordIncludedInDeserialization() throws Exception {
        String inputJson = "{\"idOperateur\":11,\"nom\":\"Trabelsi\",\"prenom\":\"Ali\",\"password\":\"MonMotDePasseEntrant456!\"}";

        Operateur op = objectMapper.readValue(inputJson, Operateur.class);

        assertNotNull(op);
        assertEquals(11L, op.getIdOperateur());
        assertEquals("MonMotDePasseEntrant456!", op.getPassword(), "Le mot de passe doit être correctement injecté dans l'entité.");
    }
}

