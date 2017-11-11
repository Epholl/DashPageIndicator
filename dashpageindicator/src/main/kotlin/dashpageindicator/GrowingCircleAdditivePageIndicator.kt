package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet

/**
 * Created by Tomáš Isteník on 05/11/2017.
 */
class GrowingCircleAdditivePageIndicator : EasyPageIndicator {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun drawDot(canvas: Canvas, position: Int) {
        if (isRightOfPreviousPosition) {
            when {
                difference < 0 -> canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), activePaint)
                difference < 1 -> {
                    canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), inactivePaint)
                    canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat()*(1-distance), activePaint)
                }
                difference >= 1 -> canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), inactivePaint)
            }
        } else if (isLeftOfPreviousPosition) {
            when {
                difference > 1 -> canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), inactivePaint)
                difference > 0 -> {
                            canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), activePaint)
                    canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat()*distance, inactivePaint)
                }
                difference <= 0 -> canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), activePaint)
            }
        } else {
            if (!isAfter) {
                canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), activePaint)
            } else {
                canvas.drawCircle(getItemCenterX(position), centerY, dp2px(activeDotRadius).toFloat(), inactivePaint)
            }
        }

    }
}