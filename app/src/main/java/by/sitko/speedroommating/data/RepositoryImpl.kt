package by.sitko.speedroommating.data

import by.sitko.speedroommating.domain.EventDomainItem
import by.sitko.speedroommating.domain.Repository
import com.quiz.users.api.ApiInterface

class RepositoryImpl(
    private val api: ApiInterface
) : Repository {

    private val localCache = mutableListOf<EventItem>()

    override suspend fun loadData(): List<EventDomainItem> {

        if (localCache.isNotEmpty()) {
            return DataToDomainEventMapper.map(localCache)
        }

        val items = api.getEvents()

        localCache.addAll(
            items
                .asSequence()
                .filterNot { it.cost.isNullOrEmpty() }
                .filterNot { it.location.isNullOrEmpty() }
                .filterNot { it.phone_number.isNullOrEmpty() }
                .filterNot { it.start_time.isNullOrEmpty() }
                .filterNot { it.end_time.isNullOrEmpty() }
                .filterNot { it.image_url.isNullOrEmpty() }
                .filterNot { it.venue.isNullOrEmpty() }
                .toList()
        )

        return DataToDomainEventMapper.map(localCache)
    }
}