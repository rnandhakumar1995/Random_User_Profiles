package io.nandha.userprofiles.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.nandha.userprofiles.R
import io.nandha.userprofiles.databinding.ActivityMainBinding
import io.nandha.userprofiles.model.CacheDb
import io.nandha.userprofiles.view.adapters.LoadingStateAdapter
import io.nandha.userprofiles.view.adapters.SearchResultAdapter
import io.nandha.userprofiles.view.adapters.UserListAdapter
import io.nandha.userprofiles.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity() : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val searchResultAdapter by lazy { SearchResultAdapter(listOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val userList = activityMainBinding.userList
        val userListAdapter = setupAdapter(userList)
        handleIntent(intent)

        activityMainBinding.searchResultList.layoutManager = LinearLayoutManager(this)
        activityMainBinding.searchResultList.adapter = searchResultAdapter

        lifecycleScope.launch {
            viewModel.loadUser().collectLatest {
                userListAdapter.submitData(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            activityMainBinding.searchResultList.apply { if (query.isNullOrEmpty()) hide() else show() }
            query?.let {
                val users = CacheDb.getInstance(this).reposDao().searchUser("%$query%")
                searchResultAdapter.updateList(users)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        (searchItem.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(p0: MenuItem?) = true

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    activityMainBinding.searchResultList.hide()
                    return true
                }
            })
        }
        return true
    }

    private fun setupAdapter(userList: RecyclerView): UserListAdapter {
        val userListAdapter = UserListAdapter()
        userList.adapter =
            userListAdapter.withLoadStateHeaderAndFooter(footer = LoadingStateAdapter { userListAdapter.retry() }, header = LoadingStateAdapter { userListAdapter.retry() })
        userList.layoutManager = LinearLayoutManager(this)
        userListAdapter.addLoadStateListener {
            val isListEmpty = it.refresh is LoadState.NotLoading && userListAdapter.itemCount == 0
            showErrorMessage(isListEmpty)
            activityMainBinding.userList.apply { if (it.source.refresh is LoadState.NotLoading) show() else hide() }
            activityMainBinding.loadingProgress.apply { if (it.source.refresh is LoadState.Loading) show() else hide() }
            activityMainBinding.retry.apply { if (it.source.refresh is LoadState.Error) show() else hide() }
        }
        return userListAdapter
    }

    private fun showErrorMessage(isListEmpty: Boolean) {
        if (isListEmpty)
            activityMainBinding.error.show()
        else
            activityMainBinding.error.hide()
    }
}