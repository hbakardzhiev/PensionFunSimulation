package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.Account
import java.lang.Exception
import java.time.Instant
import java.time.LocalDate

class ChangeBirthdateCommand(private val viewer: Account?, private val id: String, private val date: LocalDate) : Command {

    override fun execute() {
        when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> {
                val event = PolicyEvent.UpdatedBirthdateEvent(
                    newBirthdate = date,
                    id = generateUUID(),
                    appliesAt = Instant.now(),
                    createdBy = viewer,
                    aggregateID = id,
                    createdAt = Instant.now(),
                    clientNumber = id
                )
                globalEnv.eventProcessor.process(event)
            }
        }
    }
}