package events

import environment.globalEnv
import models.FinalParticipant


interface Updater {
    fun update(event: PolicyEvent)
}

class PolicyUpdater() : Updater {
    override fun update(event: PolicyEvent) {
        val participant = globalEnv.inMemoryDatabase.getParticipantNullable(event.aggregateID)
        val processed = event.processevent(participant)
        globalEnv.inMemoryDatabase.replaceParticipant(processed, event.aggregateID)
    }
}

fun PolicyEvent.processevent(participant: FinalParticipant?): FinalParticipant {
    return when (this) {
        is PolicyEvent.CreatedPolicyEvent -> processEventType()
        is PolicyEvent.UpdatedBirthdateEvent -> processEventType(participant!!)
        is PolicyEvent.AddPartnerEvent -> processEventType(participant!!)
        is PolicyEvent.AddPersonalInfoEvent -> processEventType(participant!!)
        is PolicyEvent.EditCalculationDateEvent -> processEventType(participant!!)
        is PolicyEvent.EditRetirementAgeEvent -> processEventType(participant!!)
        is PolicyEvent.UpdatedGenderEvent -> processEventType(participant!!)
        is PolicyEvent.RemovePolicyEvent -> processEventType(participant!!)
    }
}

fun PolicyEvent.RemovePolicyEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        id = id
    )
}

fun PolicyEvent.CreatedPolicyEvent.processEventType(): FinalParticipant {
    return FinalParticipant(
        id = aggregateID,
        birthDate = birthdate.toString(),
        gender = gender,
        op = op,
        ingOp = ingOp
    )
}


fun PolicyEvent.UpdatedBirthdateEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        birthDate = newBirthdate.toString()
    )
}

fun PolicyEvent.UpdatedGenderEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        gender = gender
    )
}

fun PolicyEvent.AddPartnerEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        gender = genderPartner,
        birthDate = birthdatePartner.toString(),
        startDate = startDate.toString()
    )
}

fun PolicyEvent.EditCalculationDateEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        startDate = newDate.toString()
    )
}

fun PolicyEvent.EditRetirementAgeEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        retirementAge = newRetirementAge
    )
}

fun PolicyEvent.AddPersonalInfoEvent.processEventType(participant: FinalParticipant): FinalParticipant {
    return participant.copy(
        salary = yearlySalary,
        employed = employed
    )
}

