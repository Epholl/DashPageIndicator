package dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import eu.inloop.dashpageindicator.EasyPageIndicator
import eu.inloop.dashpageindicator.R

/**
 * Created by Tomáš Isteník on 12/11/2017.
 */
class GogglyEyesPageIndicator : EasyPageIndicator {

    var swingAngle: Int = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {initAttributes(context, attrs)}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {initAttributes(context, attrs)}
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {initAttributes(context, attrs)}

    fun initAttributes(context: Context, attrs: AttributeSet) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.GogglyEyesPageIndicator, 0, 0) ?: return
        try {
            swingAngle = attr.getInt(R.styleable.GogglyEyesPageIndicator_swingAngle, DEFAULT_SWING_ANGLE)
        } finally {
            attr.recycle()
        }
    }

    override fun drawDot(canvas: Canvas, item: CurrentItem) {
        item.apply {
            val centerX = getItemCenterX(index)
            canvas.drawCircle(centerX, centerY, dp2px(activeDotRadius).toFloat(), activePaint)

            val ratio = (-difference / (itemCount-1))
            val angle: Double = (-Math.PI / 2) + ratio * (Math.PI * (swingAngle.toFloat() / 180f))
            val arcLength = Math.max(0, dp2px(activeDotRadius - inactiveDotRadius))
            val sin = (Math.sin(angle) * arcLength) + centerY
            val cos = (Math.cos(angle) * arcLength) + centerX

            canvas.drawCircle(cos.toFloat(), sin.toFloat(), dp2px(inactiveDotRadius).toFloat(), inactivePaint)
        }
    }

    companion object {
        val DEFAULT_SWING_ANGLE = 60
    }
}