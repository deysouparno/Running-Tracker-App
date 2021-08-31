package com.example.rnunningtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rnunningtracker.R
import com.example.rnunningtracker.databinding.ItemRunBinding
import com.example.rnunningtracker.databse.Run
import com.example.rnunningtracker.utils.TrackingUtility
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    class RunViewHolder(val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(run: Run) {
                Glide.with(binding.ivRunImage.context)
                    .load(run.img)
                    .into(binding.ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.mm.yy", Locale.getDefault())

            binding.apply {
                tvDate.text = dateFormat.format(calendar.time)
                tvAvgSpeed.text = "${run.avgSpeedInKmh} km/h"
                tvDistance.text = "${run.distanceInM / 1000f} km"
                tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
                tvCalories.text = "${run.caloriesBurnt} kcal"
            }

        }

        companion object {
            fun form(parent: ViewGroup): RunViewHolder {
                val binding = ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RunViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder.form(parent)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) {
        differ.submitList(list)
    }
}