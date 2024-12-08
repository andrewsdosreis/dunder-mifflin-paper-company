Feature: Employee Management
  In order to manage employees
  As a client
  I want to create and retrieve employees along with their photo and trivia.

  Scenario: Create a new employee and retrieve their data
    Given the system is initialized with no employees
    When I create an employee with firstName "Jim" lastName "Halpert" department "Sales" and photo "jim-test.png"
    Then the employee should be persisted in the database with firstName "Jim" lastName "Halpert" department "Sales" and photo "employees/Jim_Halpert.png"
    And I can retrieve the employee by id
    And the employee's trivia should be available
    And the employee's photo should be accessible in S3

  Scenario: Retrieve an existing employee by ID
    Given an employee with firstName "Michael" lastName "Scott" department "Management" and photo "michael-test.png" exists
    When I can retrieve the employee by id
    Then I should see the employee details with firstName "Michael" lastName "Scott" department "Management" and photo "employees/Michael_Scott.png"
    And the employee's trivia should be available
    And the employee's photo should be accessible in S3
