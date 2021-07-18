package io.nandha.userprofiles.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.nandha.userprofiles.R
import io.nandha.userprofiles.databinding.LoadingStateBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {
    class LoadStateViewHolder(private val binding: LoadingStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private fun View.show() {
            visibility = View.VISIBLE
        }
        private fun View.hide() {
            visibility = View.GONE
        }

        init {
            binding.retry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Error -> {
                    binding.retry.show()
                    binding.loadingProgress.hide()
                }
                is LoadState.Loading -> {
                    binding.retry.hide()
                    binding.loadingProgress.show()
                }
                is LoadState.NotLoading -> {
                    binding.root.hide()
                }
            }
        }
        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_state, parent, false)
                val binding = LoadingStateBinding.bind(view)
                return LoadStateViewHolder(binding, retry)
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        println("nandhu onCreateViewHolder")
        return LoadStateViewHolder.create(parent, retry)
    }
}