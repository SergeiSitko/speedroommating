package by.sitko.speedroommating.domain

interface LoadEventsUseCase {
    suspend fun loadUpcomingEvents(): List<EventDomainItem>

    suspend fun loadPastEvents(): List<EventDomainItem>
}