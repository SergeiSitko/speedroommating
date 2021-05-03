package by.sitko.speedroommating.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import by.sitko.speedroommating.MainCoroutineRule
import by.sitko.speedroommating.wheneverBlocking
import com.quiz.users.api.ApiInterface
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

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var repository: RepositoryImpl

    @Mock
    private lateinit var api: ApiInterface

    private val testDispatcher = TestCoroutineDispatcher()

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should be empty`() {
        runBlocking {
            wheneverBlocking { api.getEvents() }.thenReturn(emptyList())

            val allEvents = repository.loadData()

            assert(allEvents.isEmpty())
        }
    }

    @Test
    fun `should be empty if api has null data`() {
        runBlocking {
            val event = EventItem()

            wheneverBlocking { api.getEvents() }.thenReturn(listOf(event))

            val allEvents = repository.loadData()

            assert(allEvents.isEmpty())
        }
    }

    @Test
    fun `should be not empty if api returns valid event`() {
        runBlocking {
            val event = EventItem(
                cost = "free",
                start_time = "2021-06-20T23:14:54Z",
                end_time = "2021-06-20T23:14:54Z",
                image_url = "url",
                phone_number = "+3545",
                location = "location",
                venue = "venue"
            )

            wheneverBlocking { api.getEvents() }.thenReturn(listOf(event))

            val allEvents = repository.loadData()

            assert(allEvents.isNotEmpty())
            assert(allEvents.size == 1)
        }
    }
}