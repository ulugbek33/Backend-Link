package uz.pdp.backendlink.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityAlreadyException extends RuntimeException {

    private final HttpStatus status;

    public EntityAlreadyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
