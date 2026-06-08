package com.nexora.auth.exception;

import com.nexora.auth.exception.roles.EmptyRoleList;
import com.nexora.auth.exception.roles.RoleAlreadyAssociated;
import com.nexora.auth.exception.roles.RoleAlreadyExist;
import com.nexora.auth.exception.roles.RoleNotFound;
import com.nexora.auth.exception.token.IncorrectUserNameOrPasswordException;
import com.nexora.auth.exception.token.RefreshTokenExpired;
import com.nexora.auth.exception.token.RefreshTokenNotFound;
import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.exception.users.EmptyUserList;
import com.nexora.auth.exception.users.PasswordException;
import com.nexora.auth.exception.users.UserNotFound;
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

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFound userNotFound) {
        ErrorResponse errorResponse = new ErrorResponse(userNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyUserList.class)
    public ResponseEntity<ErrorResponse> handleEmptyUserListException(EmptyUserList emptyUserList) {
        ErrorResponse errorResponse = new ErrorResponse(emptyUserList.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorResponse> handlePasswordException(PasswordException passwordException) {
        ErrorResponse errorResponse = new ErrorResponse(passwordException.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RoleNotFound.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFound roleNotFound) {
        ErrorResponse errorResponse = new ErrorResponse(roleNotFound.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyRoleList.class)
    public ResponseEntity<ErrorResponse> handleEmptyRoleListException(EmptyRoleList emptyRoleList) {
        ErrorResponse errorResponse = new ErrorResponse(emptyRoleList.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenGenerationException(TokenException tokenGenerationException) {
        ErrorResponse errorResponse = new ErrorResponse(tokenGenerationException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IncorrectUserNameOrPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPasswordException(IncorrectUserNameOrPasswordException incorrectUserNameOrPasswordException) {
        ErrorResponse errorResponse = new ErrorResponse(incorrectUserNameOrPasswordException.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RoleAlreadyAssociated.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyAssociatedException(RoleAlreadyAssociated roleAlreadyAssociated) {
        ErrorResponse errorResponse = new ErrorResponse(roleAlreadyAssociated.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyExistException(RoleAlreadyExist roleAlreadyExist) {
        ErrorResponse errorResponse = new ErrorResponse(roleAlreadyExist.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenNotFound.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenNotFound(RefreshTokenNotFound refreshTokenNotFound) {
        ErrorResponse errorResponse = new ErrorResponse(refreshTokenNotFound.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RefreshTokenExpired.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenExpired(RefreshTokenExpired refreshTokenExpired) {
        ErrorResponse errorResponse = new ErrorResponse(refreshTokenExpired.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
