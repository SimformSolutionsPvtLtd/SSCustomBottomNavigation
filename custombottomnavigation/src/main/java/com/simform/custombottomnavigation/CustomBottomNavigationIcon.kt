package com.simform.custombottomnavigation

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.simform.custombottomnavigation.databinding.CustomBottomNavigationIconBinding

@Suppress("unused")
class CustomBottomNavigationIcon : RelativeLayout {

    companion object {
        const val EMPTY_VALUE = "empty"
    }

    private lateinit var binding: CustomBottomNavigationIconBinding

    var defaultIconColor = 0
        set(value) {
            field = value
            if (allowDraw)
                binding.iv.color = if (!isEnabledCell) defaultIconColor else selectedIconColor
        }
    var selectedIconColor = Color.parseColor("#00C957")
        set(value) {
            field = value
            if (allowDraw)
                binding.iv.color = if (isEnabledCell) selectedIconColor else defaultIconColor
        }
    var circleColor = 0
        set(value) {
            field = value
            if (allowDraw)
                isEnabledCell = isEnabledCell
        }

    var icon = 0
        set(value) {
            field = value
            if (allowDraw)
                binding.iv.resource = value
        }

    var iconText = ""
        set(value) {
            field = value
            if (allowDraw)
                binding.tv.text = value
        }

    var iconTextColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                if (!isEnabledCell) binding.tv.setTextColor(iconTextColor) else binding.tv.setTextColor(
                    selectedIconTextColor
                )

            }
        }

    var selectedIconTextColor = 0
        set(value) {
            field = value
            if (allowDraw)
                if (isEnabledCell) binding.tv.setTextColor(selectedIconTextColor) else binding.tv.setTextColor(
                    iconTextColor
                )
        }

    var iconTextTypeface: Typeface? = null
        set(value) {
            field = value
            if (allowDraw && field != null)
                binding.tv.typeface = field
        }

    var iconTextSize = 10f
        set(value) {
            field = value
            if (allowDraw) {
                binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, field)
            }
        }

    var count: String? = EMPTY_VALUE
        set(value) {
            field = value
            if (allowDraw) {
                if (count != null && count == EMPTY_VALUE) {
                    binding.tvCount.text = ""
                    binding.tvCount.visibility = View.INVISIBLE
                } else {
                    if (count != null && count?.length ?: 0 >= 3) {
                        field = count?.substring(0, 1) + ".."
                    }
                    binding.tvCount.text = count
                    binding.tvCount.visibility = View.VISIBLE
                    val scale = if (count?.isEmpty() == true) 0.5f else 1f
                    binding.tvCount.scaleX = scale
                    binding.tvCount.scaleY = scale
                }
            }
        }

    private var iconSize = dip(context, 48)
        set(value) {
            field = value
            if (allowDraw) {
                binding.iv.size = value
                binding.iv.pivotX = iconSize / 2f
                binding.iv.pivotY = iconSize / 2f
            }
        }

    var countTextColor = 0
        set(value) {
            field = value
            if (allowDraw)
                binding.tvCount.setTextColor(field)
        }

    var countBackgroundColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                val d = GradientDrawable()
                d.setColor(field)
                d.shape = GradientDrawable.OVAL
                ViewCompat.setBackground(binding.tvCount, d)
            }
        }

    var countTypeface: Typeface? = null
        set(value) {
            field = value
            if (allowDraw && field != null)
                field?.let {
                    binding.tvCount.typeface = it
                }
        }

    var rippleColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                isEnabledCell = isEnabledCell
            }
        }

    var isFromLeft = false
    var duration = 0L
    private var progress = 0f
        set(value) {
            field = value
            Log.e("TAG", "height is ${binding.root.layoutParams.height} ${dip(context, 18)}")
            binding.fl.y = (1f - progress) * dip(context, 18) + dip(context, -2)

            binding.iv.color = if (progress == 1f) selectedIconColor else iconTextColor
            binding.tv.setTextColor(if (progress == 1f) selectedIconTextColor else defaultIconColor)
            val scale = (1f - progress) * (-0.2f) + 1f
            binding.iv.scaleX = scale
            binding.iv.scaleY = scale
        }

    var isEnabledCell = false
        set(value) {
            field = value
            val d = GradientDrawable()
            d.setColor(circleColor)
            d.shape = GradientDrawable.OVAL
            if (Build.VERSION.SDK_INT >= 21 && !isEnabledCell) {
                binding.fl.background = RippleDrawable(ColorStateList.valueOf(rippleColor), null, d)
            } else {
                binding.fl.runAfterDelay(200) {
                    binding.fl.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }

    var onClickListener: () -> Unit = {}
        set(value) {
            field = value
            binding.fl.setOnClickListener {
                onClickListener()
            }
        }

    private var allowDraw = false

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttributeFromXml(context, attrs)
        initializeView()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
    }

    private fun initializeView() {
        allowDraw = true
        binding = CustomBottomNavigationIconBinding.inflate(LayoutInflater.from(context), this)
        draw()
    }

    private fun draw() {
        if (!allowDraw)
            return

        icon = icon
        count = count
        iconSize = iconSize
        iconTextTypeface = iconTextTypeface
        iconTextColor = iconTextColor
        selectedIconTextColor = selectedIconTextColor
        iconTextSize = iconTextSize
        countTextColor = countTextColor
        countBackgroundColor = countBackgroundColor
        countTypeface = countTypeface
        rippleColor = rippleColor
        onClickListener = onClickListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        progress = progress
    }

    fun disableCell() {
        if (isEnabledCell)
            animateProgress(false)
        isEnabledCell = false
    }

    fun enableCell(isAnimate: Boolean = true) {
        if (!isEnabledCell)
            animateProgress(true, isAnimate)
        isEnabledCell = true
    }

    private fun animateProgress(enableCell: Boolean, isAnimate: Boolean = true) {
        val d = if (enableCell) duration else 250
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.apply {
            startDelay = if (enableCell) d / 4 else 0L
            duration = if (isAnimate) d else 1L
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                val f = it.animatedFraction
                progress = if (enableCell)
                    f
                else
                    1f - f
            }
            start()
        }
    }
}