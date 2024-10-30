package mizdooni.controllers;

import mizdooni.exceptions.RestaurantNotFound;
import mizdooni.model.Restaurant;
import mizdooni.model.Review;
import mizdooni.response.PagedList;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static mizdooni.utils.CreateSample.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewControllerTest {
    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReviews_ValidRestaurantId_ReturnsReviews() throws RestaurantNotFound {
        Restaurant restaurant = create_sample_restaurant();
        PagedList<Review> reviews = createSamplePageListOfReviews();

        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        when(reviewService.getReviews(anyInt(), anyInt())).thenReturn(reviews);

        Response response = Response.ok("reviews for restaurant (" + restaurant.getId() + "): " + restaurant.getName(), reviews);
        Response result = reviewController.getReviews(restaurant.getId(), 2);

        assertEquals(result.getMessage(), response.getMessage());
        assertEquals(result.getData(), response.getData());
    }

    @Test
    void getReviews_InvalidRestaurantId_ThrowsException() {
        ResponseException responseException = new ResponseException(HttpStatus.NOT_FOUND, "restaurant not found");
        when(restaurantService.getRestaurant(anyInt())).thenThrow(responseException);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reviewController.getReviews(createSampleId(), createSamplePageNumber());
        });

        assertEquals(result.getMessage(), responseException.getMessage());
        assertEquals(result.getStatus(), responseException.getStatus());
    }
}