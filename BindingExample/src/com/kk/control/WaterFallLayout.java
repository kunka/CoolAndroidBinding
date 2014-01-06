/*
 * Copyright (C) 2014 kk-team.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kk.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.kk.example.BindingExample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hk on 13-12-3.
 */
public class WaterFallLayout extends ViewGroup {
    public static enum Orientation {
        VERTICAL, HORIZONTAL;
    }

    public static class OrientedSize {
        private Orientation _orientation;
        private float _direct;
        private float _indirect;

        public Orientation Orientation() {
            return _orientation;
        }

        public void setOrientation(Orientation _orientation) {
            this._orientation = _orientation;
        }

        public float Direct() {
            return _direct;
        }

        public void setDirect(float _direct) {
            this._direct = _direct;
        }

        public float Indirect() {
            return _indirect;
        }

        public void setIndirect(float _indirect) {
            this._indirect = _indirect;
        }

        public float Width() {
            return (_orientation == Orientation.HORIZONTAL) ? Direct() : Indirect();
        }

        public void setWidth(float value) {
            if (_orientation == Orientation.HORIZONTAL) {
                setDirect(value);
            } else {
                setIndirect(value);
            }
        }

        public float Height() {
            return (_orientation != Orientation.HORIZONTAL) ? Direct() : Indirect();
        }

        public void setHeight(float value) {
            if (_orientation != Orientation.HORIZONTAL) {
                setDirect(value);
            } else {
                setIndirect(value);
            }
        }

        public OrientedSize(Orientation orientation) {
            this(orientation, 0.0f, 0.0f);
        }

        public OrientedSize(Orientation orientation, float width, float height) {
            _orientation = orientation;
            _direct = 0.0f;
            _indirect = 0.0f;
            setWidth(width);
            setHeight(height);
        }
    }

    private Orientation orientation = Orientation.VERTICAL;
    private float horizontalPadding;
    private float verticalPadding;
    private float fixedWidthOrHeight;
    private int columnOrRowNum = 1;
    private boolean divideSpace;
    private List<OrientedSize> columnOrRowLength;

    public WaterFallLayout(Context context) {
        super(context);
        init(null);
    }

    public WaterFallLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WaterFallLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Context context = getContext();
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterfallLayout);
            if (a != null) {
                orientation = a.getInt(R.styleable.WaterfallLayout_orientation, 0) == 0 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
                columnOrRowNum = a.getInt(R.styleable.WaterfallLayout_columnOrRowNum, 1);
                fixedWidthOrHeight = a.getDimension(R.styleable.WaterfallLayout_fixedWidthOrHeight, 0);
                verticalPadding = a.getDimension(R.styleable.WaterfallLayout_verticalPadding, 0);
                horizontalPadding = a.getDimension(R.styleable.WaterfallLayout_horizontalPadding, 0);
                divideSpace = a.getBoolean(R.styleable.WaterfallLayout_divideSpace, false);
            }
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public float getHorizontalPadding() {
        return horizontalPadding;
    }

    public float getVerticalPadding() {
        return verticalPadding;
    }

    public float getFixedWidthOrHeight() {
        return fixedWidthOrHeight;
    }

    public int getColumnOrRowNum() {
        return columnOrRowNum;
    }

    public boolean isDivideSpace() {
        return divideSpace;
    }

    public void setOrientation(Orientation orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            requestLayout();
            invalidate();
        }
    }

    public void setOrientation(String orientation) {
        Orientation o = null;
        if ("horizontal".equals(orientation)) {
            o = Orientation.HORIZONTAL;
        } else if ("vertical".equals(orientation)) {
            o = Orientation.VERTICAL;
        }
        if (o != null) {
            setOrientation(o);
        }
    }

    public void setColumnOrRowNum(int columnOrRowNum) {
        if (this.columnOrRowNum != columnOrRowNum) {
            this.columnOrRowNum = columnOrRowNum;
            requestLayout();
            invalidate();
        }
    }

    public void setFixedWidthOrHeight(float fixedWidthOrHeight) {
        if (this.fixedWidthOrHeight != fixedWidthOrHeight) {
            this.fixedWidthOrHeight = fixedWidthOrHeight;
            requestLayout();
            invalidate();
        }
    }

    public void setVerticalPadding(float verticalPadding) {
        if (this.columnOrRowNum != columnOrRowNum) {
            this.columnOrRowNum = columnOrRowNum;
            requestLayout();
            invalidate();
        }
        this.verticalPadding = verticalPadding;
    }

    public void setHorizontalPadding(float horizontalPadding) {
        if (this.horizontalPadding != horizontalPadding) {
            this.horizontalPadding = horizontalPadding;
            requestLayout();
            invalidate();
        }
    }

    public void setDivideSpace(boolean divideSpace) {
        if (this.divideSpace != divideSpace) {
            this.divideSpace = divideSpace;
            requestLayout();
            invalidate();
        }
    }

    protected void resetColumnOrRowLength(Orientation orientation) {
        columnOrRowLength = new ArrayList<OrientedSize>(columnOrRowNum);
        for (int i = 0; i < columnOrRowNum; i++) {
            columnOrRowLength.add(new OrientedSize(orientation));
        }
    }

    private int getShortestColumnOrRowIndex() {
        int index = 0;
        float minLenght = Float.MAX_VALUE;
        int size = columnOrRowLength.size();
        for (int i = 0; i < size; i++) {
            if (columnOrRowLength.get(i).Direct() < minLenght) {
                minLenght = columnOrRowLength.get(i).Direct();
                index = i;
            }
        }
        return index;
    }

    private int getLongestColumnOrRowIndex() {
        int index = 0;
        float maxLenght = -1;
        int size = columnOrRowLength.size();
        for (int i = 0; i < size; i++) {
            if (columnOrRowLength.get(i).Direct() > maxLenght) {
                maxLenght = columnOrRowLength.get(i).Direct();
                index = i;
            }
        }
        return index;
    }

    private boolean isTheLastGroupChild(View element) {
        int childCount = getChildCount();
        if (childCount > columnOrRowNum) {
            int last = childCount % columnOrRowNum;
            int groupCount = childCount / columnOrRowNum;
            int i = columnOrRowNum * (last == 0 ? (groupCount - 1) : groupCount);
            for (; i < childCount; i++) {
                if (getChildAt(i) == element) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Orientation o = orientation;
        boolean isHorizontal = o == Orientation.HORIZONTAL;
        resetColumnOrRowLength(o);

        OrientedSize itemSize = new OrientedSize(o);
        OrientedSize maxSize = new OrientedSize(o);

        int widthSize;
        int heightSize;

        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int w = 0;
        int h = 0;
        w += pleft + pright;
        h += ptop + pbottom;
        widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        if (divideSpace) {
            fixedWidthOrHeight = isHorizontal
                    ? (heightSize - ptop - pbottom - verticalPadding * (columnOrRowNum - 1)) / (float) columnOrRowNum
                    : (widthSize - pleft - pright - horizontalPadding * (columnOrRowNum - 1)) / (float) columnOrRowNum;
        }

        itemSize.setDirect(isHorizontal ? widthSize + pleft + pright : heightSize + ptop + pbottom);
        itemSize.setIndirect(fixedWidthOrHeight);

        maxSize.setIndirect((isHorizontal ? pleft + pright : ptop + pbottom) + fixedWidthOrHeight * columnOrRowNum +
                (isHorizontal ? verticalPadding : horizontalPadding) * (columnOrRowNum - 1));

        double maxDirect = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View element = getChildAt(i);
            LayoutParams p = element.getLayoutParams();
            if (p == null) {
                p = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
            }

            // Determine the size of the element MeasureSpec.makeMeasureSpec(width, widthMode)
            if (orientation == Orientation.VERTICAL) {
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec,
                        ptop + pbottom, p.height);
                element.measure(MeasureSpec.makeMeasureSpec((int) itemSize.Width(), MeasureSpec.EXACTLY), childHeightSpec);
            } else {
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec,
                        pleft + pright, p.width);
                element.measure(childWidthSpec, MeasureSpec.makeMeasureSpec((int) itemSize.Height(), MeasureSpec.EXACTLY));
            }

            //insert element to the shortest list
            //TODO: This is not the most effective method
            int minIndex = getShortestColumnOrRowIndex();
            OrientedSize size = columnOrRowLength.get(minIndex);
            //Current element is the last group child,so not to add padding
            //TODO: This is not the most effective method
            if (isTheLastGroupChild(element)) {
                size.setDirect(size.Direct() + (isHorizontal ? element.getMeasuredWidth() : element.getMeasuredHeight()));
            } else {
                size.setDirect(size.Direct() + (isHorizontal ? horizontalPadding : verticalPadding)
                        + (isHorizontal ? element.getMeasuredWidth() : element.getMeasuredHeight()));
            }

            int maxIndex = getLongestColumnOrRowIndex();
            maxDirect = columnOrRowLength.get(maxIndex).Direct();

            //Increasing Direct Size
            maxSize.setDirect((float) Math.max((maxSize.Direct()), maxDirect));
        }

        setMeasuredDimension((int) maxSize.Width(), (int) maxSize.Height());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Orientation o = orientation;
        boolean isHorizontal = o == Orientation.HORIZONTAL;
        resetColumnOrRowLength(o);

        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int count = getChildCount();
        float x = 0;
        float y = 0;
        for (int i = 0; i < count; i++) {
            View element = getChildAt(i);

            //insert element to the shortest list
            int minIndex = getShortestColumnOrRowIndex();
            OrientedSize size = columnOrRowLength.get(minIndex);
            y = ptop + size.Direct();
            size.setDirect(size.Direct() + (isHorizontal ? horizontalPadding : verticalPadding)
                    + (isHorizontal ? element.getMeasuredWidth() : element.getMeasuredHeight()));
            x = pleft + minIndex * fixedWidthOrHeight + minIndex * (isHorizontal ? verticalPadding : horizontalPadding);

            RectF bounds = isHorizontal ?
                    new RectF(y, x, element.getMeasuredWidth() + y, fixedWidthOrHeight + x) :
                    new RectF(x, y, fixedWidthOrHeight + x, element.getMeasuredHeight() + y);
            element.layout((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);
        }
    }
}
