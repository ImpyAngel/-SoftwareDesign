package edu.software.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class Adapter(private val li: LayoutInflater) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val items: MutableList<Model> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(li.inflate(R.layout.todo_item, parent, false))
    }

    fun addTodo(text: String) {
        items.add(Model(text))
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        model.handle(holder)
        Controller(model).handle(holder)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val select = itemView.findViewById<ImageView>(R.id.select)!!
        val text = itemView.findViewById<TextView>(R.id.text)!!
        val delete = itemView.findViewById<ImageView>(R.id.delete)!!
    }


    private data class Model(val text: String, var isSelect: Boolean = false) {
        fun tap() {
            isSelect = !isSelect
        }

        fun handle(view: ViewHolder) {
            if (!isSelect) {
                view.select.setImageResource(R.drawable.pin_default)
            } else {
                view.select.setImageResource(R.drawable.pin_active)
            }
            view.text.text = text
        }
    }

    private inner class Controller(private val model: Model) {

        fun handle(view: ViewHolder) {
            view.select.setOnClickListener {
                model.tap()
                this@Adapter.notifyDataSetChanged()
            }
            view.delete.setOnClickListener {
                items.remove(model)
                this@Adapter.notifyDataSetChanged()
            }
        }
    }
}