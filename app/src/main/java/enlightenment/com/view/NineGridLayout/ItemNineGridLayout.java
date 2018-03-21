package enlightenment.com.view.NineGridLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import enlightenment.com.base.R;

/**
 * Created by lw on 2018/3/9.
 */

public class ItemNineGridLayout extends NineGridLayout {
    private Context context;
    private OnClickImageListener onClickImageListener;

    public void setOnClickImageListener(OnClickImageListener onClickImageListener) {
        this.onClickImageListener = onClickImageListener;
    }

    public ItemNineGridLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ItemNineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, int parentWidth) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int size=resource.getByteCount();
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = imageView.getWidth() * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.height = height;
                para.width = imageView.getWidth();
                imageView.setImageBitmap(resource);
            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.ic_banner_default)
                .into(imageView);
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
