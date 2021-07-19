package io.nandha.userprofiles.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.nandha.userprofiles.R
import io.nandha.userprofiles.databinding.LoadingStateBinding
import io.nandha.userprofiles.view.hide
import io.nandha.userprofiles.view.show

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {
    class LoadStateViewHolder(private val binding: LoadingStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
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
        return LoadStateViewHolder.create(parent, retry)
    }
}