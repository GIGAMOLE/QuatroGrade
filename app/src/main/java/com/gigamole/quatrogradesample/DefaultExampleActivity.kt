package com.gigamole.quatrogradesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gigamole.quatrograde.GradeOrientation
import com.gigamole.quatrogradesample.databinding.ActivityDefaultBinding
import com.google.android.material.button.MaterialButton

class DefaultExampleActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDefaultBinding

	private val colors by lazy {
		resources.getIntArray(R.array.default_colors)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityDefaultBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setupUi()
	}

	private fun setupUi() {
		setupButton(binding.defaultButtonTopLeft, 0)
		setupButton(binding.defaultButtonTopRight, 1)
		setupButton(binding.defaultButtonBottomLeft, 2)
		setupButton(binding.defaultButtonBottomRight, 3)

		binding.defaultLayoutOrientation.setOnClickListener {
			binding.defaultSwitchOrientation.isChecked = !binding.defaultSwitchOrientation.isChecked
			binding.defaultQuatroGradeView.orientation = when (binding.defaultSwitchOrientation.isChecked) {
				true -> GradeOrientation.VERTICAL
				else -> GradeOrientation.HORIZONTAL
			}
		}

		invalidateColors()
	}

	private fun setupButton(button: MaterialButton, index: Int) {
		button.setOnClickListener {
			showColorPickerBottomSheet(colors[index]) { color ->
				colors[index] = color
				invalidateColors()
			}
		}
	}

	private fun invalidateColors() {
		binding.defaultQuatroGradeView.setColors(colors)

		binding.defaultButtonTopLeft.setBackgroundAndContrastColors(colors[0])
		binding.defaultButtonTopRight.setBackgroundAndContrastColors(colors[1])
		binding.defaultButtonBottomLeft.setBackgroundAndContrastColors(colors[2])
		binding.defaultButtonBottomRight.setBackgroundAndContrastColors(colors[3])
	}
}
