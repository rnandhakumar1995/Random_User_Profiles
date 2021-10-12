package io.nandha.userprofiles.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

class MainActivity : AppCompatActivity() {
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
        userList.adapter =
            userListAdapter.withLoadStateFooter(footer = LoadingStateAdapter { userListAdapter.retry() })
        userList.layoutManager = LinearLayoutManager(this)
        userListAdapter.addLoadStateListener {
            val isListEmpty = it.refresh is LoadState.NotLoading && userListAdapter.itemCount == 0
            showEmptyList(isListEmpty)
            activityMainBinding.userList.visibility =
                if (it.source.refresh is LoadState.NotLoading) View.VISIBLE else View.GONE
            activityMainBinding.loadingProgress.visibility =
                if (it.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            activityMainBinding.retry.visibility =
                if (it.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
        }
        return userListAdapter
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show()
        }
    }
}