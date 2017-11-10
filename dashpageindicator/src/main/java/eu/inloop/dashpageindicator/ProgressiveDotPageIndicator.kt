package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet

/**
 * Created by Tomáš Isteník on 05/11/2017.
 */
class ProgressiveDotPageIndicator : StandardDotPageIndicator {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun drawDot(canvas: Canvas, position: Int) {
        val size = Math.max(dp2px(activeDotRadius) / (1 + distance), dp2px(inactiveDotRadius).toFloat())
        val color = lerp(activeColor, inactiveColor, 1 / (1 + distance))
        activePaint.color = color
        canvas.drawCircle(getItemCenterX(position), centerY, size, activePaint)
    }
}