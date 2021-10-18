import environment.Env
import environment.EventProcessor
import environment.InMemoryDatabase
import environment.globalEnv
import events.InMemoryEventStore
import events.PolicyUpdater
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import ktor.graphql.config
import ktor.graphql.graphQL
import models.Account
import models.Context
import org.apache.poi.ss.usermodel.WorkbookFactory
import schema.schema
import java.io.File
import java.time.Clock

val testAccount = Account("hristo", "pass", "h")

fun main(args: Array<String>) {

    val file = File("files/benchmark.xlsm")
    val workbook = WorkbookFactory.create(file)
    val interestWorksheet = workbook.getSheet("Interest rates").parseInterest()
    val mortalityM = workbook.getSheet("Collectief 2013 M").mortalityRateMaker()
    val mortalityV = workbook.getSheet("Collectief 2013 V").mortalityRateMaker()
    val configuration = workbook.getSheet("Configuratie").parseConfiguration(
        mortalityMale = mortalityM,
        mortalityFemale = mortalityV
    )

    val kasstromen = kastromen(configuration, interestWorksheet)

    val inMemoryDatabase = InMemoryDatabase(configuration, kasstromen, interestWorksheet)
    inMemoryDatabase.writeAccounts(listOf(testAccount))

    globalEnv = Env(
        inMemoryDatabase = inMemoryDatabase,
        eventProcessor = EventProcessor(updaters = mutableListOf(PolicyUpdater()), store = InMemoryEventStore()),
        clock = Clock.systemDefaultZone()
    )

    globalEnv.inMemoryDatabase.migration()

    val schema = schema()
    val module = createServer(schema)
    val server = embeddedServer(
        factory = Netty,
        port = 8080,
        module = module
    )
    println("Start")
    server.start(wait = true)
}

fun createServer(schema: GraphQLSchema): Application.() -> Unit {
    val graphQlExecutor = GraphQL.newGraphQL(schema).build()

    return fun Application.() {
        install(CORS) {
            anyHost()
            header("Authorization")
        }

        routing {
            intercept(ApplicationCallPipeline.Call) {
                setViewer(globalEnv.inMemoryDatabase)
            }
            graphQL("/graphql", schema) { request ->
                config {
                    graphiql = true
                    context = getContext(call)
                }
            }
        }
    }
}

val userKey = AttributeKey<Account>("account")

fun getContext(call: ApplicationCall): Context {

    var viewer: Account? = null
    if (call.attributes.contains(userKey)) {
        viewer = call.attributes[userKey]
    }
    return Context(viewer)
}

fun PipelineContext<Unit, ApplicationCall>.setViewer(database: InMemoryDatabase) {

    val token = call.request.queryParameters["access_token"]
    if (token != null) {
        val account = database.getAccountForAccessTokenOrNull(token)

        if (account != null) {
            call.attributes.put(userKey, account)
        }
    }
}