package eu.inloop.dashpageindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class DashPageIndicator extends View {

    private static final int DEFAULT_RADIUS = 4;
    private static final int DEFAULT_PAGE_COUNT = 3;
    private static final int DEFAULT_INDICATOR_INACTIVE = R.color.page_indicator_inactive;
    private static final int DEFAULT_INDICATOR_ACTIVE = R.color.page_indicator_active;
    private static final int DEFAULT_CURRENT_PAGE = 1;
    private static final int DEFAULT_SPACE = DEFAULT_RADIUS * 2;
    private static final int DEFAULT_ACTIVE_WIDTH = DEFAULT_RADIUS * 4;

    private int dotRadius;
    private int activeDotWidth;
    private int activeColor;
    private int inactiveColor;
    private int pageCount;
    private int currentPage;
    private float offsetFactor;

    private float dotSpace;
    private int centerY;
    private int leftX;
    private Paint inactiveDotPaint;
    private Paint activeDotPaint;
    private int targetPage;

    public DashPageIndicator(final Context context) {
        super(context);
        init(context, null);
    }

    public DashPageIndicator(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DashPageIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DashPageIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        initAttributes(context, attrs);
        preparePaints();
        invalidate();
    }

    private void preparePaints() {
        activeDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactiveDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initAttributes(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.DashPageIndicator, 0, 0);
        if (attr == null) {
            return;
        }
        try {
            dotRadius = (int) attr.getDimension(R.styleable.DashPageIndicator_dotRadius, dp2px(DEFAULT_RADIUS));
            activeDotWidth = (int) attr.getDimension(R.styleable.DashPageIndicator_activeDotWidth, dp2px(DEFAULT_ACTIVE_WIDTH));
            activeColor = attr.getColor(R.styleable.DashPageIndicator_activeColor, ContextCompat.getColor(getContext(), DEFAULT_INDICATOR_ACTIVE));
            inactiveColor = attr.getColor(R.styleable.DashPageIndicator_inactiveColor, ContextCompat.getColor(getContext(), DEFAULT_INDICATOR_INACTIVE));
            pageCount = attr.getInt(R.styleable.DashPageIndicator_pageCount, DEFAULT_PAGE_COUNT);
            currentPage = attr.getInt(R.styleable.DashPageIndicator_currentPage, DEFAULT_CURRENT_PAGE);
            dotSpace = attr.getDimension(R.styleable.DashPageIndicator_dotSpace, dp2px(DEFAULT_SPACE));
        } finally {
            attr.recycle();
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        int resultWidth;
        int resultHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.EXACTLY == widthMode) {
            resultWidth = widthSize;
        } else if (MeasureSpec.AT_MOST == widthMode) {
            resultWidth = Math.min(getSuggestedMinimumWidth(), widthSize);
        } else {
            resultWidth = getSuggestedMinimumWidth();
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == heightMode) {
            resultHeight = heightSize;
        } else if (MeasureSpec.AT_MOST == heightMode) {
            resultHeight = Math.min(getSuggestedMinimumHeight(), heightSize);
        } else {
            resultHeight = getSuggestedMinimumHeight();
        }

        setMeasuredDimension(resultWidth, resultHeight);

        recalculatePivots();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return contentWidth();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return dotRadius * 2;
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recalculatePivots();
        invalidate();
    }

    private void recalculatePivots() {
        leftX = (getWidth() - contentWidth()) / 2 + dotRadius;
        centerY = getHeight() / 2;
    }

    /**
     * Computed width in pixels that is needed for dots.
     */
    private int contentWidth() {
        //simplified '(pageCount - 1) * dotRadius * 2 + (pageCount - 1) * dotSpace + activeDotWidth'
        return (int) Math.ceil((pageCount - 1) * (dotRadius * 2 + dotSpace) + activeDotWidth + dotRadius);
    }

    /**
     * Setups itself and sets listeners on 'viewPager'.
     */
    public void setViewPager(@NonNull final ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            throw new IllegalArgumentException("ViewPager must have PagerAdapter set");
        }
        pageCount = viewPager.getAdapter().getCount();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                setScrollState(position, positionOffset);
            }

            @Override
            public void onPageSelected(final int position) {
                setScrollState(position, 0);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        if (viewPager.getCurrentItem() < pageCount) {
            currentPage = viewPager.getCurrentItem();
        }
    }

    private void setScrollState(final int position, final float positionOffset) {
        if (positionOffset == 0) {
            //final state
            currentPage = position;
            targetPage = position;
        } else if (position >= currentPage) {
            //moving to right
            targetPage = position + 1;
            offsetFactor = positionOffset;
        } else {
            //moving to left
            targetPage = position;
            offsetFactor = positionOffset - 1;
        }
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (pageCount < 1) {
            setVisibility(GONE);
            return;
        }
        super.onDraw(canvas);

        activeDotPaint.setColor(activeColor);
        activeDotPaint.setStrokeWidth(dotRadius * 2);
        inactiveDotPaint.setColor(inactiveColor);

        final float stepX = dotRadius * 2 + dotSpace;
        float pointX = leftX;

        float activeDotPointX = -1;

        for (int i = 0; i < pageCount; i++) {
            if (currentPage == i) {
                activeDotPointX = pointX + offset(i, stepX);
                pointX += activeDotWidth-dotRadius;
            } else {
                drawInactiveDot(canvas, pointX + offset(i, stepX));
            }
            pointX += stepX;
        }
        if (activeDotPointX >= 0) {
            //to avoid calc-checks and let active dot to be drawn over inactive ones,
            //we postponed drawing it as last one
            drawActiveDot(canvas, activeDotPointX);
        }

    }

    /**
     * Computes position relative offset for dot at 'page' index. Returned position offset value
     * is in pixels and is calculated by given 'stepX' and known 'offsetFactor'.
     */
    private float offset(final int page, final float stepX) {
        if (offsetFactor == 0) {
            // anything * zero == zero
            return 0;
        }
        if (page != currentPage && page != targetPage) {
            //only active and target dots are moved
            return 0;
        }
        final int delta = Math.abs(targetPage - currentPage);
        final float relativeOffset;
        if (page == currentPage) {
            relativeOffset = offsetFactor * stepX * delta;
        } else {
            relativeOffset = offsetFactor * (- stepX - dotSpace - dotRadius) * delta;
        }
        return relativeOffset;
    }

    private void drawInactiveDot(final Canvas canvas, final float pointX) {
        canvas.drawCircle(pointX, centerY, dotRadius, inactiveDotPaint);
    }

    private void drawActiveDot(final Canvas canvas, final float pointX) {
        canvas.drawCircle(pointX, centerY, dotRadius, activeDotPaint);
        canvas.drawLine(pointX, centerY, pointX+activeDotWidth-dotRadius, centerY, activeDotPaint);
        canvas.drawCircle(pointX+activeDotWidth-dotRadius, centerY, dotRadius, activeDotPaint);
    }

    public int getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(final int activeColor) {
        this.activeColor = activeColor;
    }

    public int getActiveDotWidth() {
        return activeDotWidth;
    }

    public void setActiveDotWidth(final int activeDotWidth) {
        this.activeDotWidth = activeDotWidth;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    public int getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(final int dotRadius) {
        this.dotRadius = dotRadius;
    }

    public float getDotSpace() {
        return dotSpace;
    }

    public void setDotSpace(final float dotSpace) {
        this.dotSpace = dotSpace;
    }

    public int getInactiveColor() {
        return inactiveColor;
    }

    public void setInactiveColor(final int inactiveColor) {
        this.inactiveColor = inactiveColor;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(final int pageCount) {
        this.pageCount = pageCount;
    }

    private int dp2px(final int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
