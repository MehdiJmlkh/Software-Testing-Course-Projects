package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;


    public Address create_sample_address() {
        return new Address("country", "city", "street");
    }

    public User create_sample_user() {
        Address address = create_sample_address();
        return new User("username", "password", "email@mail.com", address,
                User.Role.client);
    }

    public User create_sample_manager() {
        Address address = create_sample_address();
        return new User("username", "password", "email@mail.com", address,
                User.Role.manager);
    }

    public Restaurant create_sample_restaurant() {
        Address address = create_sample_address();
        User manager = create_sample_manager();
        return new Restaurant("name", manager, "type", LocalTime.now(), LocalTime.now(),
                "description", address, "image_link");
    }

    public Table create_sample_table() {
        return new Table(0, 0, 0);
    }

    public Reservation create_sample_reservation() {
        User user = create_sample_user();
        Restaurant restaurant = create_sample_restaurant();
        Table table = create_sample_table();
        return new Reservation(user, restaurant, table, LocalDateTime.now());
    }

    @BeforeEach
    public void setup() {
        user =  create_sample_user();
    }

    @Test
    public void add_new_reservation_added_to_the_reservations(){
        Reservation reservation = create_sample_reservation();
        user.addReservation(reservation);
        assertEquals(1, user.getReservations().size());
        assertEquals(reservation, user.getReservations().getFirst());
    }

    @Test
    public void add_new_reservation_increases_reservation_counter(){
        Reservation reservation1 = create_sample_reservation();
        Reservation reservation2 = create_sample_reservation();
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
        Reservation reservation = create_sample_reservation();
        user.addReservation(reservation);
        reservation.cancel();
        assertNull(user.getReservation(reservation.getReservationNumber()));
    }

    @Test
    public void get_existent_reservation_returns_reservation() {
        Reservation reservation = create_sample_reservation();
        user.addReservation(reservation);
        assertEquals(user.getReservation(reservation.getReservationNumber()), reservation);
    }
}
