package resolver

import commands.CommandFactory
import graphql.schema.DataFetcher
import models.*
import java.time.Instant
import java.time.LocalDate

class MutationResolver(mutations: CommandFactory) {

    val removeParticipant: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")

        mutations.removeParticipant(context.account, id).execute()
        id
    }

    val updateAssumption: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val age = env.getArgument<Int>("age")
        val month = env.getArgument<Int>("month")
        val date = env.getArgument<String>("date")
        val pensionYear = env.getArgument<Int>("pensionYear")
        val pensionMonth = env.getArgument<Int>("pensionMonth")
        val sex = env.getArgument<String>("sex")
        val transportAssumption = TransferAssumptions(age, month, date, pensionYear, pensionMonth, sex)

        mutations.updateAssumptionCommand(context.account, transportAssumption).execute()
        transportAssumption.date
    }

    val changeBirthdateCommand: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val birthDate = LocalDate.parse(env.getArgument<String>("date"))
        val timestampAsText = env.arguments["timestamp"] as String?
        val timestamp = if (timestampAsText == null) null else Instant.parse(timestampAsText)

        mutations.changeBirthdateCommand(context.account, id, birthDate, timestamp).execute()
        birthDate.toString()
    }

    val changeGenderCommand: DataFetcher<Gender> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val gender = Gender.valueOf(env.getArgument<String>("gender"))

        mutations.changeGenderCommand(context.account, id, gender).execute()
        gender
    }

    val addPartnerCommand: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val date = LocalDate.parse(env.getArgument<String>("date"))
        val genderPartner = Gender.valueOf(env.getArgument<String>("gender"))

        mutations.addPartnerCommand(context.account, InputParticipant(id, date, genderPartner)).execute()
        id
    }

    val editRetirementAgeCommand: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val age = env.getArgument<Int>("age")

        mutations.editRetirementAgeCommand(context.account, id, age).execute()
        id
    }

    val editCalculationDateCommand: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val date = LocalDate.parse(env.getArgument<String>("date"))

        mutations.editCalculationDateCommand(context.account, id, date).execute()
        id
    }

    val addPersonalInfoCommand: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val employed = env.getArgument<Boolean>("employed")
        val yearlySalary = env.getArgument<Int>("salary")

        val personalInfoInput = PersonalInfoInput(id, employed, yearlySalary)

        mutations.addPersonalInfoCommand(context.account, personalInfoInput).execute()
        id
    }

    val addAddressCommand: DataFetcher<String> = DataFetcher { env ->
        val context = env.getContext<Context>()
        val id = env.getArgument<String>("id")
        val streetName = env.getArgument<String>("streetName")
        val houseNumber = env.getArgument<Int>("houseNumber")
        val postalCode = env.getArgument<String>("postalCode")
        val country = env.getArgument<String>("country")

//        mutations.addAddressCommand(context.account, id, streetName, houseNumber, postalCode, country).execute()
        id
    }
}