package com.app.codeweb.codeweb.Others;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.app.codeweb.codeweb.R;

/**
 * Created by acer on 9/27/2017.
 */

public class CodeWebEditor extends EditText {
    private Rect rec;
    private Paint paint;

    public CodeWebEditor(Context context, AttributeSet attrs) {
        super(context, attrs);

        rec = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(124,201,232));
        paint.setTextSize(18);
    }

    @Override
    protected void onDraw(Canvas canvas){
       int baseline = getBaseline();
        for(int i = 0; i < getLineCount(); i++ ){
            if(i-1<9){
                canvas.drawText("   "+(i+1)+"  |", rec.left, baseline, paint);
                baseline += getLineHeight();
            }else{
                canvas.drawText(""+(i+1)+"  |", rec.left, baseline, paint);
                baseline += getLineHeight();
            }
        }
        super.onDraw(canvas);
   }

}
