package mizdooni.model;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import mizdooni.MizdooniApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CreateSample.createSampleUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MizdooniApplication.class)
public class RestaurantBehaviorTest {
    User user1;
    User user2;
    Restaurant restaurant;
    Rating averageRating;

    private static final double DELTA = 0.0001;

    @Given("a sample user and a sample restaurant")
    public void aSampleUserAndRestaurant() {
        user1 = createSampleUser("user1");
        restaurant = createSampleRestaurant();
    }

    @Given("two sample user and a sample restaurant")
    public void twoSampleUserAndASampleRestaurant() {
        user1 = createSampleUser("user1");
        user2 = createSampleUser("user2");
        restaurant = createSampleRestaurant();
    }

    @Given("the restaurant has no reviews")
    public void theRestaurantHasNoReviews() {
        restaurant.getReviews().clear();
    }

    @Given("the restaurant has a review for the user with comment {string} and overall rating {int}")
    public void theRestaurantHasReviewForTheUser(String comment, int overallRating) {
        Review review = createSampleReview(user1, comment, overallRating);
        restaurant.getReviews().add(review);
    }

    @When("the user adds a review with the comment {string} and an overall rating of {int}")
    public void theUserAddsReview(String comment, int overallRating) {
        Review review = createSampleReview(user1, comment, overallRating);
        restaurant.addReview(review);
    }

    @When("the first user adds a review with the comment {string} and an overall rating of {int}")
    public void theFirstUserAddsAReview(String comment, int overallRating) {
        theUserAddsReview(comment, overallRating);
    }

    @When("the second user adds a review with the comment {string} and an overall rating of {int}")
    public void theSecondUserAddsAReview(String comment, int overallRating) {
        Review review = createSampleReview(user2, comment, overallRating);
        restaurant.addReview(review);
    }

    @Then("the restaurant should have {int} review")
    public void theRestaurantShouldHaveReview(int expectedReviewCount) {
        assertEquals(expectedReviewCount, restaurant.getReviews().size());
    }

    @Then("the review's comment should be {string}")
    public void theReviewCommentShouldBe(String expectedComment) {
        theFirstReviewCommentShouldBe(expectedComment);
    }

    @Then("the review's overall rating should be {int}")
    public void theReviewOverallRatingShouldBe(int expectedRating) {
        theFirstReviewRatingShouldBe(expectedRating);
    }

    @Then("the review's user should be the user")
    public void theReviewUserShouldBeTheUser() {
        theFirstReviewUserShouldBetheFirstUser();
    }

    @Then("the restaurant should have {int} reviews")
    public void theRestaurantShouldHaveReviews(int expectedReviewCount) {
        assertEquals(expectedReviewCount, restaurant.getReviews().size());
    }

    @Then("the first review's comment should be {string}")
    public void theFirstReviewCommentShouldBe(String expectedComment) {
        assertEquals(expectedComment, restaurant.getReviews().getFirst().getComment());
    }

    @Then("the first review's overall rating should be {int}")
    public void theFirstReviewRatingShouldBe(int expectedRating) {
        assertEquals(expectedRating, restaurant.getReviews().getFirst().getRating().overall);
    }

    @Then("the first review's user should be the first user")
    public void theFirstReviewUserShouldBetheFirstUser() {
        assertEquals(user1, restaurant.getReviews().getFirst().getUser());
    }

    @Then("the second review's user should be the second user")
    public void theSecondReviewUserShouldBetheSecondUser() {
        assertEquals(user2, restaurant.getReviews().get(1).getUser());
    }

    @Then("the second review's comment should be {string}")
    public void theSecondReviewCommentShouldBe(String expectedComment) {
        assertEquals(expectedComment, restaurant.getReviews().get(1).getComment());
    }

    @Then("the second review's overall rating should be {int}")
    public void theSecondReviewRatingShouldBe(int expectedRating) {
        assertEquals(expectedRating, restaurant.getReviews().get(1).getRating().overall);
    }


    @Given("a restaurant with no reviews")
    public void aRestaurantWithNoReviews() {
        restaurant = createSampleRestaurant();
        restaurant.getReviews().clear();
    }

    @When("the average rating is calculated")
    public void theAverageRatingIsCalculated() {
        averageRating = restaurant.getAverageRating();
    }


    @Then("the average rating for food should be {double}")
    public void theAverageRatingForFoodShouldBe(double expectedFoodRating) {
        assertEquals(expectedFoodRating, averageRating.food, DELTA);
    }


    @And("the average rating for service should be {double}")
    public void theAverageRatingForServiceShouldBe(double expectedServiceRating) {
        assertEquals(expectedServiceRating, averageRating.service, DELTA);
    }


    @And("the average rating for ambiance should be {double}")
    public void theAverageRatingForAmbianceShouldBe(double expectedAmbianceRating) {
        assertEquals(expectedAmbianceRating, averageRating.ambiance, DELTA);
    }


    @And("the average rating for overall should be {double}")
    public void theAverageRatingForOverallShouldBe(double expectedOverallRating) {
        assertEquals(expectedOverallRating, averageRating.overall, DELTA);
    }


    @Given("a restaurant with a review having food rating of {int}, service rating of {int}, ambiance rating of {int}, and overall rating of {int}")
    public void aRestaurantWithAReviewHavingFoodRatingOfServiceRatingOfAmbianceRatingOfAndOverallRatingOf(int foodRating, int serviceRating, int ambianceRating, int overallRating) {
        aRestaurantWithNoReviews();
        Rating rating = createSampleRating(foodRating, serviceRating, ambianceRating, overallRating);
        Review review = createSampleReview(rating);
        restaurant.getReviews().add(review);
    }

    @Given("a restaurant with reviews:")
    public void aRestaurantWithReviews(io.cucumber.datatable.DataTable dataTable) {
        aRestaurantWithNoReviews();
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows.subList(1, rows.size())) {
            int food = Integer.parseInt(row.get(0));
            int service = Integer.parseInt(row.get(1));
            int ambiance = Integer.parseInt(row.get(2));
            int overall = Integer.parseInt(row.get(3));

            Rating rating = createSampleRating(food, service, ambiance, overall);
            Review review = createSampleReview(rating);
            restaurant.getReviews().add(review);
        }
    }
}
