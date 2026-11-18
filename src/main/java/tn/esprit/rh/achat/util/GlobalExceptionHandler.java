package tn.esprit.rh.achat.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 : Requête invalide
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                cleanPath(request)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 404 : Ressource introuvable
    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "La ressource demandée est introuvable.",
                cleanPath(request)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 403 : Accès interdit
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Accès refusé : vous ne disposez pas des privilèges nécessaires.",
                cleanPath(request)
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 500 : Erreur interne (ne jamais exposer la stack trace)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Une erreur interne s'est produite. Veuillez contacter le support.",
                cleanPath(request)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String cleanPath(WebRequest request) {
        String description = request.getDescription(false);
        if (description != null && description.startsWith("uri=")) {
            return description.substring(4);
        }
        return description;
    }
}

