package com.lamaq.spendits

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

lateinit var realm: Realm
class ListActivity : AppCompatActivity() {

    private var total = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val collectionName = intent.getStringExtra("collectionName")
        realm = Realm.getInstance(
            RealmConfiguration.Builder()
                .name("$collectionName.realm")
                .build()
        )

        val notesTitle = findViewById<TextView>(R.id.notestitle)
        notesTitle.text = collectionName

        val addNoteBtn = findViewById<MaterialButton>(R.id.addnewnotebtn)
        addNoteBtn.setOnClickListener { v: View? ->
            val intent = Intent(this@ListActivity, AddNoteActivity::class.java)
            intent.putExtra("collectionName", collectionName)
            startActivity(intent)
        }
        val notesList = realm.where(
            Note::class.java
        ).findAll().sort("createdTime", Sort.DESCENDING)

        if (notesList.size == 0) {
            findViewById<View>(R.id.nothingtodisplay).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.nothingtodisplay).visibility = View.GONE
        }

        total = 0
        for (note in notesList) {
            total += note.amt.toInt()
        }
        addNoteBtn.text = "Add new note   |   Total = ${total}"
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val myAdapter = ListAdapter(applicationContext, notesList)
        recyclerView.adapter = myAdapter

        notesList.addChangeListener { notes: RealmResults<Note> ->
            total = 0
            for (note in notes) {
                total += note.amt.toInt()
            }
            addNoteBtn.text = "Add new note   |   Total = ${total}"

            if (notes.size == 0) {
                findViewById<View>(R.id.nothingtodisplay).visibility = View.VISIBLE
            } else {
                findViewById<View>(R.id.nothingtodisplay).visibility = View.GONE
            }
            myAdapter.notifyDataSetChanged()
        }
    }
}
