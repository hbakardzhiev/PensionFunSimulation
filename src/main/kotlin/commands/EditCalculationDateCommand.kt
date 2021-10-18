package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.Account
import java.time.Instant
import java.time.LocalDate

class EditCalculationDateCommand(private val viewer: Account?, private val id: String, private val date: LocalDate): Command {
    override fun execute() {
        when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> {
                val event = PolicyEvent.EditCalculationDateEvent(
                    clientNumber = id,
                    id = generateUUID(),
                    aggregateID = id,
                    newDate = date,
                    createdBy = viewer,
                    createdAt = Instant.now(),
                    appliesAt = Instant.now()
                )
                globalEnv.eventProcessor.process(event)
            }
        }
    }
}