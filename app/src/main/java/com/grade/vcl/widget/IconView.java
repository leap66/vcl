package com.grade.vcl.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class IconView extends AppCompatTextView {

  public IconView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setTypeface(Typeface.createFromAsset(getContext().getAssets(), "iconfont.ttf"));
  }
}