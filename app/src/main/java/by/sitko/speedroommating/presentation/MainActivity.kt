package by.sitko.speedroommating.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.sitko.speedroommating.R
import by.sitko.speedroommating.presentation.utils.VerticalSpaceItemDecoration
import by.sitko.speedroommating.presentation.utils.dp
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_no_connection.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<EventViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = EventAdapter()
        events_list.addItemDecoration(VerticalSpaceItemDecoration(48.dp))
        events_list.adapter = adapter

        retry_button.setOnClickListener { loadEvents(tabLayout.selectedTabPosition) }
        error_retry_button.setOnClickListener { loadEvents(tabLayout.selectedTabPosition) }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                loadEvents(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        viewModel.data.observe(this, { state: State<List<EventViewItem>> ->
            when (state) {
                is State.Loading -> {
                    events_list.isVisible = false

                    empty_state.isVisible = false

                    error_state.isVisible = false

                    no_connection_state.isVisible = false

                    loading.isVisible = true
                }
                is State.Error -> {
                    events_list.isVisible = false

                    empty_state.isVisible = false

                    error_state.isVisible = true

                    no_connection_state.isVisible = false

                    loading.isVisible = true
                }
                is State.NoConnection -> {
                    events_list.isVisible = false

                    empty_state.isVisible = false

                    error_state.isVisible = false

                    no_connection_state.isVisible = true

                    loading.isVisible = true

                }
                is State.Content -> {
                    events_list.isVisible = true

                    adapter.updateEvents(state.content)

                    empty_state.isVisible = false

                    error_state.isVisible = false

                    no_connection_state.isVisible = false

                    loading.isVisible = false
                }
                is State.Empty -> {
                    events_list.isVisible = false

                    empty_state.isVisible = true

                    error_state.isVisible = false

                    no_connection_state.isVisible = false

                    loading.isVisible = true
                }
            }
        })

        loadEvents(tabLayout.selectedTabPosition)
    }

    private fun loadEvents(tabIndex: Int) {
        if (tabIndex == 0) {
            viewModel.loadUpcomingEvents()
        } else {
            viewModel.loadPastEvents()
        }
    }
}