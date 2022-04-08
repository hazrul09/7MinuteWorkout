package com.simpl3apz.a7minuteworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpl3apz.a7minuteworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(var items : ArrayList<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

        class ViewHolder(binding:ItemExerciseStatusBinding): RecyclerView.ViewHolder(binding.root){
            var tvExerciseItem = binding.tvExerciseItem
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : ExerciseModel = items[position]
        holder.tvExerciseItem.text = model.getId().toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}