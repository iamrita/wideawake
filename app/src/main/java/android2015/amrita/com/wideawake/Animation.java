package android2015.amrita.com.wideawake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class Animation extends View {
    int offset = 10;
    int radius = 8;
    int step = 10;
    int nsteps = 70;
    int mySteps = 0;
    int nTimes = 0;


    public Animation(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
    public Animation(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public Animation(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint gray = new Paint();
        gray.setAntiAlias(true);
        gray.setStrokeWidth(2);
        gray.setColor(Color.BLACK);
        gray.setStyle(Paint.Style.STROKE);
        gray.setTextSize(25);


        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int x = width/2;
        int y = 0 + offset;

        for(int i=0; i<nsteps; i++) {
            canvas.drawCircle(x, y + offset, radius, gray);
            offset += 20;

        }
        Paint red = new Paint();
        red.setAntiAlias(true);
        red.setStrokeWidth(5);
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.STROKE);
        red.setTextSize(25);
        offset = 10;

        int liney = 20 * 40;
        canvas.drawLine(0, liney, x-40, liney, red);
        canvas.drawLine(x+40, liney, width, liney, red);


        Paint blue = new Paint();
        blue.setAntiAlias(true);
        blue.setStrokeWidth(5);
        blue.setColor(Color.BLUE);
        blue.setStyle(Paint.Style.STROKE);
        blue.setTextSize(25);
        offset = 10;
        canvas.drawCircle(x, y+step, 8, blue );


    }
    public void update() {
        step += 20;
        if(mySteps >= nsteps) {
            //reset
            mySteps = 0;
            step = 10;
        } else {
            mySteps++;
        }
    }
    public int getScore() {
        return Math.abs(mySteps - 40 +1);
    }

    public void reset(){
        offset = 10;
        step = 10;
        mySteps = 0;

    }


}



