package environment

import java.time.Clock
import java.util.*

lateinit var globalEnv: Env

data class Env(
    val inMemoryDatabase: InMemoryDatabase,
    val eventProcessor: EventProcessor,
    val clock: Clock,
    val generatedID: String = generateUUID(random = Random())
)

fun generateUUID(random: Random = Random()):  String{
    return random.nextInt().toString()
}


