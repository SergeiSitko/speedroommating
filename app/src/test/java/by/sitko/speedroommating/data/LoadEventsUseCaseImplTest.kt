package by.sitko.speedroommating.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import by.sitko.speedroommating.MainCoroutineRule
import by.sitko.speedroommating.domain.EventDomainItem
import by.sitko.speedroommating.domain.Repository
import by.sitko.speedroommating.wheneverBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.*

@ExperimentalCoroutinesApi
class LoadEventsUseCaseImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var useCase: LoadEventsUseCaseImpl

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: Repository

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should upcoming be empty`() {
        wheneverBlocking { repository.loadData() }.thenReturn(emptyList())

        runBlocking {
            val allEvents = useCase.loadUpcomingEvents()

            assert(allEvents.isEmpty())
        }
    }

    @Test
    fun `should past be empty`() {
        wheneverBlocking { repository.loadData() }.thenReturn(emptyList())

        runBlocking {
            val allEvents = useCase.loadPastEvents()

            assert(allEvents.isEmpty())
        }
    }

    @Test
    fun `should upcoming be not empty`() {
        val date = Calendar.getInstance().time
        val eventDomainItem = EventDomainItem("1", date, "url1", "Berlin", "+123456789", date, "venue1")
        wheneverBlocking { repository.loadData() }.thenReturn(listOf(eventDomainItem))

        runBlocking {
            val allEvents = useCase.loadUpcomingEvents()

            assert(allEvents.isNotEmpty())
        }
    }

    @Test
    fun `should past be not empty`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -2)

        val eventDomainItem = EventDomainItem("1", calendar.time, "url1", "Berlin", "+123456789", calendar.time, "venue1")
        wheneverBlocking { repository.loadData() }.thenReturn(listOf(eventDomainItem))

        runBlocking {
            val allEvents = useCase.loadPastEvents()

            assert(allEvents.isNotEmpty())
        }
    }

    @Test
    fun `should upcoming be sorted by start date`() {
        val calendar = Calendar.getInstance()
        val firstDate = calendar.time
        val event1 = EventDomainItem("1", firstDate, "url1", "Berlin", "+123456789", firstDate, "venue1")

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val secondDate = calendar.time
        val event2 = EventDomainItem("1", secondDate, "url1", "Berlin", "+123456789", secondDate, "venue1")

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val thirdDate = calendar.time
        val event3 = EventDomainItem("1", thirdDate, "url1", "Berlin", "+123456789", thirdDate, "venue1")

        wheneverBlocking { repository.loadData() }.thenReturn(listOf(event2, event1, event3))

        runBlocking {
            val upcomingEvents = useCase.loadUpcomingEvents()

            assert(upcomingEvents.size == 3)
            assert(upcomingEvents[0].startTime == firstDate)
            assert(upcomingEvents[1].startTime == secondDate)
            assert(upcomingEvents[2].startTime == thirdDate)
        }
    }

    @Test
    fun `should past be sorted by start date by descending`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val firstDate = calendar.time
        val event1 = EventDomainItem("1", firstDate, "url1", "Berlin", "+123456789", firstDate, "venue1")

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val secondDate = calendar.time
        val event2 = EventDomainItem("1", secondDate, "url1", "Berlin", "+123456789", secondDate, "venue1")

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val thirdDate = calendar.time
        val event3 = EventDomainItem("1", thirdDate, "url1", "Berlin", "+123456789", thirdDate, "venue1")

        wheneverBlocking { repository.loadData() }.thenReturn(listOf(event2, event1, event3))

        runBlocking {
            val upcomingEvents = useCase.loadPastEvents()

            assert(upcomingEvents.size == 3)
            assert(upcomingEvents[0].startTime == firstDate)
            assert(upcomingEvents[1].startTime == secondDate)
            assert(upcomingEvents[2].startTime == thirdDate)
        }
    }
}