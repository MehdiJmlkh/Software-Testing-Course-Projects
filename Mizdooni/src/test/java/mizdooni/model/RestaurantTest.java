package mizdooni.model;

import mizdooni.utils.CreateSample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CreateSample.createSampleTable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RestaurantTest {
    private Restaurant restaurant;

    @BeforeEach
    public void setup() {
        restaurant = createSampleRestaurant();
    }

    @Test
    public void add_new_table_added_to_the_tables(){
        Table table = CreateSample.createSampleTable();
        restaurant.addTable(table);
        assertEquals(1, restaurant.getTables().size());
        assertEquals(table, restaurant.getTables().getFirst());
    }

    @Test
    public void add_new_table_increases_table_number(){
        Table table1 = CreateSample.createSampleTable();
        Table table2 = CreateSample.createSampleTable();
        restaurant.addTable(table1);
        restaurant.addTable(table2);
        assertEquals(1, table2.getTableNumber() - table1.getTableNumber());
    }

    @Test
    public void get_existent_table_returns_the_table() {
        Table table = CreateSample.createSampleTable();
        restaurant.addTable(table);
        assertEquals(table, restaurant.getTable(table.getTableNumber()));
    }

    @Test
    public void get_non_existent_table_returns_null() {
        Table table = CreateSample.createSampleTable();
        restaurant.addTable(table);
        assertNull(restaurant.getTable(table.getTableNumber() + 1));
    }

    @Test
    public void add_first_review_of_a_user_added_to_the_reviews(){
        Review review = createSampleReview(createSampleUser());
        restaurant.addReview(review);
        assertEquals(1, restaurant.getReviews().size());
        assertEquals(review, restaurant.getReviews().getFirst());
    }

    @Test
    public void add_second_review_of_a_user_delete_the_previous_one_from_reviews(){
        User user = createSampleUser();
        Review review1 = createSampleReview(user);
        Review review2 = createSampleReview(user);
        restaurant.addReview(review1);
        restaurant.addReview(review2);
        assertEquals(1, restaurant.getReviews().size());
        assertEquals(review2, restaurant.getReviews().getFirst());
    }

    @Test
    public void get_average_rating_of_empty_list_of_reviews_return_zero() {
        Rating average = restaurant.getAverageRating();
        assertEquals(0, average.food);
        assertEquals(0, average.service);
        assertEquals(0, average.ambiance);
        assertEquals(0, average.overall);
    }

    @ParameterizedTest
    @MethodSource("rating_provider")
    public void get_average_rating_give_the_correct_result(List<List<Double>> rates, List<Double> expected) {
        for(List<Double> rate : rates) {
            Rating rating = new Rating();
            rating.food = rate.get(0);
            rating.service = rate.get(1);
            rating.ambiance = rate.get(2);
            rating.overall = rate.get(3);

            restaurant.addReview(createSampleReview(rating));
        }
        Rating average = restaurant.getAverageRating();
        assertEquals(expected.get(0), average.food);
        assertEquals(expected.get(1), average.service);
        assertEquals(expected.get(2), average.ambiance);
        assertEquals(expected.get(3), average.overall);
    }

    public static Stream<Arguments> rating_provider() {
        return Stream.of(
                arguments(List.of(
                        Arrays.asList(1., 2., 3., 4.),
                        Arrays.asList(1., 2., 3., 4.),
                        Arrays.asList(1., 2., 3., 4.)
                        ),
                Arrays.asList(1., 2., 3., 4.)
                ),
                arguments(List.of(
                                Arrays.asList(0., 2., 3., 4.1),
                                Arrays.asList(0., 5.5, 3.5, 4.5)
                        ),
                        Arrays.asList(0., 3.75, 3.25, 4.3)
                ),
                arguments(List.of(
                                Arrays.asList(1., 0., 3.3, 4.)
                        ),
                        Arrays.asList(1., 0., 3.3, 4.)
                )
        );
    }

    @Test
    public void get_max_seat_number_of_empty_list_of_tables_returns_zero() {
        assertEquals(0, restaurant.getMaxSeatsNumber());
    }

    @ParameterizedTest
    @MethodSource("seat_number_provider")
    public void get_max_seat_number_of_non_empty_list_of_tables_works(List<Integer> seatNumbers, int expected) {
        seatNumbers.forEach(n -> restaurant.addTable(createSampleTable(n)));
        assertEquals(expected, restaurant.getMaxSeatsNumber());
    }

    public static Stream<Arguments> seat_number_provider() {
        return Stream.of(
                arguments(List.of(1, 2, 3, 3, 5), 5),
                arguments(List.of(1, 10, 9), 10),
                arguments(List.of(5), 5)
        );
    }
}
