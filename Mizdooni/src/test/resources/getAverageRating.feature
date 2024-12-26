Feature: Calculate Average Rating

  Scenario: Calculate average rating with no reviews
    Given a restaurant with no reviews
    When the average rating is calculated
    Then the average rating for food should be 0
    And the average rating for service should be 0
    And the average rating for ambiance should be 0
    And the average rating for overall should be 0

  Scenario: Calculate average rating with one review
    Given a restaurant with a review having food rating of 1, service rating of 2, ambiance rating of 3, and overall rating of 4
    When the average rating is calculated
    Then the average rating for food should be 1
    And the average rating for service should be 2
    And the average rating for ambiance should be 3
    And the average rating for overall should be 4

  Scenario: Calculate average rating with multiple reviews
    Given a restaurant with reviews:
      | food | service | ambiance | overall |
      | 4    | 5       | 3        | 5       |
      | 4    | 3       | 2        | 4       |
      | 4    | 2       | 4        | 3       |
    When the average rating is calculated
    Then the average rating for food should be 4
    And the average rating for service should be 3.3333
    And the average rating for ambiance should be 3
    And the average rating for overall should be 4
