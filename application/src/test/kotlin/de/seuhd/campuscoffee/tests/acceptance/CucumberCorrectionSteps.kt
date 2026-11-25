package de.seuhd.campuscoffee.tests.acceptance

import de.seuhd.campuscoffee.tests.ConsumptionRequests
import io.cucumber.java.ParameterType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import java.time.LocalTime

/**
 * Step definitions for the correction-specific phrases in `correction.feature`. The `{string}` and `{int}` parameters
 * also occur in [CucumberConsumptionSteps], and this class reuses its custom `{time}` parameter type. It also uses the
 * built-in `{word}` parameter and defines `{correctionCount}` locally. Both step classes use the [SettableClock] bean
 * and the [ConsumptionRequests] object; their per-step state is separate.
 */
class CucumberCorrectionSteps(
    private val clock: SettableClock
) {
    private lateinit var loginName: String
    private lateinit var actingUser: String
    private var correctedCount: Int = 0
    private var lastStatus: Int = 0
    private var lastNote: String? = null

    /** Converts a correction count in a step, including the phrase `negative count`, to an integer. */
    @ParameterType("negative count|-?\\d+")
    fun correctionCount(value: String): Int = if (value == "negative count") -1 else value.toInt()

    /** Adds [count] coffees for the correction target. */
    @Given("the correction target {string} has {int} coffees")
    fun theCorrectionTargetHasCoffees(
        loginName: String,
        count: Int
    ) {
        require(count >= 0) { "The step needs a non-negative coffee count." }
        this.loginName = loginName
        repeat(count) { addCoffeeSuccessfully() }
        assertCoffeeCount(count)
    }

    /** Adds [count] coffees for the correction target at [time]. */
    @Given("the correction target {string} has {int} coffees from {time}")
    fun theCorrectionTargetHasCoffeesFrom(
        loginName: String,
        count: Int,
        time: LocalTime
    ) {
        require(count >= 0) { "The step needs a non-negative coffee count." }
        this.loginName = loginName
        clock.setTime(time)
        repeat(count) { addCoffeeSuccessfully() }
        assertCoffeeCount(count)
    }

    /** Adds a coffee for the correction target at [time] and checks the resulting count. */
    @Given("the correction target adds a coffee at {time} \\(count {int}\\)")
    fun theCorrectionTargetAddsACoffeeAt(
        time: LocalTime,
        count: Int
    ) {
        clock.setTime(time)
        addCoffeeSuccessfully()
        assertCoffeeCount(count)
    }

    /** Remembers the admin and count for the correction completed by the note step. */
    @When("the admin {string} corrects the user's coffee count to {correctionCount}")
    fun theAdminCorrectsTheUsersCoffeeCountTo(
        adminLoginName: String,
        count: Int
    ) {
        rememberCorrection(adminLoginName, count)
    }

    /** Remembers the non-admin and count for the correction completed by its reason step. */
    @When("the user {string} corrects the user's coffee count to {correctionCount}")
    fun theUserCorrectsTheUsersCoffeeCountTo(
        userLoginName: String,
        count: Int
    ) {
        rememberCorrection(userLoginName, count)
    }

    /** Sends the pending correction with [note]. */
    @When("with the note {string}")
    fun withTheNote(note: String) {
        lastNote = note
        submitCorrection(note)
    }

    /** Sends the pending non-admin correction with a valid note, so the role check is exercised. */
    @When("the user {string} is not an admin")
    fun theUserIsNotAnAdmin(userLoginName: String) {
        assertThat(userLoginName).isEqualTo(actingUser)
        submitCorrection("Authorization check")
    }

    /** Sends the pending correction without a note. */
    @When("without a note or with a blank note")
    fun withoutANoteOrWithABlankNote() {
        submitCorrection(null)
    }

    /** Accepts the empty reason cell of the negative-count scenario outline. */
    @When("^$")
    fun noAdditionalCorrectionReason() = Unit

    /** Corrects the target's coffee count at [time]. */
    @When("the admin {string} corrects the count to {int} at {time}")
    fun theAdminCorrectsTheCountAt(
        adminLoginName: String,
        count: Int,
        time: LocalTime
    ) {
        clock.setTime(time)
        lastStatus =
            ConsumptionRequests.correctCoffeeCount(
                adminLoginName,
                loginName,
                count,
                "Count correction"
            )
    }

    /** Undoes a coffee for the correction target at [time]. */
    @When("the correction target undoes a coffee at {time}")
    fun theCorrectionTargetUndoesACoffeeAt(time: LocalTime) {
        clock.setTime(time)
        lastStatus = ConsumptionRequests.undoCoffee(loginName)
    }

    /** Checks that a correction succeeds and leaves the target with [count] coffees. */
    @Then("the correction succeeds and the coffee count is {int}")
    fun theCorrectionSucceedsAndTheCoffeeCountIs(count: Int) {
        assertThat(lastStatus).isEqualTo(200)
        assertCoffeeCount(count)
    }

    /** Checks that a correction fails and leaves the target with [count] coffees. */
    @Then("the correction fails and the coffee count stays {int}")
    fun theCorrectionFailsAndTheCoffeeCountStays(count: Int) {
        assertThat(lastStatus).isNotEqualTo(200)
        assertCoffeeCount(count)
    }

    /** Checks that the response has [status] and the matching [reasonPhrase]. */
    @Then("^the response status is (\\d+) (.+)$")
    fun theResponseStatusIs(
        status: Int,
        reasonPhrase: String
    ) {
        assertThat(lastStatus).isEqualTo(status)
        assertThat(HttpStatus.valueOf(status).reasonPhrase).isEqualTo(reasonPhrase)
    }

    /** Checks that the target's count equals [count]. */
    @Then("the correction target's coffee count stays {int}")
    fun theCorrectionTargetsCoffeeCountStays(count: Int) {
        assertCoffeeCount(count)
    }

    /** Checks that the correction records its before and after counts, note, and admin. */
    @Then(
        "and the corrections of {string} list the count before \\({int}\\), " +
            "the count after \\({int}\\), the note, and {word}"
    )
    fun theCorrectionsListTheCorrection(
        correctedUser: String,
        countBefore: Int,
        countAfter: Int,
        adminLoginName: String
    ) {
        assertThat(ConsumptionRequests.corrections(correctedUser))
            .contains(
                ConsumptionRequests.Correction(
                    countBefore = countBefore,
                    countAfter = countAfter,
                    note = checkNotNull(lastNote),
                    adminLoginName = adminLoginName
                )
            )
    }

    private fun rememberCorrection(
        actingUser: String,
        count: Int
    ) {
        this.actingUser = actingUser
        correctedCount = count
        lastNote = null
        if (count < 0) {
            submitCorrection("Count correction")
        }
    }

    private fun submitCorrection(note: String?) {
        lastStatus =
            ConsumptionRequests.correctCoffeeCount(
                actingUser,
                loginName,
                correctedCount,
                note
            )
    }

    private fun addCoffeeSuccessfully() {
        assertThat(ConsumptionRequests.addCoffee(loginName))
            .`as`("status of adding a coffee")
            .isEqualTo(200)
    }

    private fun assertCoffeeCount(count: Int) {
        assertThat(ConsumptionRequests.coffeeCount(loginName)).isEqualTo(count)
    }
}
