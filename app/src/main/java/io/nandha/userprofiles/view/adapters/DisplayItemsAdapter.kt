package io.nandha.userprofiles.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.nandha.userprofiles.R
import io.nandha.userprofiles.view.DisplayItems

class DisplayItemsAdapter(val items: List<DisplayItems>) :
    RecyclerView.Adapter<DisplayItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.display_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.let { viewHolder ->
            items[position].apply {
                viewHolder.title.text = title
                viewHolder.detail.text = detail
                if (title == "Weather") {
                    onClick.invoke(viewHolder)
                } else {
                    viewHolder.itemView.setOnClickListener {
                        onClick.invoke(viewHolder)
                    }
                }
            }
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val detail: TextView = view.findViewById(R.id.detail)
    }
}