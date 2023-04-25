package com.wei.amazingtalker_recruit.feature.teacherschedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wei.amazingtalker_recruit.core.network.model.TeacherScheduleUnit
import com.wei.amazingtalker_recruit.core.network.model.DuringDayType
import com.wei.amazingtalker_recruit.core.network.model.ScheduleUnitState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.ListHeaderScheduleTimeBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.ListItemHeaderScheduleTimeBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.ListItemScheduleTimeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

enum class ItemViewType {
    ITEM_HEADER, ITEM, HEADER
}

/**
 * Adapter for the [scheduleTimeRecyclerview] in [ScheduleFragment].
 */
class ScheduleTimeListAdapter @Inject constructor(): ListAdapter<DataItem, RecyclerView.ViewHolder>(
    ScheduleTimeListDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Main)

    fun addHeaderAndSubmitList(list: List<TeacherScheduleUnit>) {

        adapterScope.launch {
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
                holder.bind(data)
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

    class HeaderViewHolder(
        private val binding: ListHeaderScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                val offsetFormatter = DateTimeFormatter.ofPattern("xxx")
                textView.text = String.format(
                    binding.root.context.getString(R.string.your_local_time_zone),
                    ZoneId.systemDefault(),
                    offsetFormatter.format(OffsetDateTime.now( ZoneId.systemDefault() ).offset)
                )
            }
        }
    }

    class ItemHeaderViewHolder(
        private val binding: ListItemHeaderScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem) {
            binding.apply {
                textView.text = when(item.duringDayType){
                    DuringDayType.Morning -> binding.root.resources.getString(R.string.morning)
                    DuringDayType.Afternoon -> binding.root.resources.getString(R.string.afternoon)
                    DuringDayType.Evening -> binding.root.resources.getString(R.string.evening)
                    else -> binding.root.resources.getString(R.string.morning)
                }
            }
        }
    }

    class ScheduleTimeListViewHolder(
        private val binding: ListItemScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem) {
            binding.apply {
                val dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

                textScheduleUnitLocalTime.text = dateTimeFormatter.format(item.start)
                textScheduleUnitLocalTime.isEnabled = (item.state == ScheduleUnitState.AVAILABLE)

                textScheduleUnitLocalTime.setOnClickListener {
                    //TODO
                    Toast.makeText(binding.root.context, "TODO: navTo ${item.toString()}", Toast.LENGTH_SHORT).show();
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
    abstract val state: ScheduleUnitState?
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

    data class Item(val amazingtalkerTeacherScheduleUnit: TeacherScheduleUnit) : DataItem(
        ItemViewType.ITEM
    ) {
        override val name = null
        override val start = amazingtalkerTeacherScheduleUnit.start
        override val end = amazingtalkerTeacherScheduleUnit.end
        override val state = amazingtalkerTeacherScheduleUnit.state
        override val duringDayType = amazingtalkerTeacherScheduleUnit.duringDayType
        override val isHeader = false
    }
}