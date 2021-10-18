package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.Account
import models.PersonalInfoInput
import java.time.Instant

class AddPersonalInfoCommand(
    private val viewer: Account?,
    private val input: PersonalInfoInput
) : Command {

    override fun execute() {
        when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> {
                val event = PolicyEvent.AddPersonalInfoEvent(
                    yearlySalary = input.salary,
                    employed = input.employed,
                    clientNumber = input.id,
                    createdAt = Instant.now(),
                    appliesAt = Instant.now(),
                    createdBy = viewer,
                    id = generateUUID(),
                    aggregateID = input.id
                )
                globalEnv.eventProcessor.process(event)
            }
        }
    }
}