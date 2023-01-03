package com.alex.amazingtalker_recruit_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerTeacherSchedule
import com.alex.amazingtalker_recruit_android.databinding.ListItemScheduleTimeBinding

/**
 * Adapter for the [scheduleTimeRecyclerview] in [ScheduleFragment].
 */
class ScheduleTimeListAdapter : ListAdapter<AmazingtalkerTeacherSchedule, RecyclerView.ViewHolder>(ScheduleTimeListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleTimeListViewHolder(
            ListItemScheduleTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        (holder as ScheduleTimeListViewHolder).bind(data)
    }

    class ScheduleTimeListViewHolder(
        private val binding: ListItemScheduleTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AmazingtalkerTeacherSchedule) {
            binding.apply {
                hahowClassCategory.text = item.start.toString()
            }
        }
    }
}

private class ScheduleTimeListDiffCallback : DiffUtil.ItemCallback<AmazingtalkerTeacherSchedule>() {

    override fun areItemsTheSame(
        oldItem: AmazingtalkerTeacherSchedule,
        newItem: AmazingtalkerTeacherSchedule
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: AmazingtalkerTeacherSchedule,
        newItem: AmazingtalkerTeacherSchedule
    ): Boolean {
        return oldItem == newItem
    }
}