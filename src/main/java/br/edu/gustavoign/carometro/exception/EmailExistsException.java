package br.edu.gustavoign.carometro.exception;

import java.io.Serial;

public class EmailExistsException extends Exception {


    public EmailExistsException(String message) {
        super(message);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
