package com.lamaq.spendits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lamaq.spendits.R
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.RealmConfiguration

class AddNoteActivity : AppCompatActivity() {
    private var description = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        val amtInput = findViewById<EditText>(R.id.amtinput)
        val descriptionInput = findViewById<EditText>(R.id.descriptioninput)
        val saveBtn = findViewById<MaterialButton>(R.id.savebtn)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val collectionName = intent.getStringExtra("collectionName")

        Realm.init(applicationContext)
        val realm = Realm.getInstance(
            RealmConfiguration.Builder()
                .name("$collectionName.realm")
                .build()
        )

        //on ime action done
        descriptionInput.setOnEditorActionListener { v, actionId, event ->
            saveBtn.performClick()
            true
        }

        saveBtn.setOnClickListener { v: View? ->
            if (amtInput.text.toString().isNotEmpty()) {
                val title = amtInput.text.toString()
                if (descriptionInput.text.toString().isNotEmpty()) {
                    description = descriptionInput.text.toString()
                }
                val createdTime = System.currentTimeMillis()
                realm.beginTransaction()
                val note = realm.createObject(Note::class.java)
                note.amt = title
                note.description = description
                note.createdTime = createdTime
                realm.commitTransaction()
                Toast.makeText(
                    applicationContext,
                    "Note saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter both amount and description",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()
        }
    }
}