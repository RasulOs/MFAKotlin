package com.example.mfagit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mfagit.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.frameLayoutContainer, MenuCategoriesFragment())
            commit()
        }

    }
}