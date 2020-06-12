package jiang.photo.picker.utils;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by JFZ on 2017/6/27 10:51.
 */
@SuppressLint("NewApi")
public class PickerSelectorUtil {

    /**
     * 给view设置点击背景效果
     *
     * @param view
     * @param cornerRadius
     * @param normalColor
     * @param pressedColor
     */
    public static void toViewBackgraound(View view, int cornerRadius, int normalColor, int pressedColor) {
        view.setClickable(true);
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setCornerRadius(cornerRadius);
        normalDrawable.setColor(normalColor);

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setCornerRadius(cornerRadius);
        pressedDrawable.setColor(pressedColor);

        //给状态选择器添加状态
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
    }

    /**
     * 给view 的背景图片颜色过滤
     *
     * @param view
     * @param drawableId
     * @param filterColor
     */
    public static void toViewColorFilter(View view, int drawableId, int filterColor) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableId);
        drawable.setColorFilter(filterColor, PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 给 TextView 设置 字体颜色点击效果
     *
     * @param view         TextView
     * @param normalColor  默认颜色
     * @param pressedColor 点击颜色
     */
    public static void toTextViewSelector(TextView view, int normalColor, int pressedColor) {
        view.setClickable(true);
        int[] colors = new int[]{pressedColor, pressedColor, normalColor};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList stateList = new ColorStateList(states, colors);
        view.setTextColor(stateList);
    }

    /**
     * 给 ImageView 设置 selector 点击效果
     *
     * @param view      ImageView
     * @param normalId  默认图片资源
     * @param pressedId 点击图片资源
     */
    public static void toImageViewSlectorForResId(ImageView view, int normalId, int pressedId) {
        view.setClickable(true);
        //状态选择器
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normalDrawable = ContextCompat.getDrawable(view.getContext(), normalId);
        Drawable pressedDrawable = ContextCompat.getDrawable(view.getContext(), pressedId);
        Drawable enableDrawable = ContextCompat.getDrawable(view.getContext(), pressedId);
        //给状态选择器添加状态
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, enableDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
//        view.setBackground(stateListDrawable);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
    }

    /**
     * 给 ImageView 设置 图片颜色过滤 点击效果
     *
     * @param view              ImageView
     * @param normalDrawbaleRes 默认图片资源id
     * @param pressDrawableRes  点击图片资源id
     * @param normalColor       默认 过滤颜色
     * @param pressedColor      点击 过滤颜色
     */
    public static void toImageViewDrawableForResId(ImageView view,
                                                   @DrawableRes int normalDrawbaleRes,
                                                   @DrawableRes int pressDrawableRes,
                                                   int normalColor,
                                                   int pressedColor) {
        view.setClickable(true);
        //状态选择器
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normalDrawable = ContextCompat.getDrawable(view.getContext(), normalDrawbaleRes);
        normalDrawable.setColorFilter(normalColor, PorterDuff.Mode.SRC_IN);
        Drawable pressedDrawable = ContextCompat.getDrawable(view.getContext(), pressDrawableRes);
        pressedDrawable.setColorFilter(pressedColor, PorterDuff.Mode.SRC_IN);
        //给状态选择器添加状态
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
    }
}
