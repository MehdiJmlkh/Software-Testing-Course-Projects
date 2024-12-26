Feature: Add reservations to the system

  Scenario: Add a single reservation by a user
    Given a sample user
    When the user adds a reservation
    Then the user's reservation list should contain 1 reservation
    And the reservation's number should be 0

  Scenario: Add multiple reservations by a single user
    Given a sample user
    When the user adds a reservation
    And the user adds another reservation
    Then the user's reservation list should contain 2 reservations
    And the first reservation's number should be 0
    And the second reservation's number should be 1

  Scenario: Add reservations by multiple users
    Given two sample users
    When the first user adds a reservation
    And the second user adds a reservation
    Then the first user’s reservation list should contain 1 reservation
    And the second user’s reservation list should contain 1 reservation
    And the first user’s reservation number should be 0
    And the second user’s reservation number should be 0
