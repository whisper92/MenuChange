package mc.wxp.com.menuchange;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 加变减
 * Created by whisper on 14-11-29.
 */
public class AddDes extends View implements View.OnLongClickListener, View.OnClickListener {

    private int mWidth = 0;
    private int mHeight = 0;

    private Paint mPaintH;
    private Paint mPaintV;

    private double mSqrt;

    private CusLine mHor;
    private CusLine mVer;

    private Context mContext;
    private Timer mTimer1;
    private TimerTask mTimerTask1;

    private Timer mTimer2;
    private TimerTask mTimerTask2;

    private int mDegree1 = 0;
    private int mDegree2 = 0;

    /*
     *动画分两步进行，true为第一步，false为第二步
     */
    boolean flag = true;
    //boolean mConvertToAdd = false;

    /*
     *当前的状态 true:加号；false：减号
     */
    boolean mCurrentState = true;

    public AddDes(Context context) {
        this(context, null);
    }

    public AddDes(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddDes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        mPaintH = new Paint();
        mPaintH.setAntiAlias(true);
        mPaintH.setColor(Color.WHITE);
        mPaintH.setDither(true);
        mPaintH.setStyle(Paint.Style.STROKE);
        mPaintH.setStrokeWidth(10f);

        mPaintV = new Paint();
        mPaintV.setAntiAlias(true);
        mPaintV.setColor(Color.RED);
        mPaintV.setDither(true);
        mPaintV.setStyle(Paint.Style.STROKE);
        mPaintV.setStrokeWidth(10f);

        mSqrt = Math.sqrt((double) 2);
        mHor = new CusLine();
        mVer = new CusLine();

        setOnLongClickListener(this);
        setOnClickListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Drawable bgDrawable = getBackground();
        int bgW = bgDrawable.getIntrinsicWidth();
        int bgH = bgDrawable.getIntrinsicHeight();

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = bgW;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = bgH;
        }


        float EDG = (float) mWidth;

        mHor.startX = (float) (EDG / 2 - EDG / 2 / mSqrt);
        mHor.startY = EDG / 2;
        mHor.endX = (float) (EDG / 2 + EDG / 2 / mSqrt);
        mHor.endY = EDG / 2;

        mVer.startX = EDG / 2;
        mVer.startY = (float) (EDG / 2 - EDG / 2 / mSqrt);
        mVer.endX = EDG / 2;
        mVer.endY = (float) (EDG / 2 + EDG / 2 / mSqrt);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (mCurrentState == true) {
        if (flag == true) {
            canvas.save();
            canvas.rotate(mDegree1, mWidth / 2, mHeight / 2);
            canvas.drawLine(mHor.startX, mHor.startY, mHor.endX, mHor.endY, mPaintH);
            canvas.restore();
            canvas.drawLine(mVer.startX, mVer.startY, mVer.endX, mVer.endY, mPaintH);
        } else {
            canvas.save();
            canvas.rotate(-mDegree2, mWidth / 2, mHeight / 2);
            canvas.drawLine(mVer.startX, mVer.startY, mVer.endX, mVer.endY, mPaintH);
            canvas.drawLine(mVer.startX, mVer.startY, mVer.endX, mVer.endY, mPaintH);
            canvas.restore();
        }
        } else {
            Log.e("wxp","qunide");
            if (flag == true) {
                canvas.save();
                canvas.rotate(mDegree1, mWidth / 2, mHeight / 2);
                canvas.drawLine(mHor.startX, mHor.startY, mHor.endX, mHor.endY, mPaintH);
                canvas.restore();
                canvas.drawLine(mHor.startX, mHor.startY, mHor.endX, mHor.endY, mPaintH);
            } else {
                canvas.save();
                canvas.rotate(-mDegree2, mWidth / 2, mHeight / 2);
                canvas.drawLine(mHor.startX, mHor.startY, mHor.endX, mHor.endY, mPaintH);
                canvas.drawLine(mVer.startX, mVer.startY, mVer.endX, mVer.endY, mPaintH);
                canvas.restore();
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /*switch (event.getAction()){
            case MotionEvent.A
        }*/
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onLongClick(View v) {
        //Toast.makeText(mContext, "LONG", Toast.LENGTH_SHORT).show();
        startStep1();
        return true;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();

    }

    public void startStep1() {
        mDegree1 = 0;
        mDegree2 = 0;
        flag = true;
        mTimer1 = new Timer();
        mTimerTask1 = new TimerTask() {
            @Override
            public void run() {
                convertVer();
            }
        };

        mTimer1.schedule(mTimerTask1, 10, 10);
    }

    public void startStep2() {
        mTimer2 = new Timer();
        mTimerTask2 = new TimerTask() {
            @Override
            public void run() {
                convertHor();
            }
        };

        mTimer2.schedule(mTimerTask2, 10, 10);
    }

    public void convertVer() {
        mDegree1 += 3;
        mHandler.sendEmptyMessage(0);
        if (mDegree1 >= 90) {
            mTimer1.cancel();
            mHandler.sendEmptyMessage(1);
        }
    }

    public void convertHor() {
        mDegree2 += 3;
        mHandler.sendEmptyMessage(0);
        if (mDegree2 >= 90) {
            mTimer2.cancel();
            mHandler.sendEmptyMessage(2);
        }

    }

    private class CusLine {
        float startX;
        float startY;
        float endX;
        float endY;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.e("wxp","000");
                invalidate();
            }
            if (msg.what == 1) {
                Log.e("wxp","111");
                flag = false;
                mDegree1 = 0;
                startStep2();
            }
            if (msg.what == 2) {
                Log.e("wxp","222");
                //mDegree2 = 0;
                flag = true;
                if (mCurrentState == true)
                    mCurrentState = false;
                else mCurrentState = true;
            }
        }
    };

}