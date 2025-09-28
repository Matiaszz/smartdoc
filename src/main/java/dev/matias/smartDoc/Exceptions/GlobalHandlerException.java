package dev.matias.smartDoc.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex){
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File exceeds the maximum allowed size of 10MB limit. Please upload a smaller file.");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        String errorMsg = "Invalid argument: " + ex.toString() +
                ".\nPlease verify that all provided parameters are correct and try again.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMsg);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String errorMsg = "Username not found, please ensure that this user exist.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMsg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMsg = "Please, ensure your body have the correct fields.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMsg);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ex.getMessage());
    }


}
