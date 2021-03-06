package io.nandha.userprofiles.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.nandha.userprofiles.R
import io.nandha.userprofiles.model.data.User
import io.nandha.userprofiles.view.DisplayActivity

class UserListAdapter : PagingDataAdapter<User, UserListAdapter.ViewHolder>(REPO_COMPARATOR) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val profilePic: ImageView = view.findViewById(R.id.profile_pic)
        val cell: TextView = view.findViewById(R.id.cell)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        user?.let {
            holder.name.text = it.name
            holder.cell.text = it.phone
            holder.profilePic.load(it.picture) {
                crossfade(true)
                placeholder(R.drawable.profile_placeholder)
            }
            holder.itemView.setOnClickListener { view ->
                val intent = Intent(view.context, DisplayActivity::class.java)
                intent.putExtra("email", it.email)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_tile, parent, false)
        return ViewHolder(view)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.email == newItem.email

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }
}