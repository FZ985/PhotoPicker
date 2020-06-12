package jiang.photo.picker.utils.statusbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Created by JFZ
 * on 2018/5/19.
 */

public class PhotoStatusView extends AppCompatTextView {
    public PhotoStatusView(Context context) {
        super(context);
    }

    public PhotoStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, getStatusHeight(getContext()));
    }

    @SuppressWarnings("rawtypes")
    private int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
