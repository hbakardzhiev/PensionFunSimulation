import assertk.Assert
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import environment.InMemoryDatabase
import environment.Env
import environment.EventProcessor
import events.PolicyUpdater
import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import models.*
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.time.Clock
import java.time.LocalDate

val file = File("files/benchmark.xlsm")
val workbook = WorkbookFactory.create(file)

val assumptions = ConfigurationExcel(
    LocalDate.of(2020, 1, 1), 5, 5, 5, 5, Gender.Vrouw, 5.0, 5.0, 5.0, 5.0, mutableMapOf(
        2 to
                mapOf(5 to 5.0)
    ), mutableMapOf(
        2 to
                mapOf(5 to 5.0)
    )
)
val date = LocalDate.parse("2020-01-01")
val testEnv = Env(
    InMemoryDatabase(
        assumptions,
        mapOf(
            "2" to FinalParticipant("2", Gender.Vrouw, 1.0, 2.0, date.toString()),
            "1" to FinalParticipant("1", Gender.Vrouw, 3.0, 4.0, date.toString())
        ), mapOf(1 to 0.03)
    ),
    eventProcessor = EventProcessor(mutableListOf()),
    clock = Clock.systemDefaultZone()
)

val interest = mapOf(
    1 to 0.03,
    2 to 0.03,
    3 to 0.035,
    4 to 0.03,
    5 to 0.03,
    6 to 0.03
)

val configurationExcel = ConfigurationExcel(
    LocalDate.of(2020, 1, 1), 67, 4, 67, 5,
    Gender.Man, 5.0, 5.0, 5.0, 5.0, mapOf(
        3 to mapOf(3 to 3.0),
        4 to mapOf(3 to 4.0)
    ),
    mapOf(
        3 to mapOf(3 to 3.0),
        4 to mapOf(3 to 4.0)
    )
)

val maturityFloor = mapOf(
    LocalDate.of(2000, 1, 3) to 0.035,
    LocalDate.of(2000, 1, 4) to 0.035
)

val maturityCeil = mapOf(
    LocalDate.of(2000, 1, 3) to 0.03,
    LocalDate.of(2000, 1, 4) to 0.035
)

fun createAccount() = Context(Account(username = "hristo", password = "pass", accessToken = "h"))

public fun genID(schema: GraphQLSchema): String {
    val getQueries = """
                query {
                    viewer {
                        id
                    }
                }
            """.trimIndent()
    val queryResult = schema.performQuery(getQueries).toSpecification()
    val ids = queryResult["data"] as HashMap<*, ArrayList<HashMap<String, String>>>
    val id = ids.values.first()[0].values.first()
    return id
}

val testParticipant = FinalParticipant("a", Gender.Man, 0.0, 0.0, "01-08-2000", 0, "01-01-2000", false, 0, "", 9, "a")
val participants = mapOf(
    "2" to FinalParticipant("2", Gender.Vrouw, 1.0, 2.0, date.toString()),
    "1" to FinalParticipant("1", Gender.Vrouw, 3.0, 4.0, date.toString())
)
fun createTestEnv(): Env {
    return Env(
        inMemoryDatabase = InMemoryDatabase(
            assumptions = assumptions,
            participants = participants,
            interestMap = mapOf(1 to 0.03)
        ),
        eventProcessor = EventProcessor(mutableListOf(PolicyUpdater())),
        clock = Clock.systemDefaultZone()
    )
}

fun GraphQLSchema.performQuery(
    query: String,
    variables: Map<String, Any?> = mapOf("access_code" to "h"),
    operationName: String? = null,
    viewer: Context = createAccount()
): ExecutionResult {
    val input =
        ExecutionInput.newExecutionInput().operationName(operationName).variables(variables).context(viewer)
            .query(query)
            .build()
    return GraphQL.newGraphQL(this).build().execute(input)
}

fun Assert<ExecutionResult>.hasGraphQLResult(output: Map<String, Any?>) {
    transform { result ->
        assertThat(result.errors).isEmpty()

        assertThat(result.getData() as Map<String, Any?>).isEqualTo(output)
    }
}