package com.hilltop.reservation.controller;

import com.hilltop.reservation.domain.request.BookingRequestDto;
import com.hilltop.reservation.domain.response.ResponseWrapper;
import com.hilltop.reservation.enums.ErrorMessage;
import com.hilltop.reservation.enums.SuccessMessage;
import com.hilltop.reservation.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Booking controller
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/booking")
public class BookingController extends BaseController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * This method is used to make bookings.
     *
     * @param bookingRequestDto bookingRequestDto
     * @return success/ error response.
     */
    @PostMapping("")
    public ResponseEntity<ResponseWrapper> makeBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        if (!bookingRequestDto.isRequiredFieldsAvailable()) {
            log.debug("Required fields missing. data: {}", bookingRequestDto.toLogJson());
            return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
        }
        if (!bookingRequestDto.isValidDate()) {
            log.debug("Invalid date fields. data: {}", bookingRequestDto.toLogJson());
            return getBadRequestErrorResponse(ErrorMessage.INVALID_DATES);
        }
        bookingService.addBooking(bookingRequestDto);
        return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null, HttpStatus.CREATED);
    }
}
