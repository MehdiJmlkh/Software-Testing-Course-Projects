package mizdooni.utils;

import mizdooni.exceptions.BadPeopleNumber;
import mizdooni.exceptions.UserNotFound;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.*;
import mizdooni.response.PagedList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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

    public static int createSamplePositiveNumber() {
        return 1;
    }

    public static int createSampleId() {
        return 1;
    }

    public static String createSampleComment() {
        return "comment";
    }

    public static Map<String, Number> createSampleRatingMap() {
        return Map.of("food", 1, "service", 2, "ambiance", 3, "overall", 4);
    }

    public static UserNotFound createSampleUserNotFoundException() {return new UserNotFound();}

    public static UserNotManager createSampleUserNotManagerException() {return new UserNotManager();}

    public static List<Reservation> createSampleListOfReservation() {
        return List.of(create_sample_reservation());
    }

    public static String createSampleDate() {
        return "2024-01-01";
    }

    public static String createSampleBadFormattedDate() {
        return "2024,01,01";
    }

    public static int createSampleTableNumber() {
        return 1;
    }

    public static  List<LocalTime> createSampleListOfLocalTime() {
        return List.of(LocalTime.now());
    }

    public static BadPeopleNumber createSampleBadPeopleNumberException() {
        return new BadPeopleNumber();
    }
}
