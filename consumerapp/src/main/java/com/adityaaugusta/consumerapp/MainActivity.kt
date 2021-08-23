package com.adityaaugusta.consumerapp

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adityaaugusta.consumerapp.databinding.ActivityMainBinding
import com.adityaaugusta.consumerapp.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.layoutManager = LinearLayoutManager(this)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadNoteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        loadNoteAsync()
    }


    private fun loadNoteAsync() {

        CoroutineScope(Dispatchers.Main).launch {
            val users = mutableListOf<User>()
            val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
            cursor?.apply {
                while (moveToNext()) {
                    users.add(
                        User(
                            username = getString(getColumnIndexOrThrow("username")),
                            avatar = getString(getColumnIndexOrThrow("avatar"))
                        )
                    )
                }
            }

            val adapter = AdapterUser(users) {

            }
            binding.rvUser.adapter = adapter

            if (users.isNotEmpty()){
                binding.rvUser.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
            }else{
                binding.rvUser.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = getString(R.string.user_not_found)
            }
        }
    }

    companion object {
        private const val AUTHORITY = "com.adityaaugusta.githubuser"
        private const val SCHEME = "content"
        private const val TABLE_NAME = "UserFavourite"

        val CONTENT_URI: Uri =
            Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(TABLE_NAME).build()
    }
}