package environment

import events.*

data class EventProcessor (
    val updaters: List<Updater>,
    val store: EventStore = InMemoryEventStore()
) {

    fun process(event: PolicyEvent) {
        store.writeEvent(event)
        updaters.forEach { it.update(event) }
    }
}