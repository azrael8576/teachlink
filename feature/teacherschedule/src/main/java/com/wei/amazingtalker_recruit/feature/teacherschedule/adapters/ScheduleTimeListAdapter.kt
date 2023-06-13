package com.wei.amazingtalker_recruit.feature.teacherschedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.network.AtDispatchers
import com.wei.amazingtalker_recruit.core.network.Dispatcher
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.ListHeaderScheduleTimeBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.ListItemHeaderScheduleTimeBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.ListItemScheduleTimeBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

interface OnItemClickListener {
    fun onItemClick(item: IntervalScheduleTimeSlot)
}

enum class ItemViewType {
    ITEM_HEADER, ITEM, HEADER
}

/**
 * Adapter for the [scheduleTimeRecyclerview] in [ScheduleFragment].
 */
class ScheduleTimeListAdapter @Inject constructor(
    @Dispatcher(AtDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(
        ScheduleTimeListDiffCallback()
    ) {

    private val computationScope = CoroutineScope(defaultDispatcher)
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    fun addHeaderAndSubmitList(list: List<IntervalScheduleTimeSlot>) {

        computationScope.launch {
            val items = listOf(DataItem.Header) + list.groupBy { it.duringDayType }.flatMap {
                listOf(DataItem.ItemHeader(it.key), *(it.value.map { item ->
                    DataItem.Item(item)
                }.toTypedArray()))
            }

            withContext(Dispatchers.Main) { //update in main ui thread
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemViewType.HEADER.ordinal -> {
                HeaderViewHolder(
                    ListHeaderScheduleTimeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ItemViewType.ITEM_HEADER.ordinal -> {
                ItemHeaderViewHolder(
                    ListItemHeaderScheduleTimeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                ScheduleTimeListViewHolder(
                    ListItemScheduleTimeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind()
            }

            is ItemHeaderViewHolder -> {
                holder.bind(data)
            }

            is ScheduleTimeListViewHolder -> {
                holder.bind(data as DataItem.Item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ItemViewType.HEADER.ordinal
            is DataItem.ItemHeader -> ItemViewType.ITEM_HEADER.ordinal
            else -> ItemViewType.ITEM.ordinal
        }
    }

    inner class HeaderViewHolder(
        private val binding: ListHeaderScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val currentTimezone = ZoneId.systemDefault()

        fun bind() {
            binding.apply {
                val offsetFormatter = DateTimeFormatter.ofPattern("xxx")
                textView.text = String.format(
                    binding.root.context.getString(R.string.your_local_time_zone),
                    currentTimezone,
                    offsetFormatter.format(OffsetDateTime.now(currentTimezone).offset)
                )
            }
        }
    }

    inner class ItemHeaderViewHolder(
        private val binding: ListItemHeaderScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem) {
            binding.apply {
                textView.text = when (item.duringDayType) {
                    DuringDayType.Morning -> binding.root.resources.getString(R.string.morning)
                    DuringDayType.Afternoon -> binding.root.resources.getString(R.string.afternoon)
                    DuringDayType.Evening -> binding.root.resources.getString(R.string.evening)
                    else -> binding.root.resources.getString(R.string.morning)
                }
            }
        }
    }

    inner class ScheduleTimeListViewHolder(
        private val binding: ListItemScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.Item) {
            binding.apply {
                val dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

                textScheduleLocalTime.text = dateTimeFormatter.format(item.start)
                textScheduleLocalTime.isEnabled = (item.state == ScheduleState.AVAILABLE)

                textScheduleLocalTime.setOnClickListener {
                    onItemClickListener?.onItemClick(item.intervalScheduleTimeSlot)
                }
            }
        }
    }
}

private class ScheduleTimeListDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem(val itemViewType: ItemViewType) {

    abstract val name: String?
    abstract val start: OffsetDateTime?
    abstract val end: OffsetDateTime?
    abstract val state: ScheduleState?
    abstract val duringDayType: DuringDayType?
    abstract val isHeader: Boolean

    object Header : DataItem(ItemViewType.HEADER) {
        override val name = null
        override val start = null
        override val end = null
        override val state = null
        override val duringDayType = null
        override val isHeader = true
    }

    data class ItemHeader(val duringType: DuringDayType) : DataItem(ItemViewType.ITEM_HEADER) {
        override val name = null
        override val start = null
        override val end = null
        override val state = null
        override val duringDayType = duringType
        override val isHeader = false
    }

    data class Item(val intervalScheduleTimeSlot: IntervalScheduleTimeSlot) : DataItem(
        ItemViewType.ITEM
    ) {
        override val name = null
        override val start = intervalScheduleTimeSlot.start
        override val end = intervalScheduleTimeSlot.end
        override val state = intervalScheduleTimeSlot.state
        override val duringDayType = intervalScheduleTimeSlot.duringDayType
        override val isHeader = false
    }
}