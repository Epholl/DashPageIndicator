package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import extensions.isLeftOf
import extensions.isRightOf

abstract class EasyPageIndicator : View {


    var activeColor: Int = 0
        set(value) {
            field = value
            activePaint.color = value
        }

    var inactiveColor: Int = 0
        set(value) {
            field = value
            inactivePaint.color = value
        }

    var activeDotRadius: Int = 0

    var inactiveDotRadius: Int = 0

    var itemWidth: Int = 0

    var itemCount: Int = 0

    var centerY: Float = 0f
    var leftX: Int = 0

    var activePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var inactivePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    val currentItem = CurrentItem()

    inner class CurrentItem {
        var index: Int = 0
            private set

        val difference: Float get() = index - pagerPosition.current

        val distance: Float get() = Math.abs(difference)

        val isBefore: Boolean get() = index isLeftOf pagerPosition.current

        val isAfter: Boolean get() = index isRightOf pagerPosition.current

        val activeRatio: Float get() = if (distance < 1) (1 - distance) else 0f

        val inactiveRatio: Float get() = 1 - activeRatio

        fun update(index: Int) {
            this.index = index
        }
    }

    val pagerPosition = PagerPosition()

    inner class PagerPosition {
        var current: Float = 0f
            private set

        var previous: Float = 0f
            private set

        var lastVisited: Int = 0
            private set

        val delta: Float get() = current - previous

        val isMovingRight: Boolean get() = current isRightOf previous

        val isMovingLeft: Boolean get() = current isLeftOf previous

        val totalCompletionRatio: Float get() = if (itemCount <= 1) 1f else (current) / (itemCount - 1)

        fun update(position: Float) {
            previous = current
            current = position
            updateLastVisited()
        }

        private fun updateLastVisited() {
            if (isMovingRight && Math.floor(current.toDouble()).toInt() != Math.floor(previous.toDouble()).toInt()) {
                lastVisited = Math.round(current)
            } else if (isMovingLeft && Math.ceil(current.toDouble()).toInt() != Math.ceil(previous.toDouble()).toInt()) {
                lastVisited = Math.round(current)
            }
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        invalidate()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.EasyPageIndicator, 0, 0) ?: return
        try {
            itemWidth = attr.getDimension(R.styleable.EasyPageIndicator_itemWidth, dp2px(DEFAULT_ITEM_WIDTH_DP).toFloat()).toInt()
            activeColor = attr.getColor(R.styleable.EasyPageIndicator_activeColor, ContextCompat.getColor(context, DEFAULT_ITEM_COLOR))
            inactiveColor = attr.getColor(R.styleable.EasyPageIndicator_inactiveColor, ContextCompat.getColor(context, DEFAULT_ITEM_COLOR))
            activeDotRadius = attr.getDimension(R.styleable.EasyPageIndicator_activeRadius, dp2px(DEFAULT_ACTIVE_DOT_WIDTH_DP).toFloat()).toInt()
            inactiveDotRadius = attr.getDimension(R.styleable.EasyPageIndicator_inactiveRadius, dp2px(DEFAULT_INACTIVE_DOT_WIDTH_DP).toFloat()).toInt()
        } finally {
            attr.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val resultWidth: Int
        val resultHeight: Int

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        if (View.MeasureSpec.EXACTLY == widthMode) {
            resultWidth = widthSize
        } else if (View.MeasureSpec.AT_MOST == widthMode) {
            resultWidth = Math.min(suggestedMinimumWidth, widthSize)
        } else {
            resultWidth = suggestedMinimumWidth
        }

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (View.MeasureSpec.EXACTLY == heightMode) {
            resultHeight = heightSize
        } else if (View.MeasureSpec.AT_MOST == heightMode) {
            resultHeight = Math.min(suggestedMinimumHeight, heightSize)
        } else {
            resultHeight = suggestedMinimumHeight
        }

        setMeasuredDimension(resultWidth, resultHeight)

        recalculatePivots()
    }

    override fun getSuggestedMinimumWidth(): Int {
        return itemWidth * itemCount
    }

    override fun getSuggestedMinimumHeight(): Int {
        return dp2px(DEFAULT_HEIGHT_DP)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalculatePivots()
        invalidate()
    }

    fun getRectangle(centerX: Float, centerY: Float, width: Float, height: Float): RectF {
        val left = centerX - (width / 2)
        val right = centerX + (width / 2)
        val bottom = centerY + (height / 2)
        val top = centerY - (height / 2)
        return RectF(left, top, right, bottom)
    }

    private fun recalculatePivots() {
        leftX = (width - contentWidth()) / 2 + (itemWidth / 2)
        centerY = (height / 2).toFloat()
    }

    protected open fun getItemCenterX(index: Float): Float {
        if (index < 0 || index >= itemCount) {
            throw IllegalArgumentException("Requested index $index out of range 0 - $itemCount")
        } else {
            return (leftX + (index * itemWidth))
        }
    }

    protected fun getItemCenterX(index: Int): Float {
        return getItemCenterX(index.toFloat())
    }

    /**
     * Computed width in pixels that is needed for dots.
     */
    private fun contentWidth(): Int {
        return itemWidth * itemCount
    }

    /**
     * Setups itself and sets listeners on 'viewPager'.
     */
    fun setViewPager(viewPager: ViewPager) {
        if (viewPager.adapter == null) {
            throw IllegalArgumentException("ViewPager must have PagerAdapter set")
        }
        itemCount = viewPager.adapter.count
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                setScrollState(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setScrollState(position: Int, positionOffset: Float) {
        pagerPosition.update(position + positionOffset)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (itemCount < 1) {
            visibility = View.GONE
            return
        }
        super.onDraw(canvas)

        beforeDraw(canvas)
        for (i in 0 until itemCount) {
            currentItem.update(i)
            drawDot(canvas, currentItem)
        }
        afterDraw(canvas)

    }

    open fun beforeDraw(canvas: Canvas) {}

    open fun drawDot(canvas: Canvas, item: CurrentItem) {}

    open fun afterDraw(canvas: Canvas) {}

    protected fun dp2px(dp: Float): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    protected fun dp2px(dp: Int): Int {
        return dp2px(dp.toFloat())
    }

    protected fun lerp(color1: Int, color2: Int, ratio: Float): Int {
        val red = (Color.red(color1) * ratio + Color.red(color2) * (1 - ratio)).toInt()
        val green = (Color.green(color1) * ratio + Color.green(color2) * (1 - ratio)).toInt()
        val blue = (Color.blue(color1) * ratio + Color.blue(color2) * (1 - ratio)).toInt()
        val alpha = (Color.alpha(color1) * ratio + Color.alpha(color2) * (1 - ratio)).toInt()
        return Color.argb(alpha, red, green, blue)
    }

    companion object {
        private val DEFAULT_ITEM_WIDTH_DP = 12
        private val DEFAULT_HEIGHT_DP = 10
        private val DEFAULT_ITEM_COLOR = R.color.page_indicator_active
        private val DEFAULT_ACTIVE_DOT_WIDTH_DP = 2.5f
        private val DEFAULT_INACTIVE_DOT_WIDTH_DP = 1.5f
    }
}
