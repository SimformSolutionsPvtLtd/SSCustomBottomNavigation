@file:Suppress("unused")

package shweta.com.custombottomnavigation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.math.abs

internal typealias IBottomNavigationListener = (model: SSCustomBottomNavigation.Model) -> Unit

@Suppress("MemberVisibilityCanBePrivate")
class SSCustomBottomNavigation : FrameLayout {

    var models = ArrayList<Model>()
    var cells = ArrayList<CustomBottomNavigationIcon>()
        private set
    private var callListenerWhenIsSelected = false

    private var selectedId = -1

    private var onClickedListener: IBottomNavigationListener = {}
    private var onShowListener: IBottomNavigationListener = {}
    private var onReselectListener: IBottomNavigationListener = {}

    private var heightCell = 0
    private var isAnimating = false

    var defaultIconColor = Color.parseColor("#757575")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }
    var selectedIconColor = Color.parseColor("#00C957")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }
    var backgroundBottomColor = Color.parseColor("#FF5733")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }
    var circleColor = Color.parseColor("#ffffff")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }
    private var shadowColor = -0x454546
    var countTextColor = Color.parseColor("#9281c1")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }
    var countBackgroundColor = Color.parseColor("#ff0000")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }
    var countTypeface: Typeface? = null
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var iconTextColor = Color.parseColor("#003F87")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var selectedIconTextColor = Color.parseColor("#003F87")
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var iconTextTypeface: Typeface? = null
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var iconTextSize = 10f
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var bottomBarHeight = 100f
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var waveHeight = 7
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    private var rippleColor = Color.parseColor("#757575")

    private var allowDraw = false

    @Suppress("PrivatePropertyName")
    private lateinit var ll_cells: LinearLayout
    private lateinit var bezierView: BezierView

    init {
        heightCell = dip(context, 100) // bottom navigation height
    }

    constructor(context: Context) : super(context) {
        initializeViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributeFromXml(context, attrs)
        initializeViews()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttributeFromXml(context, attrs)
        initializeViews()
    }

    private fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SSCustomBottomNavigation, 0, 0)
        try {
            a.apply {
                defaultIconColor = getColor(R.styleable.SSCustomBottomNavigation_ss_defaultIconColor, defaultIconColor)
                selectedIconColor = getColor(R.styleable.SSCustomBottomNavigation_ss_selectedIconColor, selectedIconColor)
                backgroundBottomColor = getColor(R.styleable.SSCustomBottomNavigation_ss_backgroundBottomColor, backgroundBottomColor)
                circleColor = getColor(R.styleable.SSCustomBottomNavigation_ss_circleColor, circleColor)
                countTextColor = getColor(R.styleable.SSCustomBottomNavigation_ss_countTextColor, countTextColor)
                countBackgroundColor = getColor(R.styleable.SSCustomBottomNavigation_ss_countBackgroundColor, countBackgroundColor)
                rippleColor = getColor(R.styleable.SSCustomBottomNavigation_ss_rippleColor, rippleColor)
                shadowColor = getColor(R.styleable.SSCustomBottomNavigation_ss_shadowColor, shadowColor)
                iconTextColor = getColor(R.styleable.SSCustomBottomNavigation_ss_iconTextColor, iconTextColor)
                selectedIconTextColor = getColor(R.styleable.SSCustomBottomNavigation_ss_selectedIconTextColor, selectedIconTextColor)
                iconTextSize = getDimensionPixelSize(R.styleable.SSCustomBottomNavigation_ss_iconTextSize, dip(context, iconTextSize.toInt())).toFloat()
                bottomBarHeight = getDimension(R.styleable.SSCustomBottomNavigation_ss_bottomBarHeight, dipf(context, bottomBarHeight))
                waveHeight = getInteger(R.styleable.SSCustomBottomNavigation_ss_waveHeight, waveHeight)
                val iconTexttypeface = getString(R.styleable.SSCustomBottomNavigation_ss_iconTextTypeface)
                if (iconTexttypeface != null && iconTexttypeface.isNotEmpty())
                    iconTextTypeface = Typeface.createFromAsset(context.assets, iconTexttypeface)

                val typeface = getString(R.styleable.SSCustomBottomNavigation_ss_countTypeface)
                /* if (typeface != null && typeface.isNotEmpty())
                     countTypeface = Typeface.createFromAsset(context.assets, typeface)*/
            }
        } finally {
            a.recycle()
        }
    }

    private fun initializeViews() {
        ll_cells = LinearLayout(context)
        ll_cells.apply {
            val params = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, bottomBarHeight.toInt())
            params.gravity = Gravity.BOTTOM
            layoutParams = params
            orientation = LinearLayout.HORIZONTAL
            clipChildren = false
            clipToPadding = false
        }

        bezierView = BezierView(context)
        bezierView.apply {
            layoutParams = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (bottomBarHeight.toInt()))
            color = backgroundBottomColor
            shadowColor = this@SSCustomBottomNavigation.shadowColor
        }
        bezierView.waveHeight = waveHeight

        addView(bezierView)
        addView(ll_cells)
        allowDraw = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (selectedId == -1) {
            bezierView.bezierX = if (Build.VERSION.SDK_INT >= 17 && layoutDirection == LayoutDirection.RTL) measuredWidth + dipf(context, 72) else -dipf(context, 72)
        }
        if (selectedId != -1) {
            show(selectedId, false)
        }
    }

    fun add(model: Model) {
        val cell = CustomBottomNavigationIcon(context)
        cell.apply {
            val params = LinearLayout.LayoutParams(0, bottomBarHeight.toInt(), 1f)
            layoutParams = params
            icon = model.icon
            iconText = model.text
            count = model.count
            defaultIconColor = this@SSCustomBottomNavigation.defaultIconColor
            selectedIconColor = this@SSCustomBottomNavigation.selectedIconColor
            iconTextTypeface = this@SSCustomBottomNavigation.iconTextTypeface
            iconTextColor = this@SSCustomBottomNavigation.iconTextColor
            selectedIconTextColor = this@SSCustomBottomNavigation.selectedIconTextColor
            iconTextSize = this@SSCustomBottomNavigation.iconTextSize
            countTextColor = this@SSCustomBottomNavigation.countTextColor
            countBackgroundColor = this@SSCustomBottomNavigation.countBackgroundColor
            countTypeface = this@SSCustomBottomNavigation.countTypeface
            rippleColor = this@SSCustomBottomNavigation.rippleColor
            onClickListener = {
                if (isShowing(model.id)) // added for https://github.com/shetmobile/MeowBottomNavigation/issues/39
                    onReselectListener(model)

                if (!cell.isEnabledCell && !isAnimating) {
                    show(model.id)
                    onClickedListener(model)
                } else {
                    if (callListenerWhenIsSelected)
                        onClickedListener(model)
                }
            }
            disableCell()
            ll_cells.addView(this)
        }

        cells.add(cell)
        models.add(model)
    }

    private fun updateAllIfAllowDraw() {
        if (!allowDraw)
            return

        cells.forEach {
            it.defaultIconColor = defaultIconColor
            it.selectedIconColor = selectedIconColor
            it.circleColor = circleColor
            it.iconTextTypeface = iconTextTypeface
            it.iconTextSize =  iconTextSize
            //it.countTextColor = countTextColor
            //it.countBackgroundColor = countBackgroundColor
            //it.countTypeface = countTypeface
        }

        bezierView.color = backgroundBottomColor
    }

    private fun anim(cell: CustomBottomNavigationIcon, id: Int, enableAnimation: Boolean = true) {
        isAnimating = true

        val pos = getModelPosition(id)
        val nowPos = getModelPosition(selectedId)

        val nPos = if (nowPos < 0) 0 else nowPos
        val dif = abs(pos - nPos)
        val d = (dif) * 100L + 150L

        val animDuration = if (enableAnimation) d else 1L
        val animInterpolator = FastOutSlowInInterpolator()

        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.apply {
            duration = animDuration
            interpolator = animInterpolator
            val beforeX = bezierView.bezierX
            addUpdateListener {
                val f = it.animatedFraction
                val newX = cell.x + (cell.measuredWidth / 2)
                if (newX > beforeX)
                    bezierView.bezierX = f * (newX - beforeX) + beforeX
                else
                    bezierView.bezierX = beforeX - f * (beforeX - newX)
                if (f == 1f)
                    isAnimating = false
            }
            start()
        }

        if (abs(pos - nowPos) > 1) {
            val progressAnim = ValueAnimator.ofFloat(0f, 1f)
            progressAnim.apply {
                duration = animDuration
                interpolator = animInterpolator
                addUpdateListener {
                    val f = it.animatedFraction
                    bezierView.progress = f * 2f
                }
                start()
            }
        }

        cell.isFromLeft = pos > nowPos
        cells.forEach {
            it.duration = d
        }
    }

    fun show(id: Int, enableAnimation: Boolean = true) {
        for (i in models.indices) {
            val model = models[i]
            val cell = cells[i]
            if (model.id == id) {
                anim(cell, id, enableAnimation)
                cell.enableCell()
                onShowListener(model)
            } else {
                cell.disableCell()
            }
        }
        selectedId = id
    }

    fun isShowing(id: Int): Boolean {
        return selectedId == id
    }

    fun getModelById(id: Int): Model? {
        models.forEach {
            if (it.id == id)
                return it
        }
        return null
    }

    fun getCellById(id: Int): CustomBottomNavigationIcon? {
        return cells[getModelPosition(id)]
    }

    fun getModelPosition(id: Int): Int {
        for (i in models.indices) {
            val item = models[i]
            if (item.id == id)
                return i
        }
        return -1
    }

    fun setCount(id: Int, count: String) {
        val model = getModelById(id) ?: return
        val pos = getModelPosition(id)
        model.count = count
        cells[pos].count = count
    }

    fun clearCount(id: Int) {
        val model = getModelById(id) ?: return
        val pos = getModelPosition(id)
        model.count = CustomBottomNavigationIcon.EMPTY_VALUE
        cells[pos].count = CustomBottomNavigationIcon.EMPTY_VALUE
    }

    fun clearAllCounts() {
        models.forEach {
            clearCount(it.id)
        }
    }

    fun setOnShowListener(listener: IBottomNavigationListener) {
        onShowListener = listener
    }

    fun setOnClickMenuListener(listener: IBottomNavigationListener) {
        onClickedListener = listener
    }

    fun setOnReselectListener(listener: IBottomNavigationListener) {
        onReselectListener = listener
    }

    class Model(var id: Int, var icon: Int, var text: String) {

        var count: String = CustomBottomNavigationIcon.EMPTY_VALUE

    }
}