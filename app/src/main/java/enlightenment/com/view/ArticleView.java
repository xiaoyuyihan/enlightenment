package enlightenment.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import enlightenment.com.base.R;

/**
 * Created by lw on 2017/9/25.
 * @ 条状完成度
 */

public class ArticleView extends View {

    public static final String TAG=ArticleView.class.getName();

    private int height;
    private int width;
    private int articleHeight=12;                      //柱高

    private float completeDegree=0.5f;

    private int textColor;
    private int textCompleteColor;
    private int background;                       //背景
    private int completeColor;                    //完成颜色

    private int padding=6;

    private CharSequence articleText="30天";
    private CharSequence completeText="15天";
    private int textSize=12;

    private Paint mPaint=new Paint();


    public ArticleView(Context context) {
        super(context);
        init(context);
    }

    public ArticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArticleView);
        articleHeight=(int)array.getDimension(R.styleable.ArticleView_articleHeight,12);
        completeDegree=array.getFloat(R.styleable.ArticleView_completeDegree,0.5f);
        textColor=array.getColor(R.styleable.ArticleView_textArticleColor,getResources().getColor(R.color.colorLogin));
        textCompleteColor=array.getColor(R.styleable.ArticleView_textCompleteColor,getResources().getColor(R.color.colorAccent));
        background=array.getColor(R.styleable.ArticleView_backgroundColor,getResources().getColor(R.color.colorLogin));
        completeColor=array.getColor(R.styleable.ArticleView_completeColor,getResources().getColor(R.color.colorAccent));
        padding=(int)array.getDimension(R.styleable.ArticleView_articlePadding,6);
        textSize=(int)array.getDimension(R.styleable.ArticleView_articleTextSize,12);
        array.recycle();
    }

    public ArticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        textColor=background=getResources().getColor(R.color.colorLogin);
        textCompleteColor=completeColor=getResources().getColor(R.color.colorAccent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasuredWidth(widthMeasureSpec),getMeasuredHeight(heightMeasureSpec));
    }

    private int getMeasuredWidth(int widthMeasureSpec){
        width=MeasureSpec.getSize(widthMeasureSpec);
        Log.d(TAG,"width:"+width);
        return width;
    }

    private int getMeasuredHeight(int heightMeasureSpec){
        height=MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG,"height:"+height);
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArticle(canvas,1f,background);
        drawArticle(canvas,completeDegree,completeColor);
        drawText(canvas);
        drawArticleText(canvas);
    }

    private void drawArticleText(Canvas canvas) {
        mPaint.reset();
        mPaint.setTextSize(textSize);
        mPaint.setColor(textCompleteColor);
        mPaint.setAntiAlias(true);
        float start;
        float textLong=completeText.length()*textSize;
        if (completeDegree*width>textLong){
            start=width*completeDegree-textLong/2;
        }else{
            start=width*completeDegree;
        }
        canvas.drawText(completeText.toString(),start,textSize+articleHeight+padding*2,mPaint);
    }

    private void drawText(Canvas canvas) {
        mPaint.reset();
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        canvas.drawText(articleText.toString(),width-articleText.length()*textSize,textSize+articleHeight+padding*2,mPaint);
    }

    private void drawArticle(Canvas canvas, float degree, int color) {
        drawLeftSemicircle(canvas,color);
        drawCenter(canvas,degree);
        drawRightSemicircle(canvas,degree);
    }

    private void drawLeftSemicircle(Canvas canvas,int color) {
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF( padding, padding,padding+articleHeight,padding+articleHeight);
        canvas.drawArc(oval,90,180,true,mPaint);
    }
    private void drawCenter(Canvas canvas,float degree) {
        RectF oval = new RectF(padding+articleHeight/2, padding,width*degree-padding-articleHeight/2,padding+articleHeight);
        canvas.drawRect(oval,mPaint);
    }
    private void drawRightSemicircle(Canvas canvas,float degree) {
        RectF oval = new RectF(width*degree-padding-articleHeight, padding,width*degree-padding,padding+articleHeight);
        canvas.drawArc(oval,270,180,true,mPaint);
    }

    public void setHeight(int height) {
        this.height = height;
        invalidate();
    }

    public void setCompleteDegree(float completeDegree) {
        this.completeDegree = completeDegree;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setWidth(int width) {
        this.width = width;
        invalidate();
    }

    public void setArticleText(CharSequence articleText) {
        this.articleText = articleText;
        invalidate();
    }

    public void setCompleteText(CharSequence completeText) {
        this.completeText = completeText;
        invalidate();
    }

    public void setArticleHeight(int articleHeight) {
        this.articleHeight = articleHeight;
        invalidate();
    }

    public void setBackground(int background) {
        this.background = background;
        invalidate();
    }

    public void setCompleteColor(int completeColor) {
        this.completeColor = completeColor;
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTextCompleteColor(int textCompleteColor) {
        this.textCompleteColor = textCompleteColor;
        invalidate();
    }
}