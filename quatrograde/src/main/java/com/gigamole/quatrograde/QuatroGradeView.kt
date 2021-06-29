package com.gigamole.quatrograde

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * The view to draw the 4 colors on each side by default. It is possible to provide more colors and its positions
 * with [GradeModel]. The gradient colors orientation is set with [GradeOrientation]: for the wide view
 * [GradeOrientation.HORIZONTAL], and for the tall the [GradeOrientation.VERTICAL].
 *
 * ^^ - this symbol across the documentation, means that the [orientation] swaps the top colors side to the left,
 * and bottom to the right, so this should be considered while working the [QuatroGradeView] methods and XML attrs.
 */
class QuatroGradeView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

	companion object {

		private const val START_POSITION = 0.0F
		private const val END_POSITION = 1.0F

		private const val DEFAULT_RES_ID = -1
	}

	private val basePaint = Paint().apply {
		isFilterBitmap = true
		isAntiAlias = true
		isDither = true

		style = Paint.Style.FILL
	}
	private val topGradientPaint = Paint(basePaint)
	private val bottomGradientPaint = Paint(basePaint)
	private val bottomGradientMaskPaint = Paint(basePaint).apply {
		xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
	}

	private val boundsRectF = RectF()

	private val topGrades = mutableListOf<GradeModel>()
	private val bottomGrades = mutableListOf<GradeModel>()
	private val grades = mutableListOf<GradeModel>()

	/**
	 * The holder of the current [GradeOrientation].
	 */
	var orientation = GradeOrientation.getDefault()
		set(value) {
			field = value
			invalidateGradients()
		}

	init {
		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuatroGradeView)

		typedArray.run {
			val colorsResId = getResourceId(R.styleable.QuatroGradeView_qgv_colors, DEFAULT_RES_ID)

			if (colorsResId != DEFAULT_RES_ID) {
				setColors(resources.getIntArray(colorsResId))
			} else {
				val topColorsResId =
					getResourceId(R.styleable.QuatroGradeView_qgv_topColors, DEFAULT_RES_ID)
				val bottomColorsResId =
					getResourceId(R.styleable.QuatroGradeView_qgv_bottomColors, DEFAULT_RES_ID)
				val topPositionsResId =
					getResourceId(R.styleable.QuatroGradeView_qgv_topPositions, DEFAULT_RES_ID)
				val bottomPositionsResId =
					getResourceId(R.styleable.QuatroGradeView_qgv_bottomPositions, DEFAULT_RES_ID)

				if (
					topColorsResId == DEFAULT_RES_ID ||
					bottomColorsResId == DEFAULT_RES_ID ||
					topPositionsResId == DEFAULT_RES_ID ||
					bottomPositionsResId == DEFAULT_RES_ID
				) {
					if (isInEditMode) {
						throw IllegalStateException(
							"""
							To setup colors use "qgv_colors" attribute or the set of 4 attributes:
							"qgv_topColors", "qgv_bottomColors", "qgv_topPositions", "qgv_bottomPositions".
							""".trimIndent()
						)
					}
				} else {
					val topColors = resources.getIntArray(topColorsResId)
					val topPositions = resources.getStringArray(topPositionsResId).map { it.toFloat() }

					val bottomColors = resources.getIntArray(bottomColorsResId)
					val bottomPositions = resources.getStringArray(bottomPositionsResId).map { it.toFloat() }

					setGrades(
						topColors.zip(topPositions).map { GradeModel(it.first, it.second) },
						bottomColors.zip(bottomPositions).map { GradeModel(it.first, it.second) }
					)
				}
			}

			orientation = GradeOrientation.fromId(
				getInt(R.styleable.QuatroGradeView_qgv_orientation, GradeOrientation.DEFAULT_ORIENTATION_ID)
			)

			recycle()
		}
	}

	/**
	 * Set the default 4 color gradient with the color array res.
	 * Read more: [setColorsInt].
	 *
	 * @param colorsArray the colors array res.
	 */
	fun setColors(@ArrayRes colorsArray: Int) {
		val colors = resources.getIntArray(colorsArray)
		setColors(colors)
	}

	/**
	 * Set the default 4 color gradient with the color array.
	 * Read more: [setColorsInt].
	 *
	 * @param colors the colors array.
	 */
	fun setColors(colors: IntArray) {
		setColorsInt(
			colors[0],
			colors[1],
			colors[2],
			colors[3]
		)
	}

	/**
	 * Set the default 4 color gradient via [ColorRes] support.
	 * Read more: [setColorsInt].
	 *
	 * @param topLeft the (top left)^^ color res.
	 * @param topRight the (top right)^^ color res.
	 * @param bottomLeft the (bottom left)^^ color res.
	 * @param bottomRight the (bottom right)^^ color res.
	 */
	fun setColorsRes(
		@ColorRes topLeft: Int,
		@ColorRes topRight: Int,
		@ColorRes bottomLeft: Int,
		@ColorRes bottomRight: Int
	) {
		setColorsInt(
			ContextCompat.getColor(context, topLeft),
			ContextCompat.getColor(context, topRight),
			ContextCompat.getColor(context, bottomLeft),
			ContextCompat.getColor(context, bottomRight),
		)
	}

	/**
	 * Set the default 4 color gradient via [ColorInt] support.
	 * The positions automatically set to the [START_POSITION] and [END_POSITION].
	 *
	 * @param topLeft the (top left)^^ color.
	 * @param topRight the (top right)^^ color.
	 * @param bottomLeft the (bottom left)^^ color.
	 * @param bottomRight the (bottom right)^^ color.
	 */
	fun setColorsInt(
		@ColorInt topLeft: Int,
		@ColorInt topRight: Int,
		@ColorInt bottomLeft: Int,
		@ColorInt bottomRight: Int
	) {
		invalidateGrades(
			listOf(GradeModel(topLeft, START_POSITION), GradeModel(topRight, END_POSITION)),
			listOf(GradeModel(bottomLeft, START_POSITION), GradeModel(bottomRight, END_POSITION)),
		)
	}

	/**
	 * Set the custom [GradeModel]s for the top^^ and bottom^^ sides.
	 *
	 * @param topGrades the top^^ side colors and positions.
	 * @param bottomGrades the bottom^^ side colors and positions.
	 */
	fun setGrades(
		topGrades: List<GradeModel>,
		bottomGrades: List<GradeModel>
	) {
		invalidateGrades(topGrades, bottomGrades)
	}

	/**
	 * Provide the top [GradeModel]s.
	 *
	 * @return the top grades.
	 */
	fun getTopGrades(): List<GradeModel> = topGrades

	/**
	 * Provide the bottom [GradeModel]s.
	 *
	 * @return the bottom grades.
	 */
	fun getBottomGrades(): List<GradeModel> = bottomGrades

	/**
	 * Provide the all [GradeModel]s.
	 *
	 * @return the all grades.
	 */
	fun getGrades(): List<GradeModel> = grades

	private fun invalidateGrades(
		topGrades: List<GradeModel>,
		bottomGrades: List<GradeModel>
	) {
		this.topGrades.clear()
		this.bottomGrades.clear()

		this.topGrades.addAll(topGrades)
		this.bottomGrades.addAll(bottomGrades)

		grades.addAll(topGrades)
		grades.addAll(bottomGrades)

		invalidateGradients()
	}

	private fun invalidateGradients() {
		if (grades.isEmpty()) {
			if (isInEditMode) {
				throw IllegalStateException(
					"""
					GradeModels (Colors) are not found. 
					Please, provide them through XML or programmatically.
					""".trimIndent()
				)
			}
			return
		}

		val x1 = if (orientation.isHorizontal()) boundsRectF.width() else 0.0F
		val y1 = if (orientation.isHorizontal()) 0.0F else boundsRectF.height()

		topGradientPaint.shader = LinearGradient(
			0.0F,
			0.0F,
			x1,
			y1,
			topGrades.map { it.color }.toIntArray(),
			topGrades.map { it.position }.toFloatArray(),
			Shader.TileMode.CLAMP
		)
		bottomGradientPaint.shader = LinearGradient(
			0.0F,
			0.0F,
			x1,
			y1,
			bottomGrades.map { it.color }.toIntArray(),
			bottomGrades.map { it.position }.toFloatArray(),
			Shader.TileMode.CLAMP
		)

		// The x1 and y1 for the mask paint is swapped to create the fade effect.
		bottomGradientMaskPaint.shader = LinearGradient(
			0.0F,
			0.0F,
			y1,
			x1,
			Color.BLACK,
			Color.TRANSPARENT,
			Shader.TileMode.CLAMP
		)

		invalidate()
	}

	/**
	 * Refresh and redraw the view. Useful when applying custom changes on the [topGrades], [bottomGrades] or [grades].
	 */
	fun refresh() = invalidateGradients()

	/**
	 * Invalidates the View bounds holder and the gradients.
	 */
	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)

		boundsRectF.set(0.0F, 0.0F, w.toFloat(), h.toFloat())

		invalidateGradients()
	}

	/**
	 * The draw order:
	 * 1. Draws the below top^^ gradient over whole View.
	 * 2. Draws the above bottom^^ gradient over whole View with the fade mask.
	 */
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		if (grades.isEmpty()) {
			return
		}

		canvas.drawRect(boundsRectF, topGradientPaint)

		val saveCount = canvas.saveLayer(boundsRectF, null)

		canvas.drawRect(boundsRectF, bottomGradientPaint)
		canvas.drawPaint(bottomGradientMaskPaint)

		canvas.restoreToCount(saveCount)
	}
}
