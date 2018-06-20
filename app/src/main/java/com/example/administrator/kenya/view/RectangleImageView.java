package com.example.administrator.kenya.view;

import android.content.Context;
import android.util.AttributeSet;

/** An image view which always remains square with respect to its width. */
public class RectangleImageView extends android.support.v7.widget.AppCompatImageView {
  public RectangleImageView(Context context) {
    super(context);
  }

  public RectangleImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * 1.5));
  }
}
