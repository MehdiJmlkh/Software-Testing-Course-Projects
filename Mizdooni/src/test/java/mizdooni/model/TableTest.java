package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TableTest {
    private Table table;

    public Table create_sample_table() {
        return new Table(1, 1, 1);
    }

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

    public Reservation create_sample_reservation() {
        User user = create_sample_user();
        Restaurant restaurant = create_sample_restaurant();
        Table table = create_sample_table();
        return new Reservation(user, restaurant, table, LocalDateTime.now());
    }

    @BeforeEach
    public void setup() {
        table = create_sample_table();
    }

    @Test
    public void add_new_reservation_added_to_the_reservations() {
        Reservation reservation = create_sample_reservation();
        table.addReservation(reservation);
        assertEquals(1, table.getReservations().size());
        assertEquals(reservation, table.getReservations().getFirst());
    }

    @Test
    public void check_reserved_for_a_non_reserved_datetime_returns_false() {
        assertFalse(table.isReserved(LocalDateTime.now()));
    }

    @Test
    public void check_reserved_for_a_reserved_datetime_returns_true() {
        Reservation reservation = create_sample_reservation();
        table.addReservation(reservation);
        assertTrue(table.isReserved(reservation.getDateTime()));
    }

    @Test
    public void check_reserved_for_a_cancelled_datetime_returns_false() {
        Reservation reservation = create_sample_reservation();
        table.addReservation(reservation);
        reservation.cancel();
        assertFalse(table.isReserved(reservation.getDateTime()));
    }

}
