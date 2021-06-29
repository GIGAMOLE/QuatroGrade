package com.gigamole.quatrogradesample

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import codes.side.andcolorpicker.converter.setFromColorInt
import codes.side.andcolorpicker.converter.toColorInt
import codes.side.andcolorpicker.model.IntegerHSLColor
import com.gigamole.quatrograde.GradeModel
import com.gigamole.quatrogradesample.databinding.ActivityAnimationBinding
import kotlin.random.Random

class AnimationExampleActivity : AppCompatActivity() {

	private lateinit var binding: ActivityAnimationBinding

	private val topGrades = listOf(
		GradeModel(randomColor(true), 0.0F),
		GradeModel(randomColor(true), 0.33F),
		GradeModel(randomColor(true), 1.0F)
	)
	private val bottomGrades = listOf(
		GradeModel(randomColor(true), 0.0F),
		GradeModel(randomColor(true), 0.66F),
		GradeModel(randomColor(true), 1.0F)
	)

	private val argbEvaluator = ArgbEvaluator()
	private val animatorTop0 = ValueAnimator()
	private val animatorTop1 = ValueAnimator()
	private val animatorTop2 = ValueAnimator()
	private val animatorBottom0 = ValueAnimator()
	private val animatorBottom1 = ValueAnimator()
	private val animatorBottom2 = ValueAnimator()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityAnimationBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setupUi()
	}

	private fun setupUi() {
		topGrades[1].color = getRandomMidColor(topGrades[0].color)
		bottomGrades[1].color = getRandomMidColor(bottomGrades[2].color)

		binding.animationQuatroGradeView.setGrades(topGrades, bottomGrades)

		setupAnimator(animatorTop0, binding.animationQuatroGradeView.getTopGrades()[0])
		setupAnimator(
			animatorTop1,
			binding.animationQuatroGradeView.getTopGrades()[1],
			forMidColorGradeModel = binding.animationQuatroGradeView.getTopGrades()[0]
		)
		setupAnimator(animatorTop2, binding.animationQuatroGradeView.getTopGrades()[2])
		setupAnimator(animatorBottom0, binding.animationQuatroGradeView.getBottomGrades()[0])
		setupAnimator(
			animatorBottom1,
			binding.animationQuatroGradeView.getBottomGrades()[1],
			forMidColorGradeModel = binding.animationQuatroGradeView.getBottomGrades()[2]
		)
		setupAnimator(animatorBottom2, binding.animationQuatroGradeView.getBottomGrades()[2])

		animatorBottom1.addUpdateListener {
			binding.animationQuatroGradeView.refresh()
		}
	}

	private fun setupAnimator(
		animator: ValueAnimator,
		gradeModel: GradeModel,
		forMidColorGradeModel: GradeModel? = null
	) {
		randomizeAnimator(forMidColorGradeModel, animator, gradeModel)

		animator.interpolator = LinearInterpolator()
		animator.setEvaluator(argbEvaluator)
		animator.addListener(
			object : Animator.AnimatorListener {
				override fun onAnimationStart(animation: Animator?) = Unit

				override fun onAnimationEnd(animation: Animator?) {
					randomizeAnimator(forMidColorGradeModel, animator, gradeModel)

					animator.start()
				}

				override fun onAnimationCancel(animation: Animator?) = Unit

				override fun onAnimationRepeat(animation: Animator?) = Unit
			}
		)
		animator.addUpdateListener {
			gradeModel.color = it.animatedValue as Int
		}

		animator.start()
	}

	internal fun randomizeAnimator(
		forMidColorGradeModel: GradeModel?,
		animator: ValueAnimator,
		gradeModel: GradeModel
	) {
		if (forMidColorGradeModel != null) {
			animator.setIntValues(gradeModel.color, getRandomMidColor(forMidColorGradeModel.color))
		} else {
			animator.setIntValues(gradeModel.color, randomColor(true))
		}

		animator.duration = getRandomDuration()
	}

	internal fun getRandomMidColor(fromColor: Int) = IntegerHSLColor().apply {
		setFromColorInt(fromColor)
		intH += Random.nextInt(-36, 36)
	}.toColorInt()

	internal fun getRandomDuration() = (4000 + Random.nextInt(1000)).toLong()
}
