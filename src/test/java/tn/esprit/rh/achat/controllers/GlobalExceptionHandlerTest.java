package tn.esprit.rh.achat.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import tn.esprit.rh.achat.util.ErrorResponse;
import tn.esprit.rh.achat.util.GlobalExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("SEC-08 : Gestion des requêtes invalides (400 Bad Request)")
    void testHandleBadRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test-bad-request");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(
                new IllegalArgumentException("Paramètre invalide"),
                webRequest
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Paramètre invalide", response.getBody().getMessage());
    }

    @Test
    @DisplayName("SEC-08 : Gestion des erreurs internes sans fuite de stack trace (500 Internal Server Error)")
    void testHandleInternalServerError() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test-error");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ErrorResponse> response = handler.handleGlobalException(
                new RuntimeException("Crash interne de base de données"),
                webRequest
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        // Vérification de la non-exposition des détails techniques de l'exception
        assertFalse(response.getBody().getMessage().contains("Crash interne de base de données"));
    }
}

