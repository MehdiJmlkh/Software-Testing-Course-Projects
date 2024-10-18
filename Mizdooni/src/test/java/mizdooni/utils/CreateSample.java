package mizdooni.utils;

import mizdooni.model.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateSample {

    public static Address create_sample_address() {
        return new Address("country", "city", "street");
    }

    public static User create_sample_user() {
        Address address = create_sample_address();
        return new User("username", "password", "email@mail.com", address,
                User.Role.client);
    }

    public static User create_sample_manager() {
        Address address = create_sample_address();
        return new User("username", "password", "email@mail.com", address,
                User.Role.manager);
    }

    public static Restaurant create_sample_restaurant() {
        Address address = create_sample_address();
        User manager = create_sample_manager();
        return new Restaurant("name", manager, "type", LocalTime.now(), LocalTime.now(),
                "description", address, "image_link");
    }

    public static Table create_sample_table() {
        return new Table(0, 0, 0);
    }

    public static Reservation create_sample_reservation() {
        User user = create_sample_user();
        Restaurant restaurant = create_sample_restaurant();
        Table table = create_sample_table();
        return new Reservation(user, restaurant, table, LocalDateTime.now());
    }
}
