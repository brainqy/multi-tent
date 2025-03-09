Feature: User Login

  Scenario: Successful login with valid credentials
    Given a user with email "dvsomwanshi@gmail.com" and password "dnyanesh@123"
    When the user attempts to log in
    Then the login should be successful
    And a JWT token should be generated
    And the response status should be "SUCCESS"
