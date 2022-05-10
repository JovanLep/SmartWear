package com.lespayne.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.lespayne.base.R;

public class TitleView extends RelativeLayout {
    private TextView back;
    private TextView title;
    private RelativeLayout root;
    private Context context;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_title_view, this);
        init(view);
    }


    public void init(View view) {
        back = view.findViewById(R.id.topbar_txt_back);
        title = view.findViewById(R.id.topbar_txt_title);
        root = view.findViewById(R.id.root);
    }

    public void setTitle(String txt) {
        title.setText(txt);
    }

    public void setTitleColor() {
        title.setTextColor(ContextCompat.getColor(context, android.R.color.white));
    }

    public void setTitle(String txt, Activity activity) {
        title.setText(txt);
        if (activity != null) {
            back.setOnClickListener(view -> activity.finish());
        }
    }

    public void setBackGone() {
        back.setVisibility(GONE);
    }

    public void finish(Activity activity) {
        back.setOnClickListener(view -> activity.finish());
    }

    public void setBackgroundColour(@ColorInt int res) {
        root.setBackgroundColor(res);
    }
}