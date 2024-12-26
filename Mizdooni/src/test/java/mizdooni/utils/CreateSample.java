package mizdooni.utils;

import mizdooni.exceptions.BadPeopleNumber;
import mizdooni.exceptions.DuplicatedUsernameEmail;
import mizdooni.exceptions.UserNotFound;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.*;
import mizdooni.response.PagedList;
import mizdooni.service.ServiceUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mizdooni.response.PagedList;

public class CreateSample {

    public static Address createSampleAddress() {
        return new Address("country", "city", "street");
    }

    public static User createSampleUser() {
        Address address = createSampleAddress();
        return new User("username", "password", "email@mail.com", address,
                User.Role.client);
    }

    public static User createSampleUser(String username) {
        Address address = createSampleAddress();
        return new User(username, "password", "email@mail.com", address,
                User.Role.client);
    }

    public static User createSampleManager() {
        Address address = createSampleAddress();
        return new User("username", "password", "email@mail.com", address,
                User.Role.manager);
    }

    public static Restaurant createSampleRestaurant() {
        Address address = createSampleAddress();
        User manager = createSampleManager();
        return new Restaurant("name", manager, "type", LocalTime.now(), LocalTime.now(),
                "description", address, "image_link");
    }

    public static Table createSampleTable() {
        return new Table(0, 0, 0);
    }

    public static Table createSampleTable(int seatsNumber) {
        return new Table(0, 0, seatsNumber);
    }

    public static Rating createSampleRating() {
        return new Rating();
    }

    public static Rating createSampleRating(int overall) {
        Rating rating = new Rating();
        rating.overall = overall;
        return  rating;
    }

    public static Review createSampleReview(User user, String comment, int overallRating) {
        Rating rating = createSampleRating(overallRating);
        return new Review(user, rating, comment, LocalDateTime.now());
    }

    public static Review createSampleReview(User user) {
        Rating rating = createSampleRating();
        return new Review(user, rating, "comment", LocalDateTime.now());
    }

    public static Review createSampleReview(Rating rating) {
        return new Review(createSampleUser(), rating, "comment", LocalDateTime.now());
    }

    public static Review createSampleReview() {
        return new Review(createSampleUser(), createSampleRating(), "comment", LocalDateTime.now());
    }

    public static PagedList<Review> createSamplePageListOfReviews() {
        List<Review> reviews = List.of(createSampleReview(), createSampleReview());
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
        return List.of(createSampleReservation());
    }

    public static String createSampleDate() {
        return "2024-01-01";
    }

    public static String createSampleBadFormattedDate() {
        return "2024,01,01";
    }

    public static String createSampleDatetime() {
        return "2024-01-01 00:01";
    }

    public static String createSampleBadFormattedDatetime() {
        return "2024,01,01-00:01";
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

    public static LocalDateTime createSampleLocalDateTime() {
        return LocalDateTime.now();
    }

    public static Reservation createSampleReservation() {
        return new Reservation(createSampleUser(), createSampleRestaurant(), createSampleTable(), createSampleLocalDateTime());
    }

    public static double createSampleDoubleNumber() {
        return 4.7;
    }

    public static String createSampleNonBlankString() {
        return "SampleString";
    }

    public static String createSampleUsername() {
        return "username";
    }

    public static String createSampleInvalidUsername() {
        return "@Invalid Username";
    }

    public static String createSamplePassword() {
        return "password";
    }

    public static String createSampleEmail() {
        return "sample@email.com";
    }

    public static String createSampleInvalidEmail() {
        return "sampleInvalidEmail.com";
    }

    public static String createSampleRole() {
        return "client";
    }

    public static Map<String, String> createSampleMapAddress() {
        return Map.of("country", "country",
                     "city","city",
                     "street","street");
    }

    public static String createSampleBlankString() {
        return "   ";
    }

    public static String createSampleNotExistedRole() {
        return "aNotExistedRole";
    }

    public static DuplicatedUsernameEmail createSampleDuplicatedUsernameEmailException() {
        return new DuplicatedUsernameEmail();
    }

    public static List<Table> createSampleListOfTables() {
        return List.of(createSampleTable(1),
                        createSampleTable(2));
    }

    public static Map<String, String> createSampleTableMap() {
        return Map.of("seatsNumber", "1");
    }

    public static RestaurantSearchFilter createSampleRestaurantSearchFilter() {
        return new RestaurantSearchFilter();
    }

    public static List<Restaurant> createSampleListOfRestaurants() {
        return List.of(createSampleRestaurant());
    }

    public static  PagedList<Restaurant> createSamplePagedListOfRestaurants(int page) {
        return new PagedList<>(createSampleListOfRestaurants(), page, 2);
    }

    public static Map<String, Object> createSampleRestaurantMapping() {
        return new java.util.HashMap<>(Map.of(
                "name", "name",
                "type", "type",
                "startTime", "23:59",
                "endTime", "23:59",
                "description", "description",
                "address", createSampleMapAddress(),
                "image", "image-link"));
    }

    public static String createSampleRestaurantName() {
        return "SampleRestaurantName";
    }

    public static Set<String> createSampleSetOfRestaurantTypes() {
        return Set.of("type1", "type2", "type3");
    }

}
