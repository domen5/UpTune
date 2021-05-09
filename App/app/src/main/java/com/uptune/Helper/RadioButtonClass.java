package com.uptune.Helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

public class RadioButtonClass extends AppCompatRadioButton {
    private OnCheckedChangeListener onCheckedChangeListener;

    public RadioButtonClass(Context context) {
        super(context);
    }

    public RadioButtonClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioButtonClass(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOwnOnCheckListener();
        setButtonDrawable(null);
    }

    public void setOwnChachedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    private void setOwnOnCheckListener() {
        setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onCheckedChangeListener != null)
                onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        });
    }
}
