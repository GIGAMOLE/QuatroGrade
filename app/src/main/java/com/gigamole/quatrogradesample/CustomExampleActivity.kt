package com.gigamole.quatrogradesample

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import com.gigamole.quatrograde.GradeModel
import com.gigamole.quatrograde.GradeOrientation
import com.gigamole.quatrogradesample.databinding.ActivityCustomBinding
import com.gigamole.quatrogradesample.databinding.ItemCustomGradeBinding
import kotlin.random.Random

class CustomExampleActivity : AppCompatActivity() {

	private lateinit var binding: ActivityCustomBinding

	private val topGrades = mutableListOf<GradeModel>()
	private val bottomGrades = mutableListOf<GradeModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCustomBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setupUi()
	}

	private fun setupUi() {
		topGrades.addAll(binding.customQuatroGradeView.getTopGrades().map { it.copy() })
		bottomGrades.addAll(binding.customQuatroGradeView.getBottomGrades().map { it.copy() })

		binding.customTextViewTopGradesTitle.isActivated = false
		binding.customTextViewTopGradesTitle.setOnClickListener {
			togglePanel(
				binding.customTextViewTopGradesTitle,
				binding.customTopGrades,
				binding.customButtonAddTopGrade
			)
		}
		binding.customTextViewBottomGradesTitle.isActivated = false
		binding.customTextViewBottomGradesTitle.setOnClickListener {
			togglePanel(
				binding.customTextViewBottomGradesTitle,
				binding.customBottomGrades,
				binding.customButtonAddBottomGrade
			)
		}

		binding.customButtonAddTopGrade.setOnClickListener {
			val gradeModel = createRandomGradeModel(topGrades.size)

			topGrades.add(gradeModel)
			invalidateCustomGradeItems()
		}
		binding.customButtonAddBottomGrade.setOnClickListener {
			val gradeModel = createRandomGradeModel(bottomGrades.size)

			bottomGrades.add(gradeModel)
			invalidateCustomGradeItems()
		}

		binding.customLayoutOrientation.setOnClickListener {
			binding.customSwitchOrientation.isChecked = !binding.customSwitchOrientation.isChecked
			binding.customQuatroGradeView.orientation = when (binding.customSwitchOrientation.isChecked) {
				true -> GradeOrientation.VERTICAL
				else -> GradeOrientation.HORIZONTAL
			}
		}

		invalidateCustomGradeItems()
	}

	private fun togglePanel(title: TextView, layout: LinearLayout, button: Button) {
		if (title.isActivated) {
			collapsePanel(title, layout, button)
		} else {
			expandPanel(title, layout, button)
		}
	}

	private fun expandPanel(title: TextView, layout: LinearLayout, button: Button) {
		title.isActivated = true
		title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_close_section, 0)
		title.setBackgroundColor(ColorUtils.setAlphaComponent(Color.DKGRAY, 35))

		layout.isVisible = true
		button.isVisible = true
	}

	private fun collapsePanel(title: TextView, layout: LinearLayout, button: Button) {
		title.isActivated = false
		title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_open_section, 0)
		title.setBackgroundColor(Color.TRANSPARENT)

		layout.isVisible = false
		button.isVisible = false
	}

	private fun createRandomGradeModel(size: Int) = GradeModel(
		randomColor(false),
		when (size) {
			0 -> 0.0F
			1 -> 1.0F
			else -> Random.nextFloat()
		}
	)

	private fun invalidateCustomGradeItems() {
		binding.customQuatroGradeView.setGrades(topGrades, bottomGrades)

		setupCustomGradeViews(topGrades, binding.customTopGrades)
		setupCustomGradeViews(bottomGrades, binding.customBottomGrades)
	}

	private fun setupCustomGradeViews(grades: List<GradeModel>, layout: LinearLayout) {
		layout.removeAllViews()
		grades.forEachIndexed { index, _ -> addCustomGradeView(index, layout) }
	}

	private fun addCustomGradeView(index: Int, layout: LinearLayout) {
		val isTop = layout.id == R.id.custom_top_grades
		val itemBinding = ItemCustomGradeBinding.inflate(layoutInflater)
		val gradeModel = when (isTop) {
			true -> binding.customQuatroGradeView.getTopGrades()[index]
			else -> binding.customQuatroGradeView.getBottomGrades()[index]
		}

		itemBinding.itemCustomGradeButtonColor.setBackgroundAndContrastColors(gradeModel.color)
		itemBinding.itemCustomGradeButtonColor.setOnClickListener {
			showColorPickerBottomSheet(gradeModel.color) { color ->
				gradeModel.color = color

				itemBinding.itemCustomGradeButtonColor.setBackgroundAndContrastColors(gradeModel.color)
				binding.customQuatroGradeView.refresh()
			}
		}

		itemBinding.itemCustomGradeSeekBarPosition.progress = (gradeModel.position * 100.0F).toInt()
		itemBinding.itemCustomGradeSeekBarPosition.setOnSeekBarChangeListener(
			object : SeekBar.OnSeekBarChangeListener {
				override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
					gradeModel.position = progress.toFloat() / 100.0F
					binding.customQuatroGradeView.refresh()
				}

				override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

				override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
			}
		)

		itemBinding.itemCustomGradeButtonRemove.setOnClickListener {
			if (isTop) {
				if (topGrades.size <= 2) {
					return@setOnClickListener
				}

				topGrades.removeAt(index)
			} else {
				if (bottomGrades.size <= 2) {
					return@setOnClickListener
				}

				bottomGrades.removeAt(index)
			}

			invalidateCustomGradeItems()
		}

		layout.addView(itemBinding.root)
	}
}
