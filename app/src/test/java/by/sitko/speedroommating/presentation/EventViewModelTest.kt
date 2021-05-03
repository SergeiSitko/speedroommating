package by.sitko.speedroommating.presentation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import by.sitko.speedroommating.MainCoroutineRule
import by.sitko.speedroommating.domain.EventDomainItem
import by.sitko.speedroommating.domain.LoadEventsUseCase
import by.sitko.speedroommating.presentation.managers.NetworkManger
import by.sitko.speedroommating.wheneverBlocking
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.*

@ExperimentalCoroutinesApi
class EventViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: EventViewModel

    @Mock
    private lateinit var loadEventsUseCase: LoadEventsUseCase

    @Mock
    private lateinit var networkManger: NetworkManger

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var dataObserver: Observer<State<List<EventViewItem>>>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should use case not trigger`() {
        whenever(networkManger.isNetworkAvailable()).thenReturn(false)

        verifyZeroInteractions(loadEventsUseCase)
    }

    @Test
    fun `should use case load upcoming events trigger`() {
        runBlocking {
            whenever(networkManger.isNetworkAvailable()).thenReturn(true)

            viewModel.loadUpcomingEvents()

            verify(loadEventsUseCase, times(1)).loadUpcomingEvents()
        }
    }

    @Test
    fun `should use case load past events trigger`() {
        runBlocking {
            whenever(networkManger.isNetworkAvailable()).thenReturn(true)

            viewModel.loadPastEvents()

            verify(loadEventsUseCase, times(1)).loadPastEvents()
        }
    }

    @Test
    fun `should state be empty for upcoming`() {
        whenever(networkManger.isNetworkAvailable()).thenReturn(true)
        wheneverBlocking { loadEventsUseCase.loadUpcomingEvents() }.thenReturn(emptyList())

        viewModel.data.observeForever(dataObserver)

        viewModel.loadUpcomingEvents()

        assert(viewModel.data.value == State.Empty)

        viewModel.data.removeObserver(dataObserver)
    }

    @Test
    fun `should state be empty for past`() {
        whenever(networkManger.isNetworkAvailable()).thenReturn(true)
        wheneverBlocking { loadEventsUseCase.loadPastEvents() }.thenReturn(emptyList())

        viewModel.data.observeForever(dataObserver)

        viewModel.loadPastEvents()

        assert(viewModel.data.value == State.Empty)

        viewModel.data.removeObserver(dataObserver)
    }

    @Test
    fun `should state be content for upcoming`() {
        whenever(networkManger.isNetworkAvailable()).thenReturn(true)
        val now = Calendar.getInstance().time
        val eventDomainItem = EventDomainItem("1", now, "url1", "Berlin", "+123456789", now, "venue1")
        wheneverBlocking { loadEventsUseCase.loadUpcomingEvents() }.thenReturn(listOf(eventDomainItem))

        val eventViewItem = DomainToViewEventMapper.map(eventDomainItem)

        viewModel.data.observeForever(dataObserver)

        viewModel.loadUpcomingEvents()

        verify(dataObserver).onChanged(State.Content(listOf(eventViewItem)))
        viewModel.data.removeObserver(dataObserver)
    }

    @Test
    fun `should state be content for past`() {
        whenever(networkManger.isNetworkAvailable()).thenReturn(true)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -2)

        val eventDomainItem = EventDomainItem("1", calendar.time, "url1", "Berlin", "+123456789", calendar.time, "venue1")
        wheneverBlocking { loadEventsUseCase.loadPastEvents() }.thenReturn(listOf(eventDomainItem))

        val eventViewItem = DomainToViewEventMapper.map(eventDomainItem)

        viewModel.data.observeForever(dataObserver)

        viewModel.loadPastEvents()

        verify(dataObserver).onChanged(State.Content(listOf(eventViewItem)))
        viewModel.data.removeObserver(dataObserver)
    }

    @Test
    fun `should state be error`() {
        whenever(networkManger.isNetworkAvailable()).thenReturn(true)
        val runtimeException = RuntimeException("failed")

        runBlocking {
            doThrow(runtimeException).`when`(loadEventsUseCase).loadUpcomingEvents()
        }

        viewModel.data.observeForever(dataObserver)

        viewModel.loadUpcomingEvents()

        verify(dataObserver).onChanged(State.Error(runtimeException))
        viewModel.data.removeObserver(dataObserver)
    }
}