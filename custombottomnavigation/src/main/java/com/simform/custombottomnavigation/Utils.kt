package com.simform.custombottomnavigation

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Created by 1HE on 2/23/2019.
 */

private fun getDP(context: Context) = context.resources.displayMetrics.density

internal fun dipf(context: Context, f: Float) = f * getDP(context)

internal fun dipf(context: Context, i: Int) = i * getDP(context)

internal fun dip(context: Context, i: Int) = (i * getDP(context)).toInt()

internal fun toDP(context: Context,value: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(),context.resources.displayMetrics).toInt()

internal fun toPx(context: Context,value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value,context.resources.displayMetrics)

internal object DrawableHelper {

    fun changeColorDrawableVector(c: Context?, resDrawable: Int, color: Int): Drawable? {
        if (c == null)
            return null

        val d = VectorDrawableCompat.create(c.resources, resDrawable, null) ?: return null
        d.mutate()
        if (color != -2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                d.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            } else {
                d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
        return d
    }

    fun changeColorDrawableRes(c: Context?, resDrawable: Int, color: Int): Drawable? {
        if (c == null)
            return null

        val d = ContextCompat.getDrawable(c, resDrawable) ?: return null
        d.mutate()
        if (color != -2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                d.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            } else {
                d.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }

        }
        return d
    }
}

internal object ColorHelper {

    fun mixTwoColors(color1: Int, color2: Int, amount: Float): Int {
        val alphaChannel = 24
        val redChannel = 16
        val greenChannel = 8

        val inverseAmount = 1.0f - amount

        val a =
            ((color1 shr alphaChannel and 0xff).toFloat() * amount + (color2 shr alphaChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val r =
            ((color1 shr redChannel and 0xff).toFloat() * amount + (color2 shr redChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val g =
            ((color1 shr greenChannel and 0xff).toFloat() * amount + (color2 shr greenChannel and 0xff).toFloat() * inverseAmount).toInt() and 0xff
        val b =
            ((color1 and 0xff).toFloat() * amount + (color2 and 0xff).toFloat() * inverseAmount).toInt() and 0xff

        return a shl alphaChannel or (r shl redChannel) or (g shl greenChannel) or b
    }
}

internal fun Context.getDrawableCompat(res: Int) = ContextCompat.getDrawable(this, res)

internal inline fun <T : View?> T.runAfterDelay(delay: Long, crossinline f: T.() -> Unit) {
    this?.postDelayed({
        try {
            f()
        } catch (e: Exception) {
        }
    }, delay)
}
