package com.gigamole.quatrograde

import androidx.annotation.ColorInt

/**
 * The holder of the color and the position of the grade for the [QuatroGradeView].
 *
 * @property color the color.
 * @property position the custom position in the gradient.
 */
data class GradeModel(
	@ColorInt var color: Int,
	var position: Float
)
