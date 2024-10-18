package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private Reservation reservation;


    public Address create_sample_address() {
        return new Address("country", "city", "street");
    }

    public User create_sample_user(Address address) {
        return new User("username", "password", "email@mail.com", address,
                User.Role.client);
    }

    public User create_sample_manager(Address address) {
        return new User("username", "password", "email@mail.com", address,
                User.Role.manager);
    }

    public Restaurant create_sample_restaurant(Address address, User manager) {
        return new Restaurant("name", manager, "type", LocalTime.now(), LocalTime.now(),
                "description", address, "image_link");
    }

    public Table create_sample_table() {
        return new Table(0, 0, 0);
    }

    public Reservation create_sample_reservation(User user, Restaurant restaurant, Table table) {
        return new Reservation(user, restaurant, table, LocalDateTime.now());
    }

    @BeforeEach
    public void setup() {
        Address address = create_sample_address();
        user =  create_sample_user(address);
        User manager = create_sample_manager(address);
        Restaurant restaurant = create_sample_restaurant(address, manager);
        Table table = create_sample_table();
        reservation = create_sample_reservation(user, restaurant, table);
    }

    @Test
    public void add_new_reservation_added_to_the_reservations(){
        user.addReservation(reservation);
        assertEquals(1, user.getReservations().size());
        assertEquals(reservation, user.getReservations().getFirst());
    }
}
