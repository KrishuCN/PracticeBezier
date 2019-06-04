package chen.vike.c680.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lht.vike.a680_v1.R;

/**
 * Created by lht on 2017/11/27.
 */

public class ShapeView extends View {
    private Paint paint;
    public ShapeView(Context context) {
        super(context);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        RectF rectF = new RectF();
        int radius =  getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
        rectF.set(getPaddingLeft(),getPaddingTop(),radius - getPaddingRight(),radius - getPaddingBottom());
        //绘制圆弧
        canvas.drawArc(rectF,0,360,false,paint);
    }
    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.person_bg));
    }
}
