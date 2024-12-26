Feature: Add a review for a user

  Scenario: Add a review when no previous review exists
    Given a sample user and a sample restaurant
    And the restaurant has no reviews
    When the user adds a review with the comment "Great product!" and an overall rating of 5
    Then the restaurant should have 1 review
    And the review's comment should be "Great product!"
    And the review's overall rating should be 5
    And the review's user should be the user

  Scenario: Replace the existing review for a user
    Given a sample user and a sample restaurant
    And the restaurant has a review for the user with comment "Old review" and overall rating 2
    When the user adds a review with the comment "New review!" and an overall rating of 5
    Then the restaurant should have 1 review
    And the review's comment should be "New review!"
    And the review's overall rating should be 5
    And the review's user should be the user

  Scenario: Add reviews for different users
    Given two sample user and a sample restaurant
    And the restaurant has no reviews
    When the first user adds a review with the comment "Great product!" and an overall rating of 5
    And the second user adds a review with the comment "Not bad" and an overall rating of 3
    Then the restaurant should have 2 reviews
    And the first review's comment should be "Great product!"
    And the first review's overall rating should be 5
    And the first review's user should be the first user
    And the second review's comment should be "Not bad"
    And the second review's overall rating should be 3
    And the second review's user should be the second user
