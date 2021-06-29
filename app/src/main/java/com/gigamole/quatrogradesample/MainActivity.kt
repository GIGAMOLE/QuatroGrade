package com.gigamole.quatrogradesample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gigamole.quatrogradesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setupUi()
	}

	private fun setupUi() {
		binding.mainButtonDefault.setOnClickListener {
			startActivity(Intent(this, DefaultExampleActivity::class.java))
		}
		binding.mainButtonCustom.setOnClickListener {
			startActivity(Intent(this, CustomExampleActivity::class.java))
		}
		binding.mainButtonAnimation.setOnClickListener {
			startActivity(Intent(this, AnimationExampleActivity::class.java))
		}
	}
}
