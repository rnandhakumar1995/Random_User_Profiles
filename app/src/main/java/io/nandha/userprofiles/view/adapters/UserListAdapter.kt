package io.nandha.userprofiles.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.nandha.userprofiles.R
import io.nandha.userprofiles.model.User

class UserListAdapter : PagingDataAdapter<User, UserListAdapter.ViewHolder>(REPO_COMPARATOR) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        user?.let {
            holder.itemView.findViewById<TextView>(R.id.email).text = it.email
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