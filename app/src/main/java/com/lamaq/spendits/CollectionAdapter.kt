package com.lamaq.spendits

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.DateFormat

class CollectionAdapter(private val context: Context, private val collections: RealmResults<Collection>) :
    RecyclerView.Adapter<CollectionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val collection = collections[position]
        holder.amtOutput.text = collection?.name
        val formattedTime = DateFormat.getDateTimeInstance().format(collection?.createdTime)
        holder.timeOutput.text = formattedTime

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListActivity::class.java)
            intent.putExtra("collectionName", collection?.name)
            context.startActivity(intent)
        }


        holder.itemView.setOnLongClickListener { v ->
            val menu = PopupMenu(context, v)
            menu.menu.add("DELETE")
            menu.setOnMenuItemClickListener { item ->
                if (item.title == "DELETE") {
                    Realm.init(context)
                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()
                    collection?.aq_deleteFromRealm()
                    realm.commitTransaction()
                    Handler().postDelayed({
                        notifyDataSetChanged()
                    }, 100)
                }
                true
            }
            menu.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amtOutput: TextView = itemView.findViewById(R.id.amtoutput)
        val timeOutput: TextView = itemView.findViewById(R.id.timeoutput)
        val totalOutput: TextView = itemView.findViewById(R.id.descriptionoutput)
    }
}
