package com.todo.app.display

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.todo.app.R

class TodoAdapter(val navigationID: Int) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val testList = arrayOf("masak", "makan", "minum", "nyuci")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvText: MaterialTextView

        init {
            tvText = itemView.findViewById(R.id.item_text)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvText.text = testList.get(position)
        var bundle = bundleOf("todo_id" to position)
        holder.itemView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                navigationID,
                bundle
            )
        )

    }

    override fun getItemCount(): Int = testList.size
}