package io.nandha.userprofiles.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.nandha.userprofiles.databinding.ActivityMainBinding
import io.nandha.userprofiles.view.adapters.LoadingStateAdapter
import io.nandha.userprofiles.view.adapters.UserListAdapter
import io.nandha.userprofiles.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity() : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val userList = activityMainBinding.userList
        val userListAdapter = setupAdapter(userList)

        lifecycleScope.launch {
            viewModel.loadUser().collectLatest {
                userListAdapter.submitData(it)
            }
        }
    }

    private fun setupAdapter(userList: RecyclerView): UserListAdapter {
        val userListAdapter = UserListAdapter()
        userListAdapter.withLoadStateFooter(footer = LoadingStateAdapter { userListAdapter.retry() })
        userList.adapter = userListAdapter
        userList.layoutManager = LinearLayoutManager(this)
        userListAdapter.addLoadStateListener { it ->
            val isListEmpty = it.refresh is LoadState.NotLoading && userListAdapter.itemCount == 0
            showEmptyList(isListEmpty)
            activityMainBinding.userList.visibility =
                if (it.source.refresh is LoadState.NotLoading) View.VISIBLE else View.GONE
            activityMainBinding.loadingProgress.visibility =
                if (it.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            activityMainBinding.retry.visibility =
                if (it.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error
            errorState?.let { error ->
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${error.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return userListAdapter
    }

    private fun showEmptyList(show: Boolean) {
        /*if (show) {
            activityMainBinding.emptyList.visibility = View.VISIBLE
            activityMainBinding.list.visibility = View.GONE
        } else {
            activityMainBinding.emptyList.visibility = View.GONE
            activityMainBinding.list.visibility = View.VISIBLE
        }*/
    }
}