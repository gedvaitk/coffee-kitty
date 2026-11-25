Feature: Coffee count correction
  An admin corrects the coffee count of a user.

  Scenario: An admin corrects a user's coffee count
    Given the correction target "maxmustermann" has 3 coffees
    When the admin "jane_doe" corrects the user's coffee count to 2
    And with the note "Double tap on 16.11."
    Then the correction succeeds and the coffee count is 2
    And and the corrections of "maxmustermann" list the count before (3), the count after (2), the note, and jane_doe

  Scenario Outline: Correct a user's coffee count
    Given the correction target "maxmustermann" has 3 coffees
    When <actor> corrects the user's coffee count to <number>
    And <reason>
    Then the correction fails and the coffee count stays 3
    And the response status is <status>

    Examples:
      | actor                    | number         | reason                                   | status          |
      | the user "maxmustermann" | 2              | the user "maxmustermann" is not an admin | 403 Forbidden   |
      | the admin "jane_doe"     | 2              | without a note or with a blank note      | 400 Bad Request |
      | the admin "jane_doe"     | negative count |                                          | 400 Bad Request |

  Scenario: Undo when the grace period of the coffee before has passed
    Given the correction target "maxmustermann" has 2 coffees from 09:00:00
    And the correction target adds a coffee at 10:00:00 (count 3)
    When the admin "jane_doe" corrects the count to 2 at 10:02:00
    And the correction target undoes a coffee at 10:03:00
    Then the response status is 409 Conflict
    And the correction target's coffee count stays 2
