package me.zhanghai.android.materialprogressbar.internal;

import android.graphics.PorterDuff.Mode;
import me.zhanghai.android.materialprogressbar.R;

public class DrawableCompat {
    public static Mode parseTintMode(int value, Mode defaultMode) {
        switch (value) {
            case R.styleable.View_paddingEnd /*3*/:
                return Mode.SRC_OVER;
            case R.styleable.Toolbar_contentInsetStart /*5*/:
                return Mode.SRC_IN;
            case R.styleable.Toolbar_popupTheme /*9*/:
                return Mode.SRC_ATOP;
            case R.styleable.Toolbar_titleMarginEnd /*14*/:
                return Mode.MULTIPLY;
            case R.styleable.Toolbar_titleMarginTop /*15*/:
                return Mode.SCREEN;
            case R.styleable.Toolbar_titleMarginBottom /*16*/:
                return Mode.ADD;
            default:
                return defaultMode;
        }
    }
}
