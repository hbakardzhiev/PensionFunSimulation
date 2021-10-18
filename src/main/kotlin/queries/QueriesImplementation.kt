package queries

import ConfigurationExcel
import environment.InMemoryDatabase
import environment.globalEnv
import events.processevent
import models.*
import java.time.Instant
import java.time.LocalDate
import kotlin.Exception

open class QueriesImplementation : Queries {

    override fun getParticipant(viewer: Account?, id: String, timestamp: Instant?): FinalParticipant {
        return when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> {
                val participant = globalEnv.inMemoryDatabase.getParticipant(id)
                if (timestamp != null) {
                    val event = globalEnv.eventProcessor.store.getEventsForAggregateID(id)
                        .last { event -> event.appliesAt == timestamp }
                    event.processevent(participant)
                } else {
                    participant
                }
            }
        }
    }

    override fun updateParticipant(
        viewer: Account?,
        id: String,
        gender: Gender,
        birthDate: String,
        context: Context
    ): FinalParticipant {
        return when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> globalEnv.inMemoryDatabase.updateParticipant(id, gender, birthDate)
        }
    }

    override fun getParticipants(viewer: Account?): List<FinalParticipant> {
        return when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> globalEnv.inMemoryDatabase.getParticipants()
        }
    }

    override fun updateInterest(viewer: Account?, age: Int, interest: Double, context: Context): Double {
        return when (viewer == null) {
            true -> throw Exception("Unauthenticated")
            false -> globalEnv.inMemoryDatabase.updateInterest(age, interest)
        }
    }

    override fun deleteParticipant(viewer: Account?, id: String, context: Context): String {
        globalEnv.inMemoryDatabase.deleteParticipant(id)
        return id
    }


    override fun getInterest(viewer: Account?, age: Int): Double {
        if (viewer == null)
            throw Exception("Unauthenticated")
        return globalEnv.inMemoryDatabase.interestMap[age] ?: throw Exception("Interest not found")
    }

    override fun updateAssumptions(
        viewer: Account?,
        assumptions: Assumptions,
        context: Context
    ): Assumptions {
        val assumptionsMortality = globalEnv.inMemoryDatabase.assumptions
        val date = LocalDate.parse(assumptions.date)
        val newConfiguration = ConfigurationExcel(
            date,
            assumptions.age,
            assumptions.month,
            assumptions.pensionYear,
            assumptions.pensionMonth,
            assumptions.sex,
            assumptions.maleBeforeStart,
            assumptions.femaleBeforeStart,
            assumptions.maleAfterStart,
            assumptions.femaleAfterStart,
            assumptionsMortality.maleMortality,
            assumptionsMortality.femaleMortality
        )
        globalEnv.inMemoryDatabase.assumptions = newConfiguration
        return assumptions
    }

    override fun getMortalityRate(viewer: Account?, age: Int, year: Int): Double {
        if (viewer == null)
            throw Exception("Unauthenticated")
        val result = InMemoryDatabase.getMortalityForCurrSex()
        return result[age]?.get(year) ?: throw Exception("No such interest")
    }

    override fun getAssumptions(viewer: Account?): Assumptions {
        if (viewer == null)
            throw Exception("Unauthenticated")

        val assumptions = globalEnv.inMemoryDatabase.assumptions
        return Assumptions(
            assumptions.age,
            assumptions.month,
            assumptions.date.toString(),
            assumptions.maleBeforeStart,
            assumptions.maleAfterStart,
            assumptions.femaleBeforeStart,
            assumptions.femaleAfterStart,
            assumptions.pensionYear,
            assumptions.pensionMonth,
            assumptions.sex
        )
    }

    override fun getAccessCodeForAccount(id: String, password: String): String? {
        return globalEnv.inMemoryDatabase.findByUsernameAndPassword(id, password)
    }

    override fun getEvents(): List<TransportEvent> {
        return globalEnv.eventProcessor.store.getEvents()
            .map { TransportEvent(it.id, it.aggregateID, it.appliesAt.toString()) }
    }
}