package by.sitko.speedroommating.domain

import by.sitko.speedroommating.domain.EventDomainItem

interface Repository {
    suspend fun loadData(): List<EventDomainItem>
}