package com.lespayne.base.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.lespayne.base.R;

import java.util.Objects;

public class AutoClearEditText extends AppCompatEditText implements
        OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean hasFoucs;
    private OnClearTxtInterceptor onClearTxtInterceptor;
    private OnClearVisibleChange onClearVisibleChange;

    public AutoClearEditText(Context context) {
        this(context, null);
    }

    public AutoClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public AutoClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_delete);
        }
        mClearDrawable.setBounds(0, 0, Objects.requireNonNull(mClearDrawable).getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    if (onClearTxtInterceptor != null) {
                        onClearTxtInterceptor.onClear();
                        return false;
                    } else {
                        this.setText("");
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setSelection(Objects.requireNonNull(getText()).length());
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    public void setClearIconVisible(boolean visible) {
        if (onClearVisibleChange != null) {
            onClearVisibleChange.onChange(visible);
        }
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public void setOnClearTxtInterceptor(OnClearTxtInterceptor onClearTxtInterceptor) {
        this.onClearTxtInterceptor = onClearTxtInterceptor;
    }

    public void setOnClearVisibleChange(OnClearVisibleChange onClearVisibleChange) {
        this.onClearVisibleChange = onClearVisibleChange;
    }

    public interface OnClearTxtInterceptor {
        void onClear();
    }

    public interface OnClearVisibleChange {
        void onChange(boolean isClearVisible);
    }

    public void setMaxLength(int length) {
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }
}