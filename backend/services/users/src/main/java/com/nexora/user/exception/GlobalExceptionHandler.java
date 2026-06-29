package com.nexora.user.exception;

import com.nexora.user.exception.address.AddressNotFound;
import com.nexora.user.exception.preference.CurrencyNotFound;
import com.nexora.user.exception.preference.LanguageNotFound;
import com.nexora.user.exception.preference.UserPreferenceNotFound;
import com.nexora.user.exception.profile.EmptyUserProfileList;
import com.nexora.user.exception.profile.UserProfileNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserProfileNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserProfileNotFound(UserProfileNotFound userProfileNotFound) {
        return new ResponseEntity<>(new ErrorResponse(userProfileNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyUserProfileList.class)
    public ResponseEntity<ErrorResponse> handleEmptyUserProfileList(EmptyUserProfileList emptyUserProfileList) {
        return new ResponseEntity<>(new ErrorResponse(emptyUserProfileList.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressNotFound.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFound(AddressNotFound addressNotFound) {
        return new ResponseEntity<>(new ErrorResponse(addressNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserPreferenceNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserPreferenceNotFound(UserPreferenceNotFound userPreferenceNotFound) {
        return new ResponseEntity<>(new ErrorResponse(userPreferenceNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LanguageNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserLanguageNotFound(UserPreferenceNotFound languaNotFound) {
        return new ResponseEntity<>(new ErrorResponse(languaNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CurrencyNotFound.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotFound(CurrencyNotFound currencyNotFound) {
        return new ResponseEntity<>(new ErrorResponse(currencyNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            NoHandlerFoundException ex
    ) {

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


}
