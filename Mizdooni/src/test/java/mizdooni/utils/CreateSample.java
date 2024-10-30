package mizdooni.utils;

import mizdooni.model.*;
import mizdooni.response.PagedList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static Table create_sample_table(int seatsNumber) {
        return new Table(0, 0, seatsNumber);
    }

    public static Reservation create_sample_reservation() {
        User user = create_sample_user();
        Restaurant restaurant = create_sample_restaurant();
        Table table = create_sample_table();
        return new Reservation(user, restaurant, table, LocalDateTime.now());
    }

    public static Rating create_sample_rating() {
        return new Rating();
    }

    public static Review create_sample_review(User user) {
        Rating rating = create_sample_rating();
        return new Review(user, rating, "comment", LocalDateTime.now());
    }

    public static Review create_sample_review(Rating rating) {
        return new Review(create_sample_user(), rating, "comment", LocalDateTime.now());
    }

    public static Review create_sample_review() {
        return new Review(create_sample_user(), create_sample_rating(), "comment", LocalDateTime.now());
    }

    public static PagedList<Review> createSamplePageListOfReviews() {
        List<Review> reviews = List.of(create_sample_review(), create_sample_review());
        return new PagedList<>(reviews, 1, 1);
    }

    public static int createSamplePageNumber() {
        return 1;
    }

    public static int createSampleId() {
        return 1;
    }

}
