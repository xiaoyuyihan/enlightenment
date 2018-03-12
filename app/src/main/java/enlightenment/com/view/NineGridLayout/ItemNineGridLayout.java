package enlightenment.com.view.NineGridLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import enlightenment.com.base.R;

/**
 * Created by lw on 2018/3/9.
 */

public class ItemNineGridLayout extends NineGridLayout {
    private Context context;

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
        Glide.with(context).load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        if (params==null){
                            params=new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                        }
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .override(imageView.getWidth(), imageView.getHeight())
                .into(imageView);
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

    }
}
