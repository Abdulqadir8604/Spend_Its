package com.lamaq.spendits

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.button.MaterialButton
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addNoteBtn = findViewById<MaterialButton>(R.id.addnewnotebtn)

        addNoteBtn.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_new_collection)
            dialog.show()
            Realm.init(applicationContext)
            val realm = Realm.getDefaultInstance()
            val btnCreate = dialog.findViewById<Button>(R.id.btn_create)

            btnCreate.setOnClickListener {
                val etName = dialog.findViewById<EditText>(R.id.et_collection_name)
                val name = etName.text.toString()

                if (name.isNotEmpty()) {
                    realm.beginTransaction()
                    val collection = realm.createObject(Collection::class.java)
                    collection.name = name
                    realm.commitTransaction()
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        this,
                        "Please enter a name for the collection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        addNoteBtn.text = "Add new collection"
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        Realm.init(applicationContext)
        val realm = Realm.getDefaultInstance()
        val collections = realm.where(Collection::class.java).findAll().sort("name", Sort.ASCENDING)
        val adapter = CollectionAdapter(this, collections)
        recyclerView.adapter = adapter

        collections.addChangeListener { collections: RealmResults<Collection> ->
            addNoteBtn.text = "Add new collection"

            if (collections.size == 0) {
                findViewById<View>(R.id.nothingtodisplay).visibility = View.VISIBLE
            } else {
                findViewById<View>(R.id.nothingtodisplay).visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
        }
    }
}