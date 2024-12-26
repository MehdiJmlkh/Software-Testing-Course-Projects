package mizdooni.model;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import mizdooni.MizdooniApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CreateSample.createSampleUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MizdooniApplication.class)
public class RestaurantBehaviorTest {

    User user1;
    User user2;
    Restaurant restaurant;

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
}
