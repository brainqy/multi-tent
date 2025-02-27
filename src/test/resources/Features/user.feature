Feature: User Management

  Scenario: Find user by email
    Given a user with email exists
    When I search for the user with email
    Then I should get the user details with name
