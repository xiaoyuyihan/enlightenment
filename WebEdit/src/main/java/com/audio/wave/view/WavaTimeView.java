package com.audio.wave.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioRecord;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by chenzhuo on 2017/3/30.
 * 实时绘制波形视图  有之前surfaceview 改为 view
 * 原因很直接 surfaceview 解决不了刷新卡顿问题，最好的选择只有View了
 */

public class WavaTimeView extends View {
    private ArrayList<Short> audioBuf = new ArrayList<>();//绘制波形的音频数据
    private int line_off=0;//上下边距的距离
    public int rateY = 30; //  Y轴缩小的比例 默认为1
    public int baseLine = 0;// Y轴基线
    private int marginRight = 28;//波形图绘制距离右边的距离
    private float divider = 0.4f;//为了节约绘画时间，每0.2个像素画一个数据
    private Paint circlePaint;
    private Paint mPaint;
    private Paint center;
    private Paint paintLine;

    ArrayList<Short> buf = new ArrayList<>();

    public void setBaseLine(int baseLine) {
        this.baseLine = baseLine;
    }

    public void setRateY(int rateY) {
        this.rateY = rateY;
    }

    public int getLine_off() {
        return line_off;
    }


    public void setLine_off(int line_off) {
        this.line_off = line_off;
    }


    public WavaTimeView(Context context) {
        super(context);
        init();
    }

    public WavaTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WavaTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //buf.add(new Short(12));
        circlePaint = new Paint();//画圆
        circlePaint.setColor(Color.rgb(246, 131, 126));//设置上圆的颜色
        center = new Paint();
        center.setColor(Color.rgb(39, 199, 175));// 画笔为color
        center.setStrokeWidth(1);// 设置画笔粗细
        center.setAntiAlias(true);
        center.setFilterBitmap(true);
        center.setStyle(Paint.Style.FILL);
        paintLine = new Paint();
        paintLine.setColor(Color.rgb(169, 169, 169));
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(39, 199, 175));// 画笔为color
        mPaint.setStrokeWidth(1);// 设置画笔粗细
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    /**
     * 开始绘制
     */
    public void startDrawWave() {

    }

    /**
     * 暂停绘制
     */
    public void pasueDrawWave() {

    }

    /**
     * 停止绘制
     */
    public void cleatDrawWave() {
        this.buf.clear();
        invalidate();
    }

    /**
     * 重置视图
     */
    public void resetWaveTimeView() {
        this.buf.clear();
        invalidate();
    }

    /**
     * 绘制背景
     */
    private void drawBackGroud() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null)
            return;
        // canvas.drawColor(Color.rgb(241, 241, 241));// 清除背景  
        canvas.drawARGB(255, 239, 239, 239);

        int start = (int) ((buf.size()) * divider); //音频长度
        float y;

        if (getWidth() - start <= marginRight) {//如果超过预留的右边距距离
            start = getWidth() - marginRight;//画的位置x坐标
        }

        canvas.drawCircle(start, line_off / 4, line_off / 4, circlePaint);// 上圆
        canvas.drawCircle(start, getHeight() - line_off / 4, line_off / 4, circlePaint);// 下圆
        canvas.drawLine(start, 0, start, getHeight(), circlePaint);//垂直的线
        int height = getHeight() - line_off;

        canvas.drawLine(0, line_off / 2, getWidth(), line_off / 2, paintLine);//最上面的那根线
        canvas.drawLine(0, height * 0.5f + line_off / 2, getWidth(), height * 0.5f + line_off / 2, center);//中心线
        canvas.drawLine(0, getHeight() - line_off / 2 - 1, getWidth(), getHeight() - line_off / 2 - 1, paintLine);//最下面的那根线
//	         canvas.drawLine(0, height*0.25f+20, getWidth(),height*0.25f+20, paintLine);//第二根线
//	         canvas.drawLine(0, height*0.75f+20, getWidth(),height*0.75f+20, paintLine);//第3根线
        for (int i = 0; i < buf.size(); i++) {
            y = buf.get(i) / rateY + baseLine;// 调节缩小比例，调节基准线
            float x = (i) * divider;
            if (getWidth() - (i - 1) * divider <= marginRight) {
                x = getWidth() - marginRight;
            }
            float startY = getHeight()/2-y;
            float stopY = getHeight()/2+y;
            if (startY<baseLine)
                startY = baseLine;
            if (stopY>getHeight()-baseLine)
                stopY = getHeight()-baseLine;
            //画线的方式很多，你可以根据自己要求去画。这里只是为了简单
            canvas.drawLine(x, startY, x, stopY, mPaint);//中间出波形
        }
        super.onDraw(canvas);
    }

    public void updateCanvas(ArrayList<Short> buf) {
        this.buf = buf;
        invalidate();
    }
}
