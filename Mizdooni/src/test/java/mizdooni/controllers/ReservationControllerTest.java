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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
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
    public void getReservations_ValidArgs_ReturnsOkResponse() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
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
    public void getReservations_ReserveServiceThrowsException_ThrowsException() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
        int table = createSampleTableNumber();
        String date = createSampleDate();
        Restaurant restaurant = create_sample_restaurant();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        UserNotManager exception = createSampleUserNotManagerException();
        doThrow(exception).when(reserveService).getReservations(restaurant.getId(), table, LocalDate.parse(date, DATE_FORMATTER));

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reservationController.getReservations(restaurant.getId(), table, date);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(exception.getMessage(), result.getMessage());
        assertEquals(exception.getClass().getSimpleName(), result.getError());
    }

    @Test
    public void getReservations_BadFormattedDate_ThrowsException() {
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
    public void getReservations_NullDate_ReturnsOkResponse() throws UserNotManager, TableNotFound, InvalidManagerRestaurant, RestaurantNotFound {
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
}
