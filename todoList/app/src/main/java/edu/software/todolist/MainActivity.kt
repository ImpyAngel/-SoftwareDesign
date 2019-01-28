package edu.software.todolist

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.support.v7.widget.RecyclerView
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.list)
        adapter = Adapter(layoutInflater)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    @Suppress("UNUSED_PARAMETER")
    fun addTodo(view: View) {
        val alert = AlertDialog.Builder(this)
        val edit = EditText(this)
        alert.setMessage("Enter Your Message")
        alert.setTitle("Enter Your Title")

        alert.setView(edit)

        alert.setPositiveButton("Add") { dialog, _ ->
            adapter.addTodo(edit.text.toString())
            dialog.dismiss()
        }
        alert.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        alert.show()
    }
}

