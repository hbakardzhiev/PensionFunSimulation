package events

import models.Account
import models.Gender
import java.time.Instant
import java.time.LocalDate

interface Event {
    val id: String
    val aggregateID: String
    val appliesAt: Instant
    val createdAt: Instant
    val createdBy: Account
}

sealed class PolicyEvent: Event {

    data class RemovePolicyEvent(
        val clientNumber:String,
        override val aggregateID: String,
        override val appliesAt: Instant,
        override val createdAt: Instant,
        override val createdBy: Account,
        override val id: String
    ): PolicyEvent()

    data class CreatedPolicyEvent(
        val clientNumber: String,
        val gender: Gender,
        val birthdate: LocalDate,
        val op: Double,
        val ingOp: Double,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String = clientNumber
    ) : PolicyEvent()

    data class UpdatedBirthdateEvent(
        val clientNumber: String,
        val newBirthdate: LocalDate,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String = clientNumber
    ) : PolicyEvent()

    data class UpdatedGenderEvent(
        val clientNumber: String,
        val gender: Gender,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String = clientNumber
    ) : PolicyEvent()

    data class AddPartnerEvent(
        val startDate: LocalDate,
        val birthdatePartner: LocalDate,
        val genderPartner: Gender,
        val clientNumber: String,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String
    ) : PolicyEvent()

    data class EditCalculationDateEvent(
        val newDate: LocalDate,
        val clientNumber: String,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String = clientNumber
    ) : PolicyEvent()

    data class EditRetirementAgeEvent(
        val newRetirementAge: Int,
        val clientNumber: String,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String = clientNumber
    ) : PolicyEvent()

    data class AddPersonalInfoEvent(
        val yearlySalary: Int,
        val employed: Boolean,
        val clientNumber: String,
        override val createdAt: Instant,
        override val appliesAt: Instant,
        override val createdBy: Account,
        override val id: String,
        override val aggregateID: String = clientNumber
    ) : PolicyEvent()
}

interface EventStore {
    fun writeEvent(event: PolicyEvent)
    fun getEvent(id: String): PolicyEvent?
    fun getEventsForAggregateID(aggregateID: String): List<PolicyEvent>
    fun getEvents(): List<PolicyEvent>
}

class InMemoryEventStore : EventStore {
    private val storedEvents = mutableMapOf<String, PolicyEvent>()
    override fun writeEvent(event: PolicyEvent) {
        storedEvents.put(event.id, event)
    }

    override fun getEvent(id: String): PolicyEvent? {
        return storedEvents.get(id)
    }

    override fun getEventsForAggregateID(aggregateID: String): List<PolicyEvent> {
        return storedEvents.filterValues { policyEvent ->
            policyEvent.aggregateID == aggregateID
        }.values.toList()
    }

    override fun getEvents(): List<PolicyEvent> {
        return storedEvents.values.toList()
    }
}

