package br.edu.gustavoign.carometro.exception;

import java.io.Serial;

public class ServiceExc extends Exception {

    public ServiceExc(String message) {
        super(message);
    }


    @Serial
    private static final long serialVersionUID = 1L;
}
