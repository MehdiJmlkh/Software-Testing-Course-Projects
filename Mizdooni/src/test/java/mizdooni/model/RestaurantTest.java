package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.utils.CreateSample.*;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {
    private Restaurant restaurant;

    @BeforeEach
    public void setup() {
        restaurant = create_sample_restaurant();
    }

    @Test
    public void add_new_table_added_to_the_tables(){
        Table table = create_sample_table();
        restaurant.addTable(table);
        assertEquals(1, restaurant.getTables().size());
        assertEquals(table, restaurant.getTables().getFirst());
    }

    @Test
    public void add_new_table_increases_table_number(){
        Table table1 = create_sample_table();
        Table table2 = create_sample_table();
        restaurant.addTable(table1);
        restaurant.addTable(table2);
        assertEquals(1, table2.getTableNumber() - table1.getTableNumber());
    }

    @Test
    public void get_existent_table_returns_the_table() {
        Table table = create_sample_table();
        restaurant.addTable(table);
        assertEquals(table, restaurant.getTable(table.getTableNumber()));
    }

    @Test
    public void get_non_existent_table_returns_null() {
        Table table = create_sample_table();
        restaurant.addTable(table);
        assertNull(restaurant.getTable(table.getTableNumber() + 1));
    }

    @Test
    public void add_first_review_of_a_user_added_to_the_reviews(){
        Review review = create_sample_review(create_sample_user());
        restaurant.addReview(review);
        assertEquals(1, restaurant.getReviews().size());
        assertEquals(review, restaurant.getReviews().getFirst());
    }

    @Test
    public void add_second_review_of_a_user_delete_the_previous_one_from_reviews(){
        User user = create_sample_user();
        Review review1 = create_sample_review(user);
        Review review2 = create_sample_review(user);
        restaurant.addReview(review1);
        restaurant.addReview(review2);
        assertEquals(1, restaurant.getReviews().size());
        assertEquals(review2, restaurant.getReviews().getFirst());
    }
}
