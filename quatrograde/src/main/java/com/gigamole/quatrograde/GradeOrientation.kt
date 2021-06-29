package com.gigamole.quatrograde

/**
 * The gradient draw orientation for the [QuatroGradeView].
 * The mask of the above gradient will be the opposite. Read more: [QuatroGradeView.onDraw].
 *
 * @property id the id of the enum value.
 */
enum class GradeOrientation(val id: Int) {

	/**
	 * Set the draw orientation of the gradients from the left to the right.
	 */
	HORIZONTAL(0),

	/**
	 * Set the draw orientation of the gradients from the top to the bottom.
	 */
	VERTICAL(1);

	/**
	 * Returns whether the orientation is [HORIZONTAL].
	 */
	fun isHorizontal() = this == HORIZONTAL

	/**
	 * Returns whether the orientation is [VERTICAL].
	 */
	fun isVertical() = this == VERTICAL

	companion object {

		/**
		 * The default orientation [id].
		 */
		const val DEFAULT_ORIENTATION_ID = 0

		/**
		 * Provides the default [GradeOrientation].
		 */
		fun getDefault() = fromId(DEFAULT_ORIENTATION_ID)

		/**
		 * Provides the [GradeOrientation] based on the incoming id.
		 *
		 * @param id basically the id from the XML attrs.
		 * @return the respective [GradeOrientation].
		 */
		fun fromId(id: Int): GradeOrientation {
			return values().find {
				it.id == id
			} ?: throw IllegalArgumentException("GradeOrientation has only has range of ID's from 0 to 1.")
		}
	}
}
