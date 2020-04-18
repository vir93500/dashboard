package com.freecharge.dashboard.ExceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    CustomErrorResponse errors;

    @ExceptionHandler(NoServicesFound.class)
    public ResponseEntity<CustomErrorResponse> springHandleBadRequest() {
        errors.setError(ExceptionCodes.NO_SERVICES_FOUND.errMsg());
        errors.setStatus(ExceptionCodes.NO_SERVICES_FOUND.errCode());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoMethodFound.class)
    public ResponseEntity<CustomErrorResponse> NoMethodFoundBadRequest() {
        errors.setError(ExceptionCodes.NO_METHODS_FOUND.errMsg());
        errors.setStatus(ExceptionCodes.NO_METHODS_FOUND.errCode());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoTestClassesFound.class)
    public ResponseEntity<CustomErrorResponse> NoTestClassesFoundBadRequest() {
        errors.setError(ExceptionCodes.NO_CLASSES_FOUND.errMsg());
        errors.setStatus(ExceptionCodes.NO_CLASSES_FOUND.errCode());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassPercentageNotFound.class)
    public ResponseEntity<CustomErrorResponse> PassPercentageNotFoundBadRequest() {
        errors.setError(ExceptionCodes.NO_PASS_PERCENTAGE_FOUND.errMsg());
        errors.setStatus(ExceptionCodes.NO_PASS_PERCENTAGE_FOUND.errCode());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
