package commands

import models.*
import java.time.Instant
import java.time.LocalDate

open class CommandFactoryImplementation: CommandFactory {


    override fun changeBirthdateCommand(viewer: Account?, clientID: String, birthdate: LocalDate, timestamp: Instant?): Command {
        return ChangeBirthdateCommand(viewer, clientID, birthdate)
    }

    override fun changeGenderCommand(viewer: Account?, clientID: String, gender: Gender): Command {
        return ChangeGenderCommand(viewer, clientID, gender)
    }

    override fun addPartnerCommand(
        viewer: Account?,
        inputParticipant: InputParticipant
    ): Command {
        return AddPartnerCommand(viewer, inputParticipant)
    }

    override fun editRetirementAgeCommand(viewer: Account?, clientID: String, newRetirementAge: Int): Command {
        return EditRetirementAgeCommand(viewer, clientID, newRetirementAge)
    }

    override fun editCalculationDateCommand(viewer: Account?, clientID: String, newDate: LocalDate): Command {
        return EditCalculationDateCommand(viewer, clientID, newDate)
    }

    override fun addPersonalInfoCommand(
        viewer: Account?,
        personalInfoInput: PersonalInfoInput
    ): Command {
        return AddPersonalInfoCommand(viewer, personalInfoInput)
    }


    override fun removeParticipant(account: Account?, id: String): Command {
        return RemoveParticipantCommand(account, id)
    }

    override fun updateAssumptionCommand(viewer: Account?, transferAssumptions: TransferAssumptions): Command {
        return UpdateAssumptionCommand(viewer, transferAssumptions)
    }

}