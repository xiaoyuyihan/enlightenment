package enlightenment.com.view.NineGridLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import enlightenment.com.base.R;
import enlightenment.com.tool.device.CheckUtils;
import enlightenment.com.tool.device.SystemState;

/**
 * Created by lw on 2018/3/9.
 */

public class ItemNineGridLayout extends NineGridLayout {
    private Context context;
    private OnClickImageListener onClickImageListener;
    private boolean isShow = true;

    public void setOnClickImageListener(OnClickImageListener onClickImageListener) {
        this.onClickImageListener = onClickImageListener;
    }

    public ItemNineGridLayout(Context context) {
        super(context);
        this.context = context;
        this.isShow = CheckUtils.isShowPhoto();
    }

    public ItemNineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, int parentWidth) {
        if (isShow|| SystemState.isWIFIState()){
            Glide.with(context).load(url).asBitmap().thumbnail(0.4f).into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int imageWidth = resource.getWidth();
                    int imageHeight = resource.getHeight();
                    int height = imageView.getWidth() * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = imageView.getLayoutParams();
                    if (para==null)
                        para=new LayoutParams(resource.getWidth(),resource.getHeight());
                    imageView.setLayoutParams(para);
                    imageView.setImageBitmap(resource);
                }
            });
        }
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        if (isShow||SystemState.isWIFIState()){
            DrawableRequestBuilder<String> thumbnailRequest = Glide.with( context ).load( url );
            Glide.with(context).load(url).dontAnimate()
                    .placeholder(R.drawable.ic_banner_default)
                    .thumbnail(thumbnailRequest)
                    .into(imageView);
        }
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        if (onClickImageListener!=null)
            onClickImageListener.onClickImage(url);
    }

    public interface OnClickImageListener{
        void onClickImage(String url);
    }
}
