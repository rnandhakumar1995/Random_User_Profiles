package io.nandha.userprofiles.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.nandha.userprofiles.R
import io.nandha.userprofiles.model.data.User
import io.nandha.userprofiles.view.DisplayActivity

class SearchResultAdapter(var users: List<User>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    init {
        println("Initiali search adapter")
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val profilePic: ImageView = view.findViewById(R.id.profile_pic)
        val cell: TextView = view.findViewById(R.id.cell)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = user.name
        holder.cell.text = user.phone

        holder.profilePic.load(user.picture) {
            crossfade(true)
            placeholder(R.drawable.profile_placeholder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DisplayActivity::class.java)
            intent.putExtra("email", user.email)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = users.size

    fun updateList(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }
}