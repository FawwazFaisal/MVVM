package com.omnisoft.retrofitpractice.Utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.R;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/5/2021.
 */
public class PrefixedEditText extends AppCompatEditText {
    int mOriginalLeftPadding = -1;

    public PrefixedEditText(Context context) {
        super(context);
    }

    public PrefixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefixedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setPrefix();
    }

    private void setPrefix() {
        if (mOriginalLeftPadding == -1) {
            String prefix = (String) getTag();
            float[] widths = new float[prefix.length()];
            getPaint().getTextWidths(prefix, widths);
            float textWidth = 0;
            for (float w : widths) {
                textWidth += w;
            }
            mOriginalLeftPadding = getCompoundPaddingLeft();
            setPadding((int) (textWidth + mOriginalLeftPadding)
                    , getPaddingTop(), getPaddingRight(),
                    getPaddingBottom());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String prefix = (String) getTag();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(App.context, R.color.grayDark));
        paint.setTextSize(getTextSize());
        canvas.drawText(prefix, mOriginalLeftPadding,
                getLineBounds(0, null), paint);
    }
}
