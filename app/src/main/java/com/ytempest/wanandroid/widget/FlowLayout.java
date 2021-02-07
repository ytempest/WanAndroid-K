package com.ytempest.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.wanandroid.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ytempest
 * 一个实现流式布局的ViewGroup
 */
public class FlowLayout extends ViewGroup {

    private static final String TAG = FlowLayout.class.getSimpleName();

    // 每行View列表
    private LinkedList<List<View>> mRowViews = new LinkedList<>();
    // 每行View中的最大高度
    private List<Integer> mRowHeights = new ArrayList<>();
    // 每行View的总宽度
    private List<Integer> mRowWidths = new ArrayList<>();
    // 每行View的位置
    private int mGravity = Gravity.LEFT;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mGravity = array.getInt(R.styleable.FlowLayout_row_gravity, mGravity);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mRowViews.clear();

        // 计算父布局能提供的宽度大小
        int childSpace = widthSize - getPaddingLeft() - getPaddingRight();
        // 当前行的宽度
        int rowW = 0;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }

            // 先调用measureChild()方法测量子View的宽高以获取子View的宽高
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            checkChildWidth(child, widthMeasureSpec, heightMeasureSpec);

            // 计算一个子View占据的真实宽高
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childRealW = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            // 如果当前行无法能容纳该View，则开启新的一行并重置数据
            if (rowW + childRealW > childSpace || mRowViews.isEmpty()) {
                mRowViews.add(new LinkedList<>());
                // 重置数据
                rowW = 0;
            }
            mRowViews.getLast().add(child);
            rowW += childRealW;
        }

        initViewSizes();
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = caleRowMaxWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = caleTotalHeight();
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private void initViewSizes() {
        mRowWidths.clear();
        mRowHeights.clear();
        for (int i = 0; i < mRowViews.size(); i++) {
            int viewsW = 0; // 行宽度
            int rowMaxHeight = 0; // 行最大高度
            for (View view : mRowViews.get(i)) {
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                rowMaxHeight = Math.max(rowMaxHeight, view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                viewsW += lp.leftMargin + view.getMeasuredWidth() + lp.rightMargin;
            }
            mRowWidths.add(viewsW);
            mRowHeights.add(rowMaxHeight);
        }
    }

    private int caleTotalHeight() {
        int totalH = 0;
        for (Integer height : mRowHeights) {
            totalH += height;
        }
        return totalH + getPaddingTop() + getPaddingBottom();
    }

    private int caleRowMaxWidth() {
        int maxViewW = 0; // 行最大宽度
        for (Integer rowWidth : mRowWidths) {
            maxViewW = Math.max(maxViewW, rowWidth);

        }
        return maxViewW + getPaddingLeft() + getPaddingRight();
    }

    /**
     * 检测View的宽度加上margin值后是否超过父布局的的宽度减去padding值，如果超过就重新测量该View
     */
    private void checkChildWidth(View child, int widthMeasureSpec, int heightMeasureSpec) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int horizontalMargin = lp.leftMargin + lp.rightMargin;
        int childW = child.getMeasuredWidth() + horizontalMargin;
        int containerW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        // 判断该View是否超过了父布局能给予的宽度
        if (childW > containerW) {
            int mode = MeasureSpec.getMode(widthMeasureSpec);
            int width = containerW - horizontalMargin;
            // 计算父布局能给子View的最大宽度
            measureChild(child, MeasureSpec.makeMeasureSpec(width, mode), heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int curTop = getPaddingTop();
        for (int i = 0; i < mRowViews.size(); i++) {
            final int rowW = mRowWidths.get(i);
            final int rowH = mRowHeights.get(i);

            final int majorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
            final int minorGravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;

            int childLeft;
            switch (minorGravity) {
                case Gravity.RIGHT:
                    childLeft = right - rowW - getPaddingRight();
                    break;

                case Gravity.CENTER_HORIZONTAL:
                    childLeft = getPaddingLeft() + (right - left - getPaddingLeft() - getPaddingRight() - rowW) / 2;
                    break;

                case Gravity.LEFT:
                default:
                    childLeft = getPaddingLeft();
                    break;
            }

            for (View view : mRowViews.get(i)) {
                final MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                int childTop;
                switch (majorGravity) {
                    case Gravity.BOTTOM:
                        childTop = curTop + rowH - lp.bottomMargin - childHeight - lp.topMargin;
                        break;

                    case Gravity.CENTER_VERTICAL:
                        childTop = curTop + (rowH - lp.topMargin - childHeight - lp.bottomMargin) / 2;
                        break;

                    case Gravity.TOP:
                    default:
                        childTop = curTop;
                        break;
                }

                childLeft += lp.leftMargin;
                childTop += lp.topMargin;

                setChildFrame(view, childLeft, childTop, childWidth, childHeight);
                childLeft += childWidth + lp.rightMargin;
            }
            curTop += rowH;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    @Nullable
    public List<View> getViewsAtRow(int row) {
        int size = mRowViews.size();
        return 0 <= row && row < size ? mRowViews.get(row) : null;
    }

    public int getRowCount() {
        return mRowViews.size();
    }

    public int getRowMaxHeight(int row) {
        return 0 <= row && row < mRowHeights.size() ?
                mRowHeights.get(row) : -1;
    }

    public int getRowViewWidth(int row) {
        return 0 <= row && row < mRowWidths.size() ?
                mRowWidths.get(row) : -1;
    }

    /**
     * 重写方法，为View创建自定义Params
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public void reset() {
        removeAllViews();
        mRowViews.clear();
        mRowWidths.clear();
        mRowHeights.clear();
    }

    /**
     * 如果添加的子View布局是统一的，建议通过该方法添加子View以利用回收的子View
     * <p>
     * 主要使用场景为RecyclerView中，建议通过方法添加View，可以回收利用当前布局中旧的子View，若不使
     * 用该方法，需要在添加新的子View前，先调用{@link FlowLayout#reset()}重置View，以防止子View异常
     *
     * @param list   需要为子View绑定的数据
     * @param binder 绑定器
     * @param <Item> 数据类型
     */
    public <Item> void recycleAndAddView(List<Item> list, ViewBinder<Item> binder) {
        if (list == null || list.isEmpty()) {
            reset();
            return;
        }
        int childCount = getChildCount();
        int size = list.size();
        // 布局已存在的子View数量，根据数据长度，多则移除少则创建
        int diffCount = childCount - size;
        if (childCount > size) {
            removeViews(size, diffCount);

        } else {
            for (int i = 0; i < diffCount; i++) {
                View view = binder.onCreateView(this);
                addView(view);
            }
        }

        for (int i = 0; i < getChildCount(); i++) {
            binder.bindView(this, getChildAt(i), list.get(i), i);
        }
    }

    public interface ViewBinder<T> {
        View onCreateView(FlowLayout layout);

        void bindView(FlowLayout layout, View view, T data, int index);
    }
}
