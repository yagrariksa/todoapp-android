package com.todo.app.display

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.todo.app.R
import com.todo.app.models.Todo

class TodoAdapter(val navigationID: Int) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val testList = arrayOf("masak", "makan", "minum", "nyuci")

    private val listTodo = mutableListOf<Todo>()

    fun clearData() {
        listTodo.clear()
        notifyDataSetChanged()
    }

    fun supplyData(data: List<Todo>) {
        listTodo.clear()
        listTodo.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvText: MaterialTextView
        var btnUrl: MaterialButton
        val context = itemView.context

        init {
            tvText = itemView.findViewById(R.id.item_text)
            btnUrl = itemView.findViewById(R.id.btn_url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvText.text = listTodo.get(position).name
        holder.btnUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(listTodo.get(position).url))
            holder.context.startActivity(intent)
        }
        var bundle = bundleOf("todo_id" to listTodo.get(position).id)
        holder.itemView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                navigationID,
                bundle
            )
        )

    }

    override fun getItemCount(): Int = listTodo.size
}