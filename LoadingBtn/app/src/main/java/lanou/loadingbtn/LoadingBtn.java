package lanou.loadingbtn;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ZhangRui on 16/11/23.
 */

public class LoadingBtn extends Button {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_CHANGE = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_ERROR = 3;

    private int mState;// 用来记录状态

    private int mWidth, mHeight;// 组件的宽高
    private int mChangeWidth;// 当改变的时候, 圆角矩形的宽度
    private int mErrorCircleR;// 错误的半径
    private Paint mBgPaint;// 画背景的画笔
    private Paint mLinePaint;// 划线的的画笔
    private ObjectAnimator mChangeAnim;
    private ObjectAnimator mRotateAnim;

    public int getErrorCircleR() {
        return mErrorCircleR;
    }

    public void setErrorCircleR(int errorCircleR) {
        mErrorCircleR = errorCircleR;
    }

    public LoadingBtn(Context context) {
        this(context, null);
    }

    public LoadingBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 初始化方法
    private void init() {
//        mState = STATE_ERROR;
        mState = STATE_NORMAL;
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);// 抗锯齿
        mBgPaint.setColor(0xFF33FDDB);// 设置背景颜色
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xFF000000);// 黑色
        mLinePaint.setStrokeWidth(5);// 线宽
        // 只画线
        mLinePaint.setStyle(Paint.Style.STROKE);

        // 三种style  描边 填充 ....
//        setPadding(10,0,10,0);//设置边距
//        setBackgroundColor(0x00000000);// 背景 颜色

    }

    // getModel *wrap_content  match_parent
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mHeight = getHeight();
        mWidth = getWidth();
        switch (mState) {
            case STATE_NORMAL:
                RectF rectF = new RectF(0, 0, mWidth, mHeight);// 矩形
                canvas.drawRoundRect(rectF, mHeight / 2, mHeight / 2, mBgPaint);
                super.onDraw(canvas);// 画字
                break;
            case STATE_CHANGE:
                RectF changeRectF = new RectF((mWidth - mChangeWidth) / 2, 0, mChangeWidth + (mWidth - mChangeWidth) / 2, mHeight);
                canvas.drawRoundRect(changeRectF, mHeight / 2, mHeight / 2, mBgPaint);
                break;
            case STATE_LOADING:
                // 画一个背景的圆
                canvas.drawCircle(mWidth / 2, mHeight / 2, mHeight / 2, mBgPaint);
                // 画Loading的线
                RectF arcRectF = new RectF((mWidth - mChangeWidth)
                        / 2 + 25, 25, mChangeWidth + (mWidth - mChangeWidth) / 2 - 25, mHeight - 25);
                canvas.drawArc(arcRectF, 0, 90, false, mLinePaint);
                break;
            case STATE_ERROR:
                canvas.drawCircle(mWidth / 2, mHeight / 2, mHeight / 2, mBgPaint);
                canvas.rotate(45, mWidth / 2, mHeight / 2);// 让画布旋转45°
                canvas.drawLine(mWidth / 2 - mHeight / 2 + 10, mHeight / 2, mWidth / 2 + mHeight / 2 - 10, mHeight / 2, mLinePaint);
                canvas.rotate(90, mWidth / 2, mHeight / 2); // 接着旋转90°
                canvas.drawLine(mWidth / 2 - mHeight / 2 + 10, mHeight / 2, mWidth / 2 + mHeight / 2 - 10, mHeight / 2, mLinePaint);

                // 花圈
                canvas.drawCircle(mWidth/2,mHeight / 2, mErrorCircleR,mBgPaint);
                break;
        }
    }

    public int getChangeWidth() {
        return mChangeWidth;
    }

    public void setChangeWidth(int changeWidth) {
        mChangeWidth = changeWidth;
    }

    /**
     * 暴露给用户的方法
     */
    // 让组件 变成loading状态
    public void startLoading() {
        startChange2Loading();


    }

    // 让组件变成error状态
    public void startError() {
        mRotateAnim.cancel();// 让它不转
        setEnabled(false);
        setRotation(0); // 把他转回0位置
        mState = STATE_ERROR;
        ObjectAnimator errorAnim = ObjectAnimator
                .ofInt(this,"errorCircleR",mHeight / 2,-10);
        errorAnim.setDuration(1000);
        errorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        errorAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 还原回初始状态
                change2Normal();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        errorAnim.start();
    }

    // 还原回初始状态
    private void change2Normal() {
        setEnabled(false);// 不能点击
        mState = STATE_CHANGE;
        // 定义属性动画
        mChangeAnim = ObjectAnimator.ofInt
                (this, "changeWidth", mHeight, mWidth);
        mChangeAnim.setDuration(2000);
        mChangeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();// 当值改变的时候, 重新绘制
            }
        });
        mChangeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 动画完成
                mState = STATE_NORMAL;
                invalidate();
                setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mChangeAnim.start();
    }

    // 将状态变为Change
    private void startChange2Loading() {
        mState = STATE_CHANGE;
        // 开始Loading动画

        setEnabled(false); // 不能点击
        // 定义属性动画
        mChangeAnim = ObjectAnimator.ofInt
                (this, "changeWidth", mWidth, mHeight);
        mChangeAnim.setDuration(2000);
        mChangeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();// 当值改变的时候, 重新绘制
            }
        });
        mChangeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 动画完成
                startLoadingAnim();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mChangeAnim.start();
    }

    // 开始播放 loading动画
    private void startLoadingAnim() {
        setEnabled(true);
        mState = STATE_LOADING;
        invalidate();
        mRotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        // 让动画无限循环
        mRotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnim.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnim.setDuration(600);
        mRotateAnim.start();
    }
}
