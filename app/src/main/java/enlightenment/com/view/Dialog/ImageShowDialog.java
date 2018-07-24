package enlightenment.com.view.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.R;
import enlightenment.com.service.SerMessageService;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.tool.File.GlideUtils;

/**
 * Created by lw on 2018/3/14.
 */

public class ImageShowDialog extends DialogFragment {

    public static String IMAGE_SHOW_DATA = "IMAGE_SHOW_DATA";

    private String url;
    @BindView(R.id.dialog_content_image)
    public ImageView imageView;
    @BindView(R.id.dialog_content_progress)
    public ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_dialog_image, container, false);
        ButterKnife.bind(this, view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels), (int) (dm.heightPixels));
        url = getArguments().getString(IMAGE_SHOW_DATA, null);
        if (url != null) {
            Glide.with(this).load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource,
                                                       String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            if (isFromMemoryCache)
                                progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .placeholder(R.drawable.ic_banner_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_banner_error)
                    .crossFade()
                    .into(imageView);
        }
    }

    @OnClick(R.id.dialog_content_back)
    public void onBack(View v) {
        dismiss();
    }

    @OnClick(R.id.dialog_content_down)
    public void onDown(View v) {
        Toast.makeText(getActivity(), "正在下载", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), SerMessageService.class);
        intent.putExtra(SerMessageService.SERVICE_DOWN_IMAGE_URL, url);
        intent.putExtra(SerMessageService.SERVICE_DATA_EXTRA, SerMessageService.ACTION_DETECT_DOWN_IMAGE);
        getActivity().startService(intent);
    }
}
