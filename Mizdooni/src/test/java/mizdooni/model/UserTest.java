package mizdooni.model;

import mizdooni.utils.CreateSample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.utils.CreateSample.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;


    @BeforeEach
    public void setup() {
        user =  createSampleUser();
    }

    @Test
    public void add_new_reservation_added_to_the_reservations(){
        Reservation reservation = CreateSample.createSampleReservation();
        user.addReservation(reservation);
        assertEquals(1, user.getReservations().size());
        assertEquals(reservation, user.getReservations().getFirst());
    }

    @Test
    public void add_new_reservation_increases_reservation_counter(){
        Reservation reservation1 = CreateSample.createSampleReservation();
        Reservation reservation2 = CreateSample.createSampleReservation();
        user.addReservation(reservation1);
        user.addReservation(reservation2);
        assertEquals(1, reservation2.getReservationNumber() - reservation1.getReservationNumber());
    }

    @Test
    public void get_non_existent_reservation_returns_null() {
        assertNull(user.getReservation(1));
    }

    @Test
    public void get_cancelled_reservation_returns_null() {
        Reservation reservation = CreateSample.createSampleReservation();
        user.addReservation(reservation);
        reservation.cancel();
        assertNull(user.getReservation(reservation.getReservationNumber()));
    }

    @Test
    public void get_existent_reservation_returns_reservation() {
        Reservation reservation = CreateSample.createSampleReservation();
        user.addReservation(reservation);
        assertEquals(user.getReservation(reservation.getReservationNumber()), reservation);
    }

    @Test
    public void check_reserved_for_a_non_reserved_restaurant_returns_false() {
        assertFalse(user.checkReserved(createSampleRestaurant()));
    }

    @Test
    public void check_reserved_for_a_cancelled_reservation_returns_false() {
        Reservation reservation = CreateSample.createSampleReservation();
        user.addReservation(reservation);
        reservation.cancel();
        assertFalse(user.checkReserved(reservation.getRestaurant()));
    }

    @Test
    public void check_reserved_for_a_reserved_restaurant_returns_true() {
        Reservation reservation = CreateSample.createSampleReservation();
        user.addReservation(reservation);
        assertTrue(user.checkReserved(reservation.getRestaurant()));
    }
}
