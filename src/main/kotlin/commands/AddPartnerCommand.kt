package commands

import environment.generateUUID
import environment.globalEnv
import events.PolicyEvent
import models.*
import java.lang.Exception
import java.time.Instant

class AddPartnerCommand(private val viewer: Account?, private val input: InputParticipant): Command {
    override fun execute() {
        when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> {
                val createdPolicyEvent = PolicyEvent.CreatedPolicyEvent (
                    clientNumber = input.id,
                    birthdate = input.birthdate,
                    gender = input.gender,
                    createdAt = Instant.now(),
                    appliesAt = Instant.now(),
                    createdBy = viewer,
                    id = generateUUID(),
                    aggregateID = input.id,
                    op = 0.0,
                    ingOp = 0.0
                )
                globalEnv.eventProcessor.process(createdPolicyEvent)
            }
        }
    }
}