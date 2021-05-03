package by.sitko.speedroommating.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.sitko.speedroommating.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_event_item.view.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val events = mutableListOf<EventViewItem>()

    fun updateEvents(newViewItems: List<EventViewItem>) {
        val diffCallback = EventDiffUtilsCallBack(events, newViewItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        events.clear()
        events.addAll(newViewItems)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_event_item, parent, false))

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewItem: EventViewItem) {
            with(itemView) {
                cost_label.text = viewItem.cost
                location_label.text = viewItem.location
                venue_label.text = viewItem.venue
                date_label.text = viewItem.date
                duration_label.text = viewItem.duration

                Glide.with(context)
                    .load(viewItem.imageUrl)
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_camera))
                    .into(root_image)
            }
        }
    }
}

class EventDiffUtilsCallBack(
    private val oldViewItems: List<EventViewItem>,
    private val newViewItems: List<EventViewItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldViewItems.size
    override fun getNewListSize() = newViewItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldViewItems[oldItemPosition] == newViewItems[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldViewItems[oldItemPosition] == newViewItems[newItemPosition]
}
