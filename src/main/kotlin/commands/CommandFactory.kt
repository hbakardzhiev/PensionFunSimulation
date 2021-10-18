package commands

import models.*
import java.time.Instant
import java.time.LocalDate

interface CommandFactory {
    fun changeBirthdateCommand(viewer: Account?, clientID: String, birthdate: LocalDate, timestamp: Instant?): Command
    fun changeGenderCommand(viewer: Account?, clientID: String, gender: Gender): Command
    fun addPartnerCommand(viewer: Account?, inputParticipant: InputParticipant): Command
    fun editRetirementAgeCommand(viewer: Account?, clientID: String, newRetirementAge: Int): Command
    fun editCalculationDateCommand(viewer: Account?, clientID: String, newDate: LocalDate): Command
    fun addPersonalInfoCommand(viewer: Account?, personalInfoInput: PersonalInfoInput): Command
    fun removeParticipant(account: Account?, id: String): Command
    fun updateAssumptionCommand(viewer: Account?, transferAssumptions: TransferAssumptions): Command
}