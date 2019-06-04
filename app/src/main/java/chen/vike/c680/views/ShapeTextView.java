package chen.vike.c680.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lht on 2017/11/20.
 */

public class ShapeTextView extends TextView {

    private Paint paint;
    private Context context;
    public ShapeTextView(Context context) {
        super(context);
    }
    public ShapeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void iniView(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        iniView();
        RectF rectF = new RectF();
        int radius =  getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
        rectF.set(getPaddingLeft(),getPaddingTop(),radius - getPaddingRight(),radius - getPaddingBottom());
        //绘制圆弧
        canvas.drawArc(rectF,0,360,false,paint);
        super.onDraw(canvas);
    }
}
