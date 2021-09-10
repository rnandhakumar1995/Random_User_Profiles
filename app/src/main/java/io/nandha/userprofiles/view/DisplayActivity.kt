package io.nandha.userprofiles.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import io.nandha.userprofiles.R
import io.nandha.userprofiles.Result
import io.nandha.userprofiles.databinding.ActivityDisplayBinding
import io.nandha.userprofiles.model.Api
import io.nandha.userprofiles.model.data.User
import io.nandha.userprofiles.view.adapters.DisplayItemsAdapter
import io.nandha.userprofiles.viewmodel.DisplayActivityViewModel
import kotlinx.coroutines.launch


data class DisplayItems(
    val title: String,
    val detail: String,
    val onClick: (holder: DisplayItemsAdapter.ViewHolder) -> Unit = {}
)


class DisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayBinding
    private lateinit var viewModel: DisplayActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this).get(DisplayActivityViewModel::class.java)
        intent.extras?.getString("email")?.let {
            val userItems = getDetailItems(viewModel.getUser(it))
            binding.userDetails.adapter = DisplayItemsAdapter(userItems)
        }
    }

    private fun getDetailItems(user: User): List<DisplayItems> {
        val fields = mutableListOf<DisplayItems>()
        user.apply {
            binding.toolbar.title = name
            binding.profilePic.load(picture) {
                crossfade(true)
                placeholder(R.drawable.profile_placeholder)
            }
            fields.add(DisplayItems("Email", email) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                startActivity(intent)
            })
            fields.add(DisplayItems("Phone", phone) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            })
            fields.add(DisplayItems("Address", address))
            fields.add(DisplayItems("Weather", "Load") { updateWeatherReport(coordinate, it) })
        }
        return fields
    }

    private fun updateWeatherReport(
        coordinate: String,
        viewHolder: DisplayItemsAdapter.ViewHolder
    ) {
        lifecycleScope.launch {
            viewHolder.detail.text = getString(R.string.loading)
            when (val result = viewModel.loadWeather(Api.create(), coordinate)) {
                is Result.Success -> {
                    viewHolder.detail.text = getString(R.string.loading)
                    viewHolder.detail.text = result.data.weather[0].description.replaceFirstChar {
                        it.uppercase()
                    }
                }
                is Result.Error -> Toast.makeText(
                    this@DisplayActivity,
                    result.msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}