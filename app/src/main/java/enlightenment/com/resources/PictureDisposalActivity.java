package enlightenment.com.resources;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.base.registered.RegisteredActivity;
import enlightenment.com.contents.FileUrls;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.view.ClipImageLayout;

/**
 * Created by lw on 2017/9/13.
 * 图片处理（缩放扩大后方形剪切）
 */

public class PictureDisposalActivity extends AppActivity implements View.OnClickListener {

    @BindView(R.id.top_left_image)
    ImageView leftImage;
    @BindView(R.id.top_center_text)
    TextView centerText;
    @BindView(R.id.top_right_text)
    TextView rightText;
    @BindView(R.id.clip_image)
    ClipImageLayout clipImageLayout;
    @BindView(R.id.clip_image_view)
    ImageView imageView;

    Bitmap bitmap=null;

    private boolean ClipFlag = false;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent=new Intent();
            intent.putExtra(RegisteredActivity.START_PHOTO_URL,msg.obj.toString());
            setResult(RegisteredActivity.START_COMPLETE_PHOTO,intent);
            finish();
        }
    };
    private Handler mThreadHandler=new Handler(EnlightenmentApplication.getInstance()
            .getHandlerThread().getLooper());

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clip_image;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        rightText.setText("剪切");
        rightText.setOnClickListener(this);
        centerText.setText("剪切图片");
        leftImage.setOnClickListener(this);
        Glide.with(this).load(new File(getIntent().getExtras().getString(RegisteredActivity.START_PHOTO_URL)))
                .into(clipImageLayout.getZoomImageView());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left_image:
                if (ClipFlag) {
                    clipImageLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                } else
                    finish();
                break;
            case R.id.top_right_text:
                if (!ClipFlag) {
                    bitmap = clipImageLayout.clip();
                    clipImageLayout.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                    rightText.setText("保存");
                    ClipFlag=!ClipFlag;
                } else {
                    mThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String url=FileUrls.PATH_PHOTO+System.currentTimeMillis()+".jpg";
                            FileUtils.writeFileBitmap(url,bitmap);
                            Message msg=mHandler.obtainMessage();
                            msg.obj=url;
                            mHandler.sendMessage(msg);
                        }
                    });
                }
                break;
        }
    }
}
