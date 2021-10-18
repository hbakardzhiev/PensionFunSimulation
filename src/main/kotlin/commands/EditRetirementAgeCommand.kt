package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.Account
import java.time.Instant
import kotlin.Exception

class EditRetirementAgeCommand(private val viewer: Account?, private val id: String, private val age: Int): Command {
    override fun execute() {
        when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> {
                val event = PolicyEvent.EditRetirementAgeEvent(
                    clientNumber = id,
                    id = generateUUID(),
                    createdAt = Instant.now(),
                    createdBy = viewer,
                    aggregateID = id,
                    appliesAt = Instant.now(),
                    newRetirementAge = age
                )
                globalEnv.eventProcessor.process(event)
            }
        }
    }
}