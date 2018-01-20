package enlightenment.com.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.base.RegisteredActivity;

/**
 * Created by lw on 2017/9/13.
 * 相册内图片
 */

public class PictureActivity extends AppActivity implements View.OnClickListener {

    public static final String PICTURE_EXTRA = "EXTRA_DATA";

    private List<String> paths;

    @InjectView(R.id.picture_grid)
    GridView gridView;
    @InjectView(R.id.top_left_image)
    ImageView leftView;
    @InjectView(R.id.top_center_text)
    TextView centerView;
    @InjectView(R.id.top_right_text)
    TextView rightView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.inject(this);
        paths = getIntent().getExtras().getStringArrayList(PICTURE_EXTRA);
        rightView.setTextColor(getResources().getColor(R.color.mainTopColor));
        leftView.setOnClickListener(this);
        centerView.setText("选择图片");
        gridView.setAdapter(new PictureAdapter());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.top_left_image) {
            Intent intent = new Intent(this, AlbumActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class PictureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public String getItem(int i) {
            return paths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(PictureActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(360,
                    360));
            Glide.with(PictureActivity.this).load(getItem(i))
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishStart(getItem(i), RegisteredActivity.class);
                }
            });
            return imageView;
        }
    }

    private void finishStart(String url, Class name) {
        Intent intent = new Intent(this, name);
        intent.putExtra(RegisteredActivity.START_PHOTO_URL, url);
        setResult(AlbumActivity.FLAG_PICTURE, intent);
        finish();
    }
}