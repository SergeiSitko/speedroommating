@file:Suppress("BlockingMethodInNonBlockingContext")

package by.sitko.speedroommating.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.sitko.speedroommating.domain.LoadEventsUseCase
import by.sitko.speedroommating.presentation.managers.NetworkManger
import kotlinx.coroutines.launch
import timber.log.Timber

class EventViewModel(
    private val loadEventsUseCase: LoadEventsUseCase,
    private val networkManger: NetworkManger
) : ViewModel() {

    private val comingEvents = mutableListOf<EventViewItem>()
    private val pastEvents = mutableListOf<EventViewItem>()

    private val _data = MutableLiveData<State<List<EventViewItem>>>()
    val data: LiveData<State<List<EventViewItem>>> = _data

    init {
        _data.value = State.Empty
    }

//    fun loadEvents() {
//        if (networkManger.isNetworkAvailable().not()) {
//
//            _data.value = State.NoConnection
//            return
//        }
//
//        _data.value = State.Loading
//        viewModelScope.launch {
//            try {
//                val domainItems = loadEventsUseCase.loadEvents()
//                val viewEvents = DomainToViewEventMapper.map(domainItems)
//
//                val state = if (viewEvents.isEmpty()) State.Empty else State.Content(viewEvents)
//
//                _data.value = state
//            } catch (e: Exception) {
//                Timber.e(e)
//                _data.value = State.Error(e)
//            }
//        }
//    }

    fun loadUpcomingEvents() {
        if (networkManger.isNetworkAvailable().not()) {

            _data.value = State.NoConnection
            return
        }

        if (comingEvents.isNotEmpty()) {
            _data.value = State.Content(comingEvents)
        }

        _data.value = State.Loading
        viewModelScope.launch {
            try {
                val domainItems = loadEventsUseCase.loadUpcomingEvents()
                val viewEvents = DomainToViewEventMapper.map(domainItems)

                comingEvents.clear()
                comingEvents.addAll(viewEvents)

                val state = if (comingEvents.isEmpty()) State.Empty else State.Content(comingEvents)

                _data.value = state
            } catch (e: Exception) {
                Timber.e(e)
                _data.value = State.Error(e)
            }
        }
    }

    fun loadPastEvents() {
        if (networkManger.isNetworkAvailable().not()) {

            _data.value = State.NoConnection
            return
        }

        if (pastEvents.isNotEmpty()) {
            _data.value = State.Content(pastEvents)
        }

        _data.value = State.Loading
        viewModelScope.launch {
            try {
                val domainItems = loadEventsUseCase.loadPastEvents()
                val viewEvents = DomainToViewEventMapper.map(domainItems)

                pastEvents.clear()
                pastEvents.addAll(viewEvents)

                val state = if (pastEvents.isEmpty()) State.Empty else State.Content(pastEvents)

                _data.value = state
            } catch (e: Exception) {
                Timber.e(e)
                _data.value = State.Error(e)
            }
        }
    }
}