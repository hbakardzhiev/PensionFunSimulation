package server

import commands.CommandFactory
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import queries.Queries
import resolver.MutationResolver
import resolver.QueryResolver

fun buildRuntimeWiring(queries: Queries, mutations: CommandFactory): RuntimeWiring {
    val queryResolver = QueryResolver(queries)
    val mutationResolver = MutationResolver(mutations)
    return RuntimeWiring.newRuntimeWiring()
        .Type("Query") {
            dataFetcher("getParticipant", queryResolver.participant)
            dataFetcher("viewer", queryResolver.participants)
            dataFetcher("getAssumptions", queryResolver.assumptions)
            dataFetcher("getInterestRate", queryResolver.interestRate)
            dataFetcher("getMortality", queryResolver.mortalityRate)
            dataFetcher("getAccessCodeForAccount", queryResolver.accessCode)
            dataFetcher("getEvents", queryResolver.events)
        }
        .Type("Mutation") {
            dataFetcher("removeParticipant", mutationResolver.removeParticipant)
            dataFetcher("updateAssumptions", mutationResolver.updateAssumption)
            dataFetcher("changeBirthdateCommand", mutationResolver.changeBirthdateCommand)
            dataFetcher("changeGenderCommand", mutationResolver.changeGenderCommand)
            dataFetcher("addPartnerCommand", mutationResolver.addPartnerCommand)
            dataFetcher("editRetirementAgeCommand", mutationResolver.editRetirementAgeCommand)
            dataFetcher("editCalculationDateCommand", mutationResolver.editCalculationDateCommand)
            dataFetcher("addPersonalInfoCommand", mutationResolver.addPersonalInfoCommand)
            dataFetcher("addAddressCommand", mutationResolver.addAddressCommand)
        }.build()
}

fun RuntimeWiring.Builder.Type(
    typeName: String,
    block: TypeRuntimeWiring.Builder.() -> TypeRuntimeWiring.Builder
): RuntimeWiring.Builder {

    return this.type(typeName) { typeWiring ->
        typeWiring.block()
    }
}