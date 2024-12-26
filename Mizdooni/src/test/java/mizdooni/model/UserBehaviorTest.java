package mizdooni.model;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import mizdooni.MizdooniApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CreateSample.createSampleUser;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(classes = MizdooniApplication.class)
public class UserBehaviorTest {

    private User user1;
    private User user2;

    @Given("a sample user")
    public void aSampleUser() {
        user1 = createSampleUser();
    }


    @Given("two sample users")
    public void twoSampleUsers() {
        user1 = createSampleUser();
        user2 = createSampleUser();
    }

    @When("the user adds a reservation")
    public void theUserAddsAReservation() {
        user1.addReservation(createSampleReservation());
    }

    @When("the user adds another reservation")
    public void theUserAddsAnotherReservation() {
        theUserAddsAReservation();
    }

    @When("the first user adds a reservation")
    public void theFirstUserAddsAReservation() {
        user1.addReservation(createSampleReservation());
    }

    @When("the second user adds a reservation")
    public void theSecondUserAddsAReservation() {
        user2.addReservation(createSampleReservation());
    }

    @Then("the user's reservation list should contain {int} reservation(s)")
    public void theUserReservationListShouldContainReservations(int expectedCount) {
        assertEquals(expectedCount, user1.getReservations().size());
    }

    @Then("the reservation's number should be {int}")
    public void theReservationNumberShouldBe(int expectedNumber) {
        int actualNumber = user1.getReservations().getFirst().getReservationNumber();
        assertEquals(expectedNumber, actualNumber);
    }

    @Then("the first reservation's number should be {int}")
    public void theFirstReservationNumberShouldBe(int expectedNumber) {
        int actualNumber = user1.getReservations().getFirst().getReservationNumber();
        assertEquals(expectedNumber, actualNumber);
    }

    @Then("the second reservation's number should be {int}")
    public void theSecondReservationNumberShouldBe(int expectedNumber) {
        int actualNumber = user1.getReservations().get(1).getReservationNumber();
        assertEquals(expectedNumber, actualNumber);
    }

    @Then("the first user’s reservation list should contain {int} reservation")
    public void theFirstUserReservationListShouldContainReservation(int expectedCount) {
        assertEquals(expectedCount, user1.getReservations().size());
    }

    @Then("the second user’s reservation list should contain {int} reservation")
    public void theSecondUserReservationListShouldContainReservation(int expectedCount) {
        assertEquals(expectedCount, user2.getReservations().size());
    }

    @Then("the first user’s reservation number should be {int}")
    public void theFirstUserReservationNumberShouldBe(int expectedNumber) {
        int actualNumber = user1.getReservations().getFirst().getReservationNumber();
        assertEquals(expectedNumber, actualNumber);
    }

    @Then("the second user’s reservation number should be {int}")
    public void theSecondUserReservationNumberShouldBe(int expectedNumber) {
        int actualNumber = user2.getReservations().getFirst().getReservationNumber();
        assertEquals(expectedNumber, actualNumber);
    }

}
