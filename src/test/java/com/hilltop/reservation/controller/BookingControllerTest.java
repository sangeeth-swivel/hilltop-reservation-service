package com.hilltop.reservation.controller;

import com.hilltop.reservation.domain.request.BookingRequestDto;
import com.hilltop.reservation.domain.response.ResponseWrapper;
import com.hilltop.reservation.enums.ErrorMessage;
import com.hilltop.reservation.enums.SuccessMessage;
import com.hilltop.reservation.exception.HillTopBookingApplicationException;
import com.hilltop.reservation.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Booking controller test
 * Unit tests for {@link  BookingController}
 */
class BookingControllerTest {

    private final String ADD_BOOKING_URI = "/api/v1/booking";
    private final BookingRequestDto bookingRequestDto = getBookingRequestDto();
    @Mock
    private BookingService bookingService;

    private MockMvc mockMvc;

    @Mock
    private BaseController baseController;


    @InjectMocks
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @BeforeEach
    void setUp() {
        openMocks(this);
        BookingController bookingController = new BookingController(bookingService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    /**
     * Unit tests for makeBooking() method.
     */
    @Test
    void Should_ReturnOk_When_BookingIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BOOKING_URI)
                        .content(bookingRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_MissingRequiredFields() throws Exception {
        bookingRequestDto.setUserId(null);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BOOKING_URI)
                        .content(bookingRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnBadRequest_When_InvalidDateFieldsAreGiven() throws Exception {
        bookingRequestDto.setCheckIn(2208969000000L);
        bookingRequestDto.setCheckOut(1893436200000L);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_BOOKING_URI)
                        .content(bookingRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INVALID_DATES.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_BookingIsFailedDueToInternalErrors() throws Exception {
        HillTopBookingApplicationException exception = new HillTopBookingApplicationException("Failed.");
        ResponseEntity<ResponseWrapper> expectedResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        // Mock the behavior of the baseController
        when(baseController.getInternalServerError()).thenReturn(expectedResponse);

        // Invoke the exception handler
        ResponseEntity<ResponseWrapper> actualResponse = globalControllerExceptionHandler.hillTopUserApplicationException(exception);

        // Verify the interactions and assertions
        verify(baseController, times(1)).getInternalServerError();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    }

    /**
     * This method is used to mock bookingRequestDto.
     *
     * @return bookingRequestDto
     */
    private BookingRequestDto getBookingRequestDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setUserId("uid-123");
        bookingRequestDto.setRoomId("rid-123");
        bookingRequestDto.setCustomerCount(4);
        bookingRequestDto.setAmount(1000);
        bookingRequestDto.setCheckIn(1893436200000L);
        bookingRequestDto.setCheckOut(2208969000000L);
        return bookingRequestDto;
    }
}