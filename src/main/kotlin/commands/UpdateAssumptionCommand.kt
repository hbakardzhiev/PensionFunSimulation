package commands

import environment.globalEnv
import models.Account
import models.TransferAssumptions
import java.lang.Exception

class UpdateAssumptionCommand(val viewer: Account?, private val transferAssumptions: TransferAssumptions): Command {
    override fun execute() {
        if (viewer == null)
            throw Exception("Unauthenticated")
        globalEnv.inMemoryDatabase.updateAssumptions(viewer, transferAssumptions)
    }
}