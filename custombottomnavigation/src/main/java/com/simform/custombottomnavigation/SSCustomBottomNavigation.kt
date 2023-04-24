@file:Suppress("unused")

package com.simform.custombottomnavigation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.LayoutDirection
import android.util.Log
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import com.google.android.material.internal.ContextUtils.getActivity
import kotlin.math.abs

internal typealias IBottomNavigationListener = (model: Model) -> Unit

@Suppress("MemberVisibilityCanBePrivate")
class SSCustomBottomNavigation : FrameLayout {

    // initialize empty array so that we don't have to check if it's initialized or not
    private var cbnMenuItems: Array<Model> = arrayOf()
    var models = ArrayList<Model>()
    var cells = ArrayList<CustomBottomNavigationIcon>()
        private set
    private var callListenerWhenIsSelected = false

    private var selectedIndex = -1

    private var onClickedListener: IBottomNavigationListener = {}
    private var onShowListener: IBottomNavigationListener = {}
    private var onReselectListener: IBottomNavigationListener = {}

    private var heightCell = 0
    private var isAnimating = false

    // listener for the menuItemClick
    private var menuItemClickListener: ((Model, Int) -> Unit)? = null

    // control the rendering of the menu when the menu is empty
    private var isMenuInitialized = false

    private var animatorSet = AnimatorSet()

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
    var backgroundBottomDrawable =
        AppCompatResources.getDrawable(context, R.drawable.bottom_drawable_default)
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

    var waveHeight = 7
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    var isReverseCurve = false
        set(value) {
            field = value
            updateAllIfAllowDraw()
        }

    fun setSelectedIndex(activeIndex: Int = 0) {
        selectedIndex = activeIndex
    }

    fun getSelectedIndex(): Int {
        return selectedIndex
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

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttributeFromXml(context, attrs)
        initializeViews()
    }

    private fun setAttributeFromXml(context: Context, attrs: AttributeSet) {
        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SSCustomBottomNavigation, 0, 0)
        try {
            a.apply {
                defaultIconColor = getColor(
                    R.styleable.SSCustomBottomNavigation_ss_defaultIconColor,
                    defaultIconColor
                )
                selectedIconColor = getColor(
                    R.styleable.SSCustomBottomNavigation_ss_selectedIconColor,
                    selectedIconColor
                )
                backgroundBottomColor = getColor(
                    R.styleable.SSCustomBottomNavigation_ss_backgroundBottomColor,
                    backgroundBottomColor
                )
                circleColor =
                    getColor(R.styleable.SSCustomBottomNavigation_ss_circleColor, circleColor)
                countTextColor =
                    getColor(R.styleable.SSCustomBottomNavigation_ss_countTextColor, countTextColor)
                countBackgroundColor = getColor(
                    R.styleable.SSCustomBottomNavigation_ss_countBackgroundColor,
                    countBackgroundColor
                )
                rippleColor =
                    getColor(R.styleable.SSCustomBottomNavigation_ss_rippleColor, rippleColor)
                shadowColor =
                    getColor(R.styleable.SSCustomBottomNavigation_ss_shadowColor, shadowColor)
                iconTextColor =
                    getColor(R.styleable.SSCustomBottomNavigation_ss_iconTextColor, iconTextColor)
                selectedIconTextColor = getColor(
                    R.styleable.SSCustomBottomNavigation_ss_selectedIconTextColor,
                    selectedIconTextColor
                )
                iconTextSize = getDimensionPixelSize(
                    R.styleable.SSCustomBottomNavigation_ss_iconTextSize,
                    dip(context, iconTextSize.toInt())
                ).toFloat()
                waveHeight =
                    getInteger(R.styleable.SSCustomBottomNavigation_ss_waveHeight, waveHeight)

                isReverseCurve = getBoolean(R.styleable.SSCustomBottomNavigation_ss_reverseCurve, isReverseCurve)
                val iconTextTypeFace =
                    getString(R.styleable.SSCustomBottomNavigation_ss_iconTextTypeface)
                if (TextUtils.isEmpty(iconTextTypeFace))
                    iconTextTypeface = Typeface.createFromAsset(context.assets, iconTextTypeFace)

                val typeface = getString(R.styleable.SSCustomBottomNavigation_ss_countTypeface)
                if (TextUtils.isEmpty(typeface))
                    countTypeface = Typeface.createFromAsset(context.assets, typeface)

                val drawable =
                    a.getDrawable(R.styleable.SSCustomBottomNavigation_ss_backgroundBottomDrawable)
                drawable?.let {
                    backgroundBottomDrawable = it
                }
            }
        } finally {
            a.recycle()
        }
    }

    private fun initializeViews() {
        ll_cells = LinearLayout(context)
        ll_cells.apply {
            val params = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightCell)
            params.gravity = Gravity.BOTTOM
            layoutParams = params
            orientation = LinearLayout.HORIZONTAL
            clipChildren = false
            clipToPadding = false
        }

        bezierView = BezierView(context)
        bezierView.apply {
            layoutParams = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (heightCell))
            color = backgroundBottomColor
            shadowColor = this@SSCustomBottomNavigation.shadowColor
            isReverseCurve = this@SSCustomBottomNavigation.isReverseCurve
        }
        bezierView.waveHeight = waveHeight

        addView(bezierView)
        addView(ll_cells)
        allowDraw = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (selectedIndex == -1) {
            bezierView.bezierX =
                if (Build.VERSION.SDK_INT >= 21 && layoutDirection == LayoutDirection.RTL) measuredWidth + dipf(
                    context,
                    72
                ) else -dipf(context, 72)
        }
        if (selectedIndex != -1) {
            Log.e("selectedIndex", " $selectedIndex")
            val imm: InputMethodManager = getActivity(context)?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (!imm.isAcceptingText) show(selectedIndex, false)
        }
    }

    fun setMenuItems(models: Array<Model>, activeIndex: Int = 0) {
        if (models.isEmpty()) {
            isMenuInitialized = false
            return
        }

        this.cbnMenuItems = models
        isMenuInitialized = true
        selectedIndex = activeIndex

        this.cbnMenuItems.forEachIndexed { _, cbnMenuItem ->
            add(
                Model(
                    destinationId = cbnMenuItem.destinationId,
                    icon = cbnMenuItem.icon,
                    id = cbnMenuItem.id,
                    text = cbnMenuItem.text,
                    count = cbnMenuItem.count
                )
            )
        }
    }

    fun getMenuItems(): Array<Model> {
        return cbnMenuItems
    }

    // set the click listener for menu items
    fun setOnMenuItemClickListener(menuItemClickListener: (Model, Int) -> Unit) {
        this.menuItemClickListener = menuItemClickListener
    }

    // function to setup with navigation controller just like in BottomNavigationView
    fun setupWithNavController(navController: NavController) {
        // check for menu initialization
        if (!isMenuInitialized) {
            throw RuntimeException("initialize menu by calling setMenuItems() before setting up with NavController")
        }

        // initialize the menu
        setOnMenuItemClickListener { item, _ ->
            navigateToDestination(navController, item)
        }
        // setup destination change listener to properly sync the back button press
        navController.addOnDestinationChangedListener { _, destination, _ ->
            for (i in cbnMenuItems.indices) {
                if (matchDestination(destination, cbnMenuItems[i].destinationId)) {
                    if (selectedIndex != i && isAnimating) {
                        // this is triggered internally, even if the animations looks kinda funky (if duration is long)
                        // but we will sync with the destination
                        animatorSet.cancel()
                        isAnimating = false
                    }
                }
            }
        }
    }

    fun onMenuItemClick(menuItemPos: Int) {
        if (selectedIndex == menuItemPos) {
            Log.i("TAG", "same icon multiple clicked, skipping animation!")
            return
        }
        if (isAnimating) {
            Log.i("TAG", "animation is in progress, skipping navigation")
            return
        }

        selectedIndex = menuItemPos
        isAnimating = true

        // notify the listener
        menuItemClickListener?.invoke(cbnMenuItems[menuItemPos], menuItemPos)

    }

    // source code referenced from the actual JetPack Navigation Component
    // refer to the original source code
    private fun navigateToDestination(navController: NavController, itemCbn: Model) {
        if (itemCbn.destinationId == -1) {
            throw RuntimeException("please set a valid id, unable the navigation!")
        }
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
        // pop to the navigation graph's start  destination
        builder.setPopUpTo(findStartDestination(navController.graph).id, false)
        val options = builder.build()
        try {
            navController.popBackStack()
            navController.navigate(itemCbn.destinationId, null, options)
        } catch (e: IllegalArgumentException) {
            Log.w("TAG", "unable to navigate!", e)
        }
    }

    // source code referenced from the actual JetPack Navigation Component
    // refer to the original source code
    private fun matchDestination(destination: NavDestination, @IdRes destinationId: Int): Boolean {
        var currentDestination = destination
        while (currentDestination.id != destinationId && currentDestination.parent != null) {
            currentDestination.parent?.let {
                currentDestination = it
            }
        }

        return currentDestination.id == destinationId
    }

    // source code referenced from the actual JetPack Navigation Component
    // refer to the original source code
    private fun findStartDestination(graph: NavGraph): NavDestination {
        var startDestination: NavDestination = graph
        while (startDestination is NavGraph) {
            graph.findNode(graph.startDestinationId)?.let {
                startDestination = it
            }
        }

        return startDestination
    }

    fun add(model: Model) {
        val cell = CustomBottomNavigationIcon(context)
        cell.apply {
            val params = LinearLayout.LayoutParams(0, heightCell, 1f)
            layoutParams = params
            icon = model.icon
            iconText = context.getString(model.text)
            count = context.getString(model.count)
            defaultIconColor = this@SSCustomBottomNavigation.defaultIconColor
            selectedIconColor = this@SSCustomBottomNavigation.selectedIconColor
            iconTextTypeface = this@SSCustomBottomNavigation.iconTextTypeface
            iconTextColor = this@SSCustomBottomNavigation.iconTextColor
            selectedIconTextColor = this@SSCustomBottomNavigation.selectedIconTextColor
            iconTextSize = this@SSCustomBottomNavigation.iconTextSize
            countTextColor = this@SSCustomBottomNavigation.countTextColor
            countBackgroundColor = this@SSCustomBottomNavigation.countBackgroundColor
            backgroundBottomColor = this@SSCustomBottomNavigation.backgroundBottomColor
            backgroundBottomDrawable = this@SSCustomBottomNavigation.backgroundBottomDrawable
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
            it.iconTextSize = iconTextSize
            it.countTextColor = countTextColor
            it.countBackgroundColor = countBackgroundColor
            it.countTypeface = countTypeface
        }

        bezierView.color = backgroundBottomColor
    }

    private fun anim(cell: CustomBottomNavigationIcon, id: Int, enableAnimation: Boolean = true) {
        isAnimating = true

        val pos = getModelPosition(id)
        val nowPos = getModelPosition(selectedIndex)

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
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    isAnimating = false
                }
            })
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
                onShowListener(model)
                menuItemClickListener?.invoke(cbnMenuItems[i], i)
                anim(cell, id, enableAnimation)
                cell.enableCell()
            } else {
                cell.disableCell()
            }
        }
        selectedIndex = id
    }

    fun isShowing(id: Int): Boolean {
        return selectedIndex == id
    }

    fun getModelById(id: Int): Model? {
        models.forEach {
            if (it.id == id)
                return it
        }
        return null
    }

    fun getCellById(id: Int): CustomBottomNavigationIcon {
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

    fun setCount(id: Int, count: Int) {
        val model = getModelById(id) ?: return
        val pos = getModelPosition(id)
        model.count = count
        cells[pos].count = context.getString(count)
    }

    fun clearCount(id: Int) {
        val model = getModelById(id) ?: return
        val pos = getModelPosition(id)
        model.count = R.string.empty_value
        cells[pos].count = context.getString(R.string.empty_value)
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
}