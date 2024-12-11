Feature: Employee Management
  In order to manage employees
  I want to create and retrieve employees along with their photo and trivia.

  Scenario: Create a new employee and retrieve their data
    Given the system is initialized with no employees
    When I create an employee with firstName "Jim" lastName "Halpert" department "Sales" and photo "jim-test.jpg"
    Then the employee should be persisted in the database with firstName "Jim" lastName "Halpert" department "Sales" and photo "employees/Jim_Halpert.jpg"
    And I can retrieve the employee by id
    And the employee's trivia should be available
    And the employee's photo should be accessible in S3

  Scenario: Retrieve an existing employee by ID
    Given an employee with firstName "Dwight" lastName "Schrute" department "Sales" and photo "dwight-test.jpg" exists
    When I can retrieve the employee by id
    Then I should see the employee details with firstName "Dwight" lastName "Schrute" department "Sales" and photo "employees/Dwight_Schrute.jpg"
    And the employee's trivia should be available
    And the employee's photo should be accessible in S3

  Scenario: Send Quotes to the SQS queue and retrieve the quotes
    Given an employee with firstName "Michael" lastName "Scott" department "Management" and photo "michael-test.png" exists
    And I have a message payload:
      """
      {
        "name": "MichaelScott",
        "quote": "That's What She Said!"
      }
      """
    When I send the message to the "quotes" SQS queue
    And I wait 2 seconds for the message to be processed
    Then I can retrieve the employee by id
    And the employee have the quote "That's What She Said!"
