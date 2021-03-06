package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import extensions.isLeftOf
import extensions.isRightOf

/**
 * Created by Tomáš Isteník on 08/11/2017.
 */
class DashPageIndicator : EasyPageIndicator {

    var dashWidth: Int = 0

    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {initAttributes(context, attrs)}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {initAttributes(context, attrs)}
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {initAttributes(context, attrs)}

    fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.DashPageIndicator, 0, 0) ?: return
        try {
            dashWidth = attr.getDimension(R.styleable.DashPageIndicator_dashWidth, dp2px(DEFAULT_DASH_WIDTH_DP).toFloat()).toInt()
        } finally {
            attr.recycle()
        }
    }

    override fun getSuggestedMinimumWidth(): Int {
        return super.getSuggestedMinimumWidth() + dashWidth
    }

    fun getOffsetItemCenterX(index: Float): Float {
        var centerX = getItemCenterX(index)

        return when {
            index isLeftOf pagerPosition.current -> centerX - (dashWidth / 2)
            index isRightOf pagerPosition.current -> centerX + (dashWidth / 2)
            else -> centerX
        }
    }

    override fun drawDot(canvas: Canvas, item: CurrentItem) {
        item.apply {

            if (distance >= 1) {
                canvas.drawCircle(getOffsetItemCenterX(index.toFloat()), centerY, dp2px(inactiveDotRadius).toFloat(), inactivePaint)
            } else {
                val ratio = 1 - distance
                val activeCenter = getItemCenterX(index)
                val inactiveCenter = getOffsetItemCenterX(index.toFloat())
                val realCenter = (ratio * activeCenter) + ((1-ratio) * inactiveCenter)

                val color = lerp(activeColor, inactiveColor, ratio)
                paint.color = color

                drawDash(canvas, realCenter, activeDotRadius.toFloat(), ratio, paint)
            }
        }

    }

    fun drawDash(canvas: Canvas, center: Float, radius: Float, ratio: Float, paint: Paint) {
        val roundedDashWidth = (dashWidth - (activeDotRadius*2)) * ratio
        val left = center - (roundedDashWidth/2)
        val right = center + (roundedDashWidth/2)
        canvas.drawCircle(left, centerY, dp2px(radius).toFloat(), paint)
        canvas.drawCircle(right, centerY, dp2px(radius).toFloat(), paint)
        canvas.drawRect(getRectangle(center, centerY, roundedDashWidth, (radius*4).toFloat()), paint)
    }

    companion object {
        private val DEFAULT_DASH_WIDTH_DP = 16
    }
}