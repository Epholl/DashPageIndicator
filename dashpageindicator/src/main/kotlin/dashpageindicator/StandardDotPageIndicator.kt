package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet

/**
 * Created by Tomáš Isteník on 15/10/2017.
 */
open class StandardDotPageIndicator : EasyPageIndicator {


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun getSuggestedMinimumHeight(): Int {
        return dp2px(activeDotRadius)*2
    }

    override fun drawDot(canvas: Canvas, position: Int) {
        val size = activeRatio * dp2px(activeDotRadius) + inactiveRatio * dp2px(inactiveDotRadius)
        val color = lerp(activeColor, inactiveColor, activeRatio)
        activePaint.color = color
        canvas.drawCircle(getItemCenterX(position), centerY, size, activePaint)
    }
}