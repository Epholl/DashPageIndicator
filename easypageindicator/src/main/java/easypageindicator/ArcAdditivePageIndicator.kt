package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import extensions.isLeftOf
import extensions.isRightOf

/**
 * Created by Tomáš Isteník on 05/11/2017.
 */
class ArcAdditivePageIndicator : EasyPageIndicator {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun drawDot(canvas: Canvas, item: CurrentItem) {
        item.apply {
            if (pagerPosition.current isRightOf pagerPosition.lastVisited) {
                when {
                    difference < 0 -> canvas.drawCircle(getItemCenterX(index), centerY, dp2px(activeDotRadius).toFloat(), activePaint)
                    difference < 1 -> {
                        val activeEdgeLength = dp2px(activeDotRadius*2).toFloat()
                        val inactiveEdgeLength = dp2px(inactiveDotRadius*2).toFloat()
                        val activeRect = getRectangle(getItemCenterX(index), centerY, activeEdgeLength, activeEdgeLength)
                        val inactiveRect = getRectangle(getItemCenterX(index), centerY, inactiveEdgeLength, inactiveEdgeLength)
                        val ratio = 1-distance
                        var degrees = ratio * 360
                        var startDegree = - (degrees / 2)
                        canvas.drawArc(activeRect, startDegree, degrees, true, activePaint)
                        startDegree += degrees
                        degrees = 360 - degrees
                        canvas.drawArc(inactiveRect, startDegree, degrees, true, inactivePaint)
                    }
                    difference >= 1 -> canvas.drawCircle(getItemCenterX(index), centerY, dp2px(inactiveDotRadius).toFloat(), inactivePaint)
                }
            } else if (pagerPosition.current isLeftOf pagerPosition.lastVisited) {
                when {
                    difference > 1 -> canvas.drawCircle(getItemCenterX(index), centerY, dp2px(inactiveDotRadius).toFloat(), inactivePaint)
                    difference > 0 -> {
                        val activeEdgeLength = dp2px(activeDotRadius*2).toFloat()
                        val inactiveEdgeLength = dp2px(inactiveDotRadius*2).toFloat()
                        val activeRect = getRectangle(getItemCenterX(index), centerY, activeEdgeLength, activeEdgeLength)
                        val inactiveRect = getRectangle(getItemCenterX(index), centerY, inactiveEdgeLength, inactiveEdgeLength)
                        val ratio = distance
                        var degrees = ratio * 360
                        var startDegree = 180 - (degrees / 2)
                        canvas.drawArc(activeRect, startDegree, degrees, true, inactivePaint)
                        startDegree += degrees
                        degrees = 360 - degrees
                        canvas.drawArc(inactiveRect, startDegree, degrees, true, activePaint)
                    }
                    difference <= 0 -> canvas.drawCircle(getItemCenterX(index), centerY, dp2px(activeDotRadius).toFloat(), activePaint)
                }
            } else {
                if (index isRightOf pagerPosition.current) {
                    canvas.drawCircle(getItemCenterX(index), centerY, dp2px(activeDotRadius).toFloat(), inactivePaint)
                } else {
                    canvas.drawCircle(getItemCenterX(index), centerY, dp2px(inactiveDotRadius).toFloat(), activePaint)
                }
            }
        }
    }
}