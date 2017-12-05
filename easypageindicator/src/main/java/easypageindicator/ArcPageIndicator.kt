package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet

/**
 * Created by Tomáš Isteník on 05/11/2017.
 */
class ArcPageIndicator : EasyPageIndicator {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun drawDot(canvas: Canvas, item: CurrentItem) {
        item.apply {
            canvas.drawCircle(getItemCenterX(index), centerY, dp2px(inactiveDotRadius).toFloat(), inactivePaint)
            if (distance < 1) {
                val edgeLength = dp2px(activeDotRadius*2).toFloat()
                val rect = getRectangle(getItemCenterX(index), centerY, edgeLength, edgeLength)
                if (difference < 0) {
                    val ratio = distance
                    val degrees = 360 - ratio * 360
                    val startDegree = 180 - (degrees / 2)
                    canvas.drawArc(rect, startDegree, degrees, true, activePaint)
                } else {
                    val ratio = 1 - distance
                    val degrees = ratio * 360
                    val startDegree = - (degrees / 2)
                    canvas.drawArc(rect, startDegree, degrees, true, activePaint)
                }
            }
        }
    }
}