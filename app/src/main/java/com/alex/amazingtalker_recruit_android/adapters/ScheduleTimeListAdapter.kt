package com.alex.amazingtalker_recruit_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alex.amazingtalker_recruit_android.data.AmazingtalkerTeacherScheduleUnit
import com.alex.amazingtalker_recruit_android.data.ScheduleUnitState
import com.alex.amazingtalker_recruit_android.databinding.ListItemScheduleTimeBinding

/**
 * Adapter for the [scheduleTimeRecyclerview] in [ScheduleFragment].
 */
class ScheduleTimeListAdapter : ListAdapter<AmazingtalkerTeacherScheduleUnit, RecyclerView.ViewHolder>(ScheduleTimeListDiffCallback()) {
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

        fun bind(item: AmazingtalkerTeacherScheduleUnit) {
            binding.apply {
                textScheduleUnitLocalTime.text = item.start.toString() + item.state
                textScheduleUnitLocalTime.isEnabled = (item.state == ScheduleUnitState.AVAILABLE)

                textScheduleUnitLocalTime.setOnClickListener {
                    //TODO
                    Toast.makeText(binding.root.context, "TODO: navTo ${item.toString()}", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

private class ScheduleTimeListDiffCallback : DiffUtil.ItemCallback<AmazingtalkerTeacherScheduleUnit>() {

    override fun areItemsTheSame(
        oldItem: AmazingtalkerTeacherScheduleUnit,
        newItem: AmazingtalkerTeacherScheduleUnit
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: AmazingtalkerTeacherScheduleUnit,
        newItem: AmazingtalkerTeacherScheduleUnit
    ): Boolean {
        return oldItem == newItem
    }
}