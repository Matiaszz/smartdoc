package dev.matias.smartDoc.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

}
