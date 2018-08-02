package enlightenment.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import enlightenment.com.base.R;
import enlightenment.com.tool.device.DisplayUtils;

/**
 * Created by lw on 2017/7/24.
 * 圆形Textview
 */

public class CircularTextView extends TextView {
    /**
     * 需要绘制的文字
     */
    private CharSequence mText = "世界";
    private ArrayList<String> mTextList = new ArrayList<>();
    private int mtextPaint = 96;
    private int mPadding = 0;
    private double multiple = 1.5;
    private int textColor = Color.BLACK;
    private int backColor = Color.WHITE;

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);
        mText = array.getText(R.styleable.CircularTextView_text);
        mPadding = (int) array.getDimension(R.styleable.CircularTextView_padding, 0f);
        mtextPaint = (int) array.getDimension(R.styleable.CircularTextView_textSize, 12);
        textColor = array.getColor(R.styleable.CircularTextView_textColor, Color.BLACK);
        backColor = array.getColor(R.styleable.CircularTextView_backgroudColor, Color.WHITE);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getWidthMeasure(widthMeasureSpec);
        int height = getHeightMeasure(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int getWidthMeasure(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int widthSize = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                widthSize = (int) Math.sqrt(Math.pow(mTextList.size() * mtextPaint * multiple, 2)) + 2 * mPadding;
                break;
        }
        return widthSize;
    }

    private int getHeightMeasure(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int heightSize = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec); //获取高的尺寸
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightSize = MeasureSpec.getSize(heightMeasureSpec);   //获取宽的尺寸
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                heightSize = 2 * (int) Math.sqrt(Math.pow(mTextList.size() * mtextPaint * multiple, 2)) + 2 * mPadding;
                break;
        }
        return heightSize;
    }

    private void initText() {
        if (mText == null)
            return;
        int line = getline(Math.sqrt(mText.length()));
        if (mText.length() <= 2) {
            multiple = 2.0;
        }
        mTextList.clear();
        if(line==1){
            mTextList.add(mText.toString());
        }else {
            for (int i = 0; i < line; i++) {
                int beginIndex = i * line;
                int endIndex = (i + 2) * line;
                if (beginIndex + 1 >= mText.length()) {
                    return;
                }
                if (endIndex > mText.length()) {
                    endIndex = mText.length();
                }
                mTextList.add(mText.toString().substring(beginIndex, endIndex));
            }
        }
    }

    private int getline(double line) {
        if (line > (int) line) {
            return (int) line + 1;
        }
        return (int) line;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initText();
        int viewWidth = getWidth() / 2;
        int viewHeight = getHeight() / 2;
        Paint backPaint = new Paint();
        backPaint.setColor(backColor);
        backPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2 - mPadding), backPaint);
        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(mtextPaint);
        textPaint.setStyle(Paint.Style.FILL);
        if (mText != null && !mText.equals("") && mTextList.size() > 0) {
            int textWidth = mTextList.get(0).length() * mtextPaint;
            int textHeight = mTextList.size() * mtextPaint;
            int startWidth = viewWidth - textWidth / 2;
            int startHeight = viewHeight - textHeight / 2- DisplayUtils.px2dp(getContext(),8);
            for (int i = 0; i < mTextList.size(); i++) {
                canvas.drawText(mTextList.get(i), startWidth, startHeight + (i + 1) * mtextPaint, textPaint);
            }
        }
    }

    public void setText(String mText) {
        this.mText = mText;
        invalidate();
    }
}
