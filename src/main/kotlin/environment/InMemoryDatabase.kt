package environment

import ConfigurationExcel
import migration.initialMigration
import models.*
import java.time.LocalDate
import kotlin.Exception

class InMemoryDatabase(
    var assumptions: ConfigurationExcel, val participants: Map<String, FinalParticipant>,
    val interestMap: Map<Int, Double>
) {
    private val participantDB = participants.toMutableMap()
    private val mutableInterestMap = interestMap.toMutableMap()

    private val accountDB = mutableListOf<Account>()

    fun migration() {
        initialMigration(participantDB)
    }

    fun getAccountForAccessTokenOrNull(token: String): Account? {
        return accountDB.firstOrNull { it.accessToken == token }
    }

    fun writeAccounts(accounts: List<Account>) {
        accountDB.addAll(accounts)
    }

    fun replaceParticipant(finalParticipant: FinalParticipant, id:String? = null) {
        val key = id ?: finalParticipant.id
        if (participantDB.containsKey(id)) participantDB.replace(key, finalParticipant)
        else participantDB.put(key, finalParticipant)
    }


    fun deleteParticipant(id: String) {
        participantDB.remove(id)
    }

    fun updateInterest(age: Int, interest: Double): Double {
        mutableInterestMap[age] = interest
        return interest
    }

    fun updateParticipant(id: String, gender: Gender, birthDate: String): FinalParticipant {
        val participant = participantDB.get(id) ?: throw Exception("no such element")
        val resultParticipant = FinalParticipant(participant.id, gender, participant.op, participant.ingOp, birthDate)
        participantDB.replace(id, resultParticipant)
        return resultParticipant
    }

    fun getParticipantNullable(id: String): FinalParticipant? {
        return participantDB.values.firstOrNull { participant -> participant.id == id }
    }

    fun getParticipant(id: String): FinalParticipant {
        return participantDB.values.firstOrNull { participant -> participant.id == id }
            ?: throw Exception("Unauthenticated")
    }


    fun getParticipants(): List<FinalParticipant> {
        return participantDB.values.toList()
    }

    companion object {
        fun getMortalityForCurrSex(assumptions: ConfigurationExcel = globalEnv.inMemoryDatabase.assumptions): Map<Int, Map<Int, Double>> {
            return if (assumptions.sex == Gender.Man) assumptions.maleMortality else assumptions.femaleMortality
        }
    }

    fun updateAssumptions(viewer: Account?, input: TransferAssumptions): String {
        val configurationExcel =
            ConfigurationExcel(
                LocalDate.parse(input.date),
                input.age,
                input.month,
                input.pensionYear,
                input.pensionMonth,
                Gender.valueOf(input.sex),
                assumptions.maleBeforeStart,
                assumptions.femaleBeforeStart,
                assumptions.maleAfterStart,
                assumptions.femaleAfterStart,
                assumptions.maleMortality,
                assumptions.femaleMortality
            )
        assumptions = configurationExcel
        return input.date
    }

    fun findByUsernameAndPassword(username: String, password: String): String? {
        return accountDB.firstOrNull { account -> account.username == username && account.password == password }?.accessToken
    }

}