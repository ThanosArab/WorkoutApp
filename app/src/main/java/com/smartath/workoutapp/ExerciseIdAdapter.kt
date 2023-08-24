package com.smartath.workoutapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smartath.workoutapp.databinding.ActivityExerciseBinding
import com.smartath.workoutapp.databinding.ItemExerciseIdBinding

class ExerciseIdAdapter(private val items: ArrayList<ExerciseModel>): RecyclerView.Adapter<ExerciseIdAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseIdBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemIdTv.text = (items[position].getId()).toString()

        when{
            items[position].getIsSelected() ->{
                holder.itemIdTv.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_id_selected_background)
            }
            items[position].getIsCompleted() ->{
                holder.itemIdTv.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_id_completed_background)
            }
            else->{
                holder.itemIdTv.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_id_background)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(binding: ItemExerciseIdBinding): RecyclerView.ViewHolder(binding.root){
        var itemIdTv = binding.itemIdTv
    }
}