package mizdooni.controllers;

import mizdooni.exceptions.*;
import mizdooni.model.Reservation;
import mizdooni.model.Restaurant;
import mizdooni.model.Review;
import mizdooni.response.PagedList;
import mizdooni.response.ResponseException;
import mizdooni.service.ReservationService;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;
import mizdooni.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.controllers.ControllerUtils.*;
import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CustomAssertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static mizdooni.utils.CreateSample.createSampleRatingMap;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationControllerTest {
    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReservationService reserveService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getRestaurantReservations_ValidArgs_ReturnsOkResponse() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
        int table = createSampleTableNumber();
        String date = createSampleDate();
        Restaurant restaurant = create_sample_restaurant();
        List<Reservation> reservations = createSampleListOfReservation();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        when(reserveService.getReservations(restaurant.getId(), table, LocalDate.parse(date, DATE_FORMATTER))).thenReturn(reservations);

        Response result = reservationController.getReservations(restaurant.getId(), table, date);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("restaurant table reservations", result.getMessage());
        assertEquals(reservations, result.getData());
    }

    @Test
    public void getRestaurantReservations_ReserveServiceThrowsException_ThrowsException() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
        int table = createSampleTableNumber();
        String date = createSampleDate();
        Restaurant restaurant = create_sample_restaurant();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        UserNotManager exception = createSampleUserNotManagerException();
        doThrow(exception).when(reserveService).getReservations(restaurant.getId(), table, LocalDate.parse(date, DATE_FORMATTER));

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.getReservations(restaurant.getId(), table, date);
        });


        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, exception);
        assertEquals(expected, result);
    }

    @Test
    public void getRestaurantReservations_BadFormattedDate_ThrowsException() {
        int table = createSampleTableNumber();
        String date = createSampleBadFormattedDate();
        Restaurant restaurant = create_sample_restaurant();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.getReservations(restaurant.getId(), table, date);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void getRestaurantReservations_NullDate_ReturnsOkResponse() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
        int table = createSampleTableNumber();
        Restaurant restaurant = create_sample_restaurant();
        List<Reservation> reservations = createSampleListOfReservation();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        when(reserveService.getReservations(restaurant.getId(), table, null)).thenReturn(reservations);

        Response result = reservationController.getReservations(restaurant.getId(), table, null);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("restaurant table reservations", result.getMessage());
        assertEquals(reservations, result.getData());
    }

    @Test
    public void getCustomerReservations_ValidArgs_ReturnsOkResponse() throws UserNotFound, UserNoAccess {
        int customerId = createSampleId();
        List<Reservation> reservations = createSampleListOfReservation();
        when(reserveService.getCustomerReservations(customerId)).thenReturn(reservations);

        Response result = reservationController.getCustomerReservations(customerId);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("user reservations", result.getMessage());
        assertEquals(reservations, result.getData());
    }

    @Test
    public void getCustomersReservations_ReserveServiceThrowsException_ThrowsException() throws UserNotFound, UserNoAccess {
        int customerId = createSampleId();

        UserNotFound exception = createSampleUserNotFoundException();
        doThrow(exception).when(reserveService).getCustomerReservations(customerId);

        ResponseException result = assertThrows(ResponseException.class, () -> {
           reservationController.getCustomerReservations(customerId);
        });

        assertEquals(exception.getMessage(), result.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(exception.getClass().getSimpleName(), result.getError());
    }

    @Test
    public void getAvailableTimes_ValidArgs_ReturnsOkResponse() throws DateTimeInThePast, RestaurantNotFound, BadPeopleNumber {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        String date = createSampleDate();
        List<LocalTime> localTimes = createSampleListOfLocalTime();

        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        when(reserveService.getAvailableTimes(restaurant.getId(), people, LocalDate.parse(date, DATE_FORMATTER))).thenReturn(localTimes);

        Response result = reservationController.getAvailableTimes(restaurant.getId(), people, date);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("available times", result.getMessage());
        assertEquals(localTimes, result.getData());
    }

    @Test
    public void getAvailableTimes_BadFormattedDate_ThrowsException() {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        String date = createSampleBadFormattedDate();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.getAvailableTimes(restaurant.getId(), people, date);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void getAvailableTimes_ReserveServiceThrowsException_ThrowsException() throws DateTimeInThePast, RestaurantNotFound, BadPeopleNumber {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        String date = createSampleDate();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        BadPeopleNumber exception = createSampleBadPeopleNumberException();
        doThrow(exception).when(reserveService).getAvailableTimes(restaurant.getId(), people, LocalDate.parse(date, DATE_FORMATTER));

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.getAvailableTimes(restaurant.getId(), people, date);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, exception);
        assertEquals(expected, result);
    }

    @Test
    public void addReservation_ValidArgs_ReturnsOkResponse() throws UserNotFound, DateTimeInThePast, TableNotFound, ReservationNotInOpenTimes, ManagerReservationNotAllowed, RestaurantNotFound, InvalidWorkingTime {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        String datetime = createSampleDatetime();
        Map<String, String> params = Map.of("people", Integer.toString(people), "datetime", datetime);
        Reservation reservation = createSampleReservation();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        when(reserveService.reserveTable(restaurant.getId(), people, LocalDateTime.parse(datetime, DATETIME_FORMATTER)))
                .thenReturn(reservation);

        Response result = reservationController.addReservation(restaurant.getId(), params);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(reservation, result.getData());
        assertEquals("reservation done", result.getMessage());
    }

    @Test
    public void addReservation_ParamsMissing_ThrowsException() {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        Map<String, String> params = Map.of("people", Integer.toString(people));
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.addReservation(restaurant.getId(), params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void addReservation_PeopleNotInteger_ThrowsException() {
        Restaurant restaurant = create_sample_restaurant();
        double people = createSampleDoubleNumber();
        String datetime = createSampleDatetime();
        Map<String, String> params = Map.of("people", Double.toString(people), "datetime", datetime);
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.addReservation(restaurant.getId(), params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void addReservation_BadFormattedDatetime_ThrowsException() {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        String datetime = createSampleBadFormattedDatetime();
        Map<String, String> params = Map.of("people", Integer.toString(people), "datetime", datetime);
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.addReservation(restaurant.getId(), params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void addReservation_ReserveServiceThrowsException_ThrowsException() throws UserNotFound, DateTimeInThePast, TableNotFound, ReservationNotInOpenTimes, ManagerReservationNotAllowed, RestaurantNotFound, InvalidWorkingTime {
        Restaurant restaurant = create_sample_restaurant();
        int people = createSamplePositiveNumber();
        String datetime = createSampleDatetime();
        Map<String, String> params = Map.of("people", Integer.toString(people), "datetime", datetime);
        UserNotFound exception = createSampleUserNotFoundException();

        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        doThrow(exception).when(reserveService).reserveTable(restaurant.getId(), people,  LocalDateTime.parse(datetime, DATETIME_FORMATTER));

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.addReservation(restaurant.getId(), params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, exception);
        assertEquals(expected, result);
    }

    @Test
    public void cancelReservation_ValidArgs_CallsCancelReservationMethodOfReserveService() throws ReservationCannotBeCancelled, UserNotFound, ReservationNotFound {
        int reservationNumber = createSamplePositiveNumber();


        Response result = reservationController.cancelReservation(reservationNumber);
        verify(reserveService).cancelReservation(eq(reservationNumber));
    }

    @Test
    public void cancelReservation_ValidArgs_ReturnsOkResponse() throws ReservationCannotBeCancelled, UserNotFound, ReservationNotFound {
        int reservationNumber = createSamplePositiveNumber();

        Response result = reservationController.cancelReservation(reservationNumber);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("reservation cancelled", result.getMessage());
    }

    @Test
    public void cancelReservation_ReserveServiceThrowsException_ThrowsException() throws ReservationCannotBeCancelled, UserNotFound, ReservationNotFound {
        int reservationNumber = createSamplePositiveNumber();
        UserNotFound exception = createSampleUserNotFoundException();

        doThrow(exception).when(reserveService).cancelReservation(reservationNumber);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.cancelReservation(reservationNumber);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, exception);
        assertEquals(expected, result);
    }
}
