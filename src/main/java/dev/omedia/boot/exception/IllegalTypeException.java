package dev.omedia.boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalTypeException extends RuntimeException{

    public IllegalTypeException(String message) {
        super(message);
    }

}
