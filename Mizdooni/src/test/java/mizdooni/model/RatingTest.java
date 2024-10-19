package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.utils.CreateSample.*;
import static org.junit.jupiter.api.Assertions.*;

public class RatingTest {
    private Rating rating;

    @BeforeEach
    public void setup() {
        rating = new Rating();
    }

    @Test
    public void get_star_count_when_overall_is_greater_than_five_returns_five() {
        rating.overall = 5.5;
        assertEquals(5, rating.getStarCount());
    }

    @Test
    public void get_star_count_when_overall_is_a_double_returns_its_round() {
        rating.overall = 3.5;
        assertEquals(4, rating.getStarCount());
    }

    @Test
    public void get_star_count_when_overall_is_an_integer_returns_overall() {
        rating.overall = 3;
        assertEquals(3, rating.getStarCount());
    }
}

