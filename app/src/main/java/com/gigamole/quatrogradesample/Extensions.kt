package com.gigamole.quatrogradesample

import android.app.Activity
import androidx.annotation.ColorInt
import codes.side.andcolorpicker.converter.ContrastColorAlphaMode
import codes.side.andcolorpicker.converter.setFromColorInt
import codes.side.andcolorpicker.converter.toColorInt
import codes.side.andcolorpicker.converter.toContrastColor
import codes.side.andcolorpicker.group.PickerGroup
import codes.side.andcolorpicker.group.registerPickers
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import codes.side.andcolorpicker.view.picker.OnIntegerHSLColorPickListener
import com.gigamole.quatrogradesample.databinding.LayoutColorPickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

fun Activity.showColorPickerBottomSheet(
	@ColorInt color: Int,
	onColorChanged: (color: Int) -> Unit
) {
	val binding = LayoutColorPickerBinding.inflate(layoutInflater)

	val bottomSheetDialog = BottomSheetDialog(this)
	bottomSheetDialog.setContentView(binding.root)

	val group = PickerGroup<IntegerHSLColor>().also {
		it.registerPickers(
			binding.colorPickerHueSeekBar,
			binding.colorPickerSaturationSeekBar,
			binding.colorPickerLightnessSeekBar,
			binding.colorPickerAlphaSeekBar
		)
	}
	group.addListener(
		object : OnIntegerHSLColorPickListener() {
			override fun onColorChanged(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int) {
				super.onColorChanged(picker, color, value)
				onColorChanged(color.toColorInt())
			}
		}
	)
	group.setColor(IntegerHSLColor().apply { setFromColorInt(color) })

	binding.colorPickerButtonRandomColor.setOnClickListener {
		group.setColor(randomHSLColor())
	}

	bottomSheetDialog.show()
}

fun MaterialButton.setBackgroundAndContrastColors(@ColorInt color: Int) {
	setBackgroundColor(color)
	setTextColor(
		IntegerHSLColor().apply {
			setFromColorInt(color)
		}.toContrastColor(ContrastColorAlphaMode.LIGHT_BACKGROUND)
	)
}

fun randomHSLColor(pure: Boolean = false) = IntegerHSLColor.createRandomColor(pure)

fun randomColor(pure: Boolean = false) = randomHSLColor(pure).toColorInt()
