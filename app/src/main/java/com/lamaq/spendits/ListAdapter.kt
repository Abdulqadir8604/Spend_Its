package com.lamaq.spendits

import android.content.Context
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import java.text.DateFormat

class ListAdapter(var context: Context, var notesList: RealmResults<Note>) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notesList[position]!!
        holder.amtOutput.text = note.amt
        holder.descriptionOutput.text = note.getDescription()
        val formatedTime = DateFormat.getDateTimeInstance().format(note.getCreatedTime())
        holder.timeOutput.text = formatedTime

        holder.itemView.background.setTint(context.getColor(R.color.white))
        holder.amtOutput.setTextColor(context.getColor(R.color.dark))
        holder.descriptionOutput.setTextColor(context.getColor(R.color.dark))
        holder.timeOutput.setTextColor(context.getColor(R.color.dark))

        holder.itemView.setOnLongClickListener { v ->
            val menu = PopupMenu(context, v)
            menu.menu.add("DELETE")
            menu.setOnMenuItemClickListener { item ->
                if (item.title == "DELETE") {
                    Realm.init(context)
                    realm.beginTransaction()
                    note.deleteFromRealm()
                    realm.commitTransaction()
                    Handler().postDelayed({
                        notifyDataSetChanged()
                    }, 100)
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                }
                true
            }
            menu.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var amtOutput: TextView
        var descriptionOutput: TextView
        var timeOutput: TextView

        init {
            amtOutput = itemView.findViewById(R.id.amtoutput)
            descriptionOutput = itemView.findViewById(R.id.descriptionoutput)
            timeOutput = itemView.findViewById(R.id.timeoutput)
        }
    }
}