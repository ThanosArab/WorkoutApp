package com.smartath.workoutapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smartath.workoutapp.databinding.ItemHistoryLayoutBinding

class HistoryAdapter(private val items: ArrayList<HistoryEntity>,
                        private val deleteListener:(id:Int)->Unit): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.id.text = (position + 1).toString()
        holder.date.text = items[position].date
        holder.duration.text = items[position].duration

        if (position %2 ==0){
            holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey))
        }
        else{
            holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.deleteIv.setOnClickListener {
            deleteListener.invoke(items[position].id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(binding: ItemHistoryLayoutBinding): RecyclerView.ViewHolder(binding.root){
        var layout = binding.mainLayout
        var id = binding.positionTv
        var date = binding.dateTv
        var duration = binding.durationTv
        var deleteIv = binding.deleteIv

    }

}