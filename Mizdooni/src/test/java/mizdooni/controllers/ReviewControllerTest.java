package mizdooni.controllers;

import mizdooni.exceptions.*;
import mizdooni.model.Restaurant;
import mizdooni.model.Review;
import mizdooni.response.PagedList;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.controllers.ControllerUtils.PARAMS_BAD_TYPE;
import static mizdooni.controllers.ControllerUtils.PARAMS_MISSING;
import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CustomAssertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static mizdooni.utils.CreateSample.createSampleRatingMap;
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
    void getReviews_ExistedRestaurantId_ReturnsReviews() throws RestaurantNotFound {
        Restaurant restaurant = createSampleRestaurant();
        PagedList<Review> reviews = createSamplePageListOfReviews();
        int page = createSamplePositiveNumber();

        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        when(reviewService.getReviews(restaurant.getId(), page)).thenReturn(reviews);

        Response result = reviewController.getReviews(restaurant.getId(), page);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("reviews for restaurant (" + restaurant.getId() + "): " + restaurant.getName(), result.getMessage());
        assertEquals(reviews, result.getData());
    }

    @Test
    void getReviews_NotExistedRestaurantId_ThrowsException() {
        ResponseException responseException = new ResponseException(HttpStatus.NOT_FOUND, "restaurant not found");
        when(restaurantService.getRestaurant(anyInt())).thenThrow(responseException);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reviewController.getReviews(createSampleId(), createSamplePositiveNumber());
        });

        assertEquals(responseException, result);
    }

    @Test
    public void addReview_ValidParams_addsReviews() throws UserNotFound, ManagerCannotReview, UserHasNotReserved, RestaurantNotFound, InvalidReviewRating {
        Restaurant restaurant = createSampleRestaurant();
        String comment = createSampleComment();
        Map<String, Number> ratingMap = createSampleRatingMap();
        Map<String, Object> params = Map.of("comment", comment,"rating", ratingMap);
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        reviewController.addReview(restaurant.getId(), params);

        verify(reviewService).addReview(eq(restaurant.getId()), argThat(rating ->
                                rating.food == ratingMap.get("food").doubleValue() &&
                                rating.service == ratingMap.get("service").doubleValue() &&
                                rating.ambiance == ratingMap.get("ambiance").doubleValue() &&
                                rating.overall == ratingMap.get("overall").doubleValue()),
                            eq(comment));
    }

    @Test
    public void addReview_ValidParams_ReturnsCorrectResponse() {
        Restaurant restaurant = createSampleRestaurant();
        String comment = createSampleComment();
        Map<String, Number> ratingMap = createSampleRatingMap();
        Map<String, Object> params = Map.of("comment", comment,"rating", ratingMap);
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);

        Response response = reviewController.addReview(restaurant.getId(), params);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("review added successfully", response.getMessage());
    }

    @Test
    void addReview_CommentMissing_ThrowsException() {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        Map<String, Object> params = Map.of("rating", createSampleRatingMap());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurant.getId(), params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, PARAMS_MISSING);
        assertEquals(expected, result);
    }

    @Test
    void addReview_RatingMissing_ThrowsException() {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        Map<String, Object> params = Map.of("comment", createSampleComment());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurant.getId(), params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, PARAMS_MISSING);
        assertEquals(expected, result);
    }

    @Test
    public void addReview_ParamsBadType_ThrowsException() {
        Restaurant restaurant = createSampleRestaurant();
        String comment = createSampleComment();
        Map<String, String> ratingMap =  Map.of("food", "", "service", "", "ambiance", "", "overall", "");
        Map<String, Object> params = Map.of("comment", comment,"rating", ratingMap);
        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurant.getId(), params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void addReview_ReviewServiceThrowsException_ThrowsException() throws UserNotFound, ManagerCannotReview, UserHasNotReserved, RestaurantNotFound, InvalidReviewRating {
        Restaurant restaurant = createSampleRestaurant();
        String comment = createSampleComment();
        Map<String, Number> ratingMap = createSampleRatingMap();
        Map<String, Object> params = Map.of("comment", comment,"rating", ratingMap);

        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        UserNotFound exception = createSampleUserNotFoundException();
        doThrow(exception).when(reviewService).addReview(anyInt(), any(), any());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurant.getId(), params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, exception);
        assertEquals(expected, result);
    }

}