package com.dicoding.dicodingevents.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dicodingevents.databinding.ActiveHomeLayoutBinding
import com.dicoding.dicodingevents.databinding.CompletedHomeLayoutBinding
import com.dicoding.dicodingevents.database.EventEntity
import com.dicoding.dicodingevents.ui.detail.EventDetail
import com.squareup.picasso.Picasso

class EventAdapter(
    private val typeView : Int?,
) : ListAdapter<EventEntity, RecyclerView.ViewHolder>(
    DIFF_CALLBACK) {
    inner class ViewHolder(private val binding : CompletedHomeLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : EventEntity) {

            binding.dataTitle.text = data.name
            Picasso.get()
                .load(data.imageLogo)
                .into(binding.imgItemPhoto)
            binding.cityName.text = data.cityName
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, EventDetail::class.java)
                intent.putExtra("EXTRA_EVENT", data)
                binding.root.context.startActivity(intent)
            }
        }
    }

    inner class HomeActiveViewHolder(private val binding : ActiveHomeLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(event : EventEntity){

            binding.activeEventTitle.text = event.name
            binding.activeEventOverview.text = event.summary
            Picasso.get()
                .load(event.mediaCover)
                .into(binding.activeEventImage)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, EventDetail::class.java)
                intent.putExtra("EXTRA_EVENT", event)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (typeView) {
            VIEW_TYPE_ACTIVE_AT_HOME -> {
                val binding = ActiveHomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HomeActiveViewHolder(binding)
            }
            VIEW_TYPE_COMPLETED_AT_HOME -> {
                val binding =
                    CompletedHomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding)
            }
            else -> {
                val binding =
                    CompletedHomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        when (holder) {
            is HomeActiveViewHolder -> holder.bind(event)
            is ViewHolder -> {
                holder.bind(event)

            }
        }

    }

    companion object {
        const val VIEW_TYPE_ACTIVE_AT_HOME = 1
        const val VIEW_TYPE_COMPLETED_AT_HOME = 2
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
