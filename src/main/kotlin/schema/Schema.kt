package schema

import commands.CommandFactory
import commands.CommandFactoryImplementation
import graphql.schema.GraphQLSchema
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import queries.QueriesImplementation
import queries.Queries
import server.buildRuntimeWiring

private val schemaDef = """
    type Query {
        getParticipant(id: String!, timestamp: String): Participant
        viewer: [Participant!] 
        getAssumptions: Assumptions!
        getInterestRate(age: Int!, timestamp: String): Float!
        getMortality(age: Int!, year: Int!): Float!
        getAccessCodeForAccount(username: String!, password: String!): String
        getEvents: [Event!]
    }
    type Mutation {
        removeParticipant(id: String!): String
        updateAssumptions(age: Int!, month: Int!, date: String!, pensionYear: Int!, pensionMonth: Int!, sex: String!): String
        changeBirthdateCommand(id: String!, date: String!): String
        changeGenderCommand(id: String!, gender: String!): Gender
        addPartnerCommand(id: String!, date: String!, gender: String!): String
        editRetirementAgeCommand(id: String!, age: Int!, timestamp: String): String
        editCalculationDateCommand(id: String!, date: String!): String
        addPersonalInfoCommand(id: String!, employed: Boolean!, salary: Int!): String
        addAddressCommand(id: String!, streetName: String!, houseNumber: Int!, postalCode: String!, country: String!): String
    }
    type Assumptions {
        age: Int!
        month: Int!
        date: String!
        maleBeforeStart: Float!
        maleAfterStart: Float!
        femaleBeforeStart: Float!
        femaleAfterStart: Float!
        pensionYear: Int!
        pensionMonth: Int!
        sex: String!
    }
    type Event {
        id: String!,
        name: String!,
        appliesAt: String!
    }
    type Participant {
        id: String!
        gender: Gender!
        birthDate: String!
        op: Float!
        ingOp: Float!
        employed: Boolean
        yearlySalary: Int
        streetName: String
        houseNumber: Int
        postalCode: String
        country: String
    }
    enum Gender {
        Man,
        Vrouw
    }
""".trimIndent()


fun schema(
    queries: Queries = QueriesImplementation(),
    mutations: CommandFactory = CommandFactoryImplementation()
): GraphQLSchema {
    val schemaParser = SchemaParser()
    val schemaGenerator = SchemaGenerator()

    val typeRegistry = schemaParser.parse(schemaDef)
    val wiring = buildRuntimeWiring(queries, mutations)
    val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring)

    return graphQLSchema
}