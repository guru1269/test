Feature: User wants to validate the Car details provided in the input file

  Scenario: Comparing the vehicle details in the "<inputFile>" to given output file
    Given user launches the car check website
    And read car registration numbers from input file
    When perform the free car check
    Then retrieved car details should match to "outputFile" file
    Examples:
    |inputFile            |outputFile                |
    |input/givenInput.txt |output/expectedOutput.csv |
    |input/givenInput2.txt|output/expectedOutput2.csv|



