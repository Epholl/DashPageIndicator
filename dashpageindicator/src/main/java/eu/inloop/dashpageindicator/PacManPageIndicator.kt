package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet

/**
 * Created by Tomáš Isteník on 09/11/2017.
 */
class PacManPageIndicator : EasyPageIndicator {

    var pacManColor: Int = 0
        set(value) {
            field = value
            pacManPaint.color = value
        }

    val pacManPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun initCustomAttributes(context: Context, attrs: AttributeSet?) {
        super.initCustomAttributes(context, attrs)
        val attr = context.obtainStyledAttributes(attrs, R.styleable.PacManPageIndicator, 0, 0) ?: return
        try {
            pacManColor = attr.getColor(R.styleable.PacManPageIndicator_pacManColor, dp2px(DEFAULT_PAC_MAN_COLOR))
        } finally {
            attr.recycle()
        }
    }

    override fun drawDot(canvas: Canvas, position: Int) {
        if (isAfter) {
            canvas.drawCircle(getItemCenterX(position), centerY, dp2px(inactiveDotRadius).toFloat(), activePaint)
        } else {
            canvas.drawCircle(getItemCenterX(position), centerY, dp2px(inactiveDotRadius).toFloat(), inactivePaint)
        }
    }

    override fun afterDraw(canvas: Canvas) {
        super.afterDraw(canvas)
        val edgeLength = dp2px(activeDotRadius*2).toFloat()
        val rect = getRectangle(getItemCenterX(currentViewPagerPosition), centerY, edgeLength, edgeLength)
        if (isRightOfPreviousPosition) {
            val ratio = Math.min(currentViewPagerPosition % 1, 1 - (currentViewPagerPosition % 1))
            val degrees = 360 - ratio * 360
            val startDegree = 180 - (degrees / 2)
            canvas.drawArc(rect, startDegree, degrees, true, pacManPaint)
        } else {
            val ratio = 1 - distance
            val degrees = ratio * 360
            val startDegree = - (degrees / 2)
            canvas.drawArc(rect, startDegree, degrees, true, pacManPaint)
        }
    }

    companion object {
        private val DEFAULT_PAC_MAN_COLOR = R.color.pac_man_color
    }
}