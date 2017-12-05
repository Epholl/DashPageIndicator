package eu.inloop.dashpageindicator

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import extensions.isLeftOf
import extensions.isRightOf

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

    var pacManMouthOpenDegrees = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {initAttributes(context, attrs)}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {initAttributes(context, attrs)}
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {initAttributes(context, attrs)}

    fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.PacManPageIndicator, 0, 0) ?: return
        try {
            pacManColor = attr.getColor(R.styleable.PacManPageIndicator_pacManColor, ContextCompat.getColor(context, DEFAULT_PAC_MAN_COLOR))
            pacManMouthOpenDegrees = attr.getInt(R.styleable.PacManPageIndicator_pacManOpenMouthDegrees, DEFAULT_PAC_MAN_OPEN_MOUTH_DEGREES)
        } finally {
            attr.recycle()
        }
    }

    override fun drawDot(canvas: Canvas, item: CurrentItem) {
        item.apply {
            if (index isRightOf pagerPosition.current) {
                canvas.drawCircle(getItemCenterX(index), centerY, dp2px(inactiveDotRadius).toFloat(), activePaint)
            } else {
                canvas.drawCircle(getItemCenterX(index), centerY, dp2px(inactiveDotRadius).toFloat(), inactivePaint)
            }
        }
    }

    override fun afterDraw(canvas: Canvas) {
        super.afterDraw(canvas)
        val edgeLength = dp2px(activeDotRadius*2).toFloat()
        val rect = getRectangle(getItemCenterX(pagerPosition.current), centerY, edgeLength, edgeLength)
        when {
            pagerPosition.current isRightOf pagerPosition.lastVisited -> {
                val ratio = Math.min(pagerPosition.current % 1, 1 - (pagerPosition.current % 1))
                val degrees = 360 - (2 * ratio * pacManMouthOpenDegrees)
                val startDegree = 180 - (degrees / 2)
                canvas.drawArc(rect, startDegree, degrees, true, pacManPaint)
            }
            pagerPosition.current isLeftOf pagerPosition.lastVisited -> {
                val ratio = Math.min(pagerPosition.current % 1, 1 - (pagerPosition.current % 1))
                val degrees = 360 - (2 * ratio * pacManMouthOpenDegrees)
                val startDegree = - (degrees / 2)
                canvas.drawArc(rect, startDegree, degrees, true, pacManPaint)
            }
            else ->
                canvas.drawCircle(getItemCenterX(pagerPosition.current), centerY, dp2px(activeDotRadius).toFloat(), pacManPaint)
        }
    }

    companion object {
        private val DEFAULT_PAC_MAN_COLOR = R.color.pac_man_color
        private val DEFAULT_PAC_MAN_OPEN_MOUTH_DEGREES = 180;
    }
}