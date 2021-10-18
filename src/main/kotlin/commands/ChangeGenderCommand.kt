package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.Account
import models.Gender
import java.lang.Exception
import java.time.Instant

class ChangeGenderCommand(private val viewer: Account?, private val id: String, private val gender: Gender) : Command {

    override fun execute() {
        when (viewer == null) {
            true -> throw Exception("Unauthorized")
            false -> {
                val event = PolicyEvent.UpdatedGenderEvent(
                    clientNumber = id,
                    id = generateUUID(),
                    aggregateID = id,
                    appliesAt = Instant.now(),
                    createdAt = Instant.now(),
                    createdBy = viewer,
                    gender = gender
                )
                globalEnv.eventProcessor.process(event)
            }
        }
    }

}