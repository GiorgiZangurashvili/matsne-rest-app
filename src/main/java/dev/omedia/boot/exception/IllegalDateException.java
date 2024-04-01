package dev.omedia.boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalDateException extends RuntimeException{

    public IllegalDateException(String message) {
        super(message);
    }

}
