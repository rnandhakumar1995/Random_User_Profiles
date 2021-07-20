package io.nandha.userprofiles.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import io.nandha.userprofiles.databinding.ActivityDisplayBinding
import io.nandha.userprofiles.model.CacheDb


class DisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        intent.extras?.getString("email")?.let {
            val user = CacheDb.getInstance(this).reposDao().getUser(it)
            Picasso.get().load(user.picture).into(binding.profilePic)
        }
    }
}