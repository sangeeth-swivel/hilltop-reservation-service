package com.hilltop.reservation.controller;

import com.hilltop.reservation.domain.response.ResponseWrapper;
import com.hilltop.reservation.exception.HillTopBookingApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final BaseController baseController;

    public GlobalControllerExceptionHandler(BaseController baseController) {
        this.baseController = baseController;
    }

    @ExceptionHandler(HillTopBookingApplicationException.class)
    public ResponseEntity<ResponseWrapper> hillTopUserApplicationException(HillTopBookingApplicationException exception) {
        log.debug("Internal Server Error. {}", exception.getMessage());
        return baseController.getInternalServerError();
    }

}
