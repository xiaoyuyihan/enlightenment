package enlightenment.com.resources;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.provider.utils.ContentProviderUtils;
import com.provider.utils.ImageBean;
import com.provider.view.ProviderActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.base.registered.RegisteredActivity;

/**
 * Created by lw on 2017/9/13.
 * 相册
 */

public class AlbumActivity extends AppActivity implements GeneralAdapter.GeneralAdapterHelp ,ContentProviderUtils.OnReadListener{

    public static final int FLAG_PICTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA_CODE = 2;
    public static final String EXTRA_DATA = "extra_data";

    @BindView(R.id.album_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.top_left_image)
    ImageView leftImage;
    @BindView(R.id.top_center_text)
    TextView centerText;
    @BindView(R.id.top_right_text)
    TextView rightText;

    private Handler mThreadHandler;
    private Handler mHandler;
    private ArrayList<AlbumBean> mData = new ArrayList();
    private GeneralAdapter<AlbumViewHolder> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        rightText.setTextColor(getResources().getColor(R.color.mainTopColor));
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumActivity.this, RegisteredActivity.class);
                startActivity(intent);
                finish();
            }
        });

        centerText.setText("相册");
        mThreadHandler = new Handler(EnlightenmentApplication.getInstance().getHandlerThread().getLooper());
        mHandler = new Handler(Looper.getMainLooper());
        adapter = new GeneralAdapter<>(
                AlbumViewHolder.class, this, R.layout.item_album_view, mData, this);
        //线性布局管理器
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestCameraPermission();
            } else {
                loadingAlbumCreate();
            }
        } else {
            loadingAlbumCreate();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void clearData() {

    }


    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA_CODE);
        }
    }

    private void loadingAlbumCreate() {
        recyclerView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        loadingAlbum();
    }

    private void loadingAlbum() {

        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );
        if (isAllGranted) {
            ContentProviderUtils.getInstance(this)
                    .setRecentSize(50)
                    .setReadListener(this)
                    .subjectType(ContentProviderUtils.TYPE_PHOTO);
            return;
        }
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1
        );
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                ContentProviderUtils.getInstance(this)
                        .setRecentSize(50)
                        .setReadListener(this)
                        .subjectType(ContentProviderUtils.TYPE_PHOTO);
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("备份通讯录需要访问 “外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlbumActivity.this.finish();
            }
        });
        builder.show();
    }

    /**
     * 加载完成
     */
    private void loadingConduct() {
        recyclerView.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    private void MapDataToMainList(Map Data) {
        Iterator<Map.Entry> items = Data.entrySet().iterator();
        while (items.hasNext()) {
            Map.Entry<String, List<ImageBean>> item = items.next();
            AlbumBean mAlbumBean = new AlbumBean();
            ArrayList<String> urls = getArray(item.getValue());
            mAlbumBean.setFolderName(item.getValue().get(0).getParentName());
            mAlbumBean.setCounts(item.getValue().size());
            mAlbumBean.setFirstImagePath(urls.get(0));
            mAlbumBean.setChilds(urls);
            mData.add(mAlbumBean);
        }
    }

    private ArrayList<String> getArray(List<ImageBean> value) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ImageBean imageBean:value)
            arrayList.add(imageBean.getPath());
        return arrayList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AlbumViewHolder) {
            AlbumViewHolder viewHolder = (AlbumViewHolder) holder;
            viewHolder.setText(mData.get(position).getFolderName());
            viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AlbumActivity.this, PictureActivity.class);
                    intent.putStringArrayListExtra(PictureActivity.PICTURE_EXTRA, mData.get(position).getChilds());
                    startActivityForResult(intent, FLAG_PICTURE);
                }
            });
            Glide.with(this).load(new File(mData.get(position).getFirstImagePath()))
                    .into(viewHolder.getImageView());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLAG_PICTURE) {
            setResult(RegisteredActivity.START_SELECT_PHOTO, data);
            finish();
        }
    }

    @Override
    public void onRead(Map paramMap) {
        MapDataToMainList(paramMap);
        loadingConduct();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView name;
        private View view;

        public View getView() {
            return view;
        }

        public AlbumViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.item_album_image);
            name = (TextView) itemView.findViewById(R.id.item_album_name);
        }

        public TextView getName() {
            return name;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImage(Bitmap image) {
            this.imageView.setImageBitmap(image);
        }

        public void setText(String name) {
            this.name.setText(name);
        }
    }

    static class AlbumBean {
        private String folderName;
        private int counts;
        private String firstImagePath;
        private ArrayList<String> childs;

        public String getFolderName() {
            return folderName;
        }

        public int getCounts() {
            return counts;
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public ArrayList<String> getChilds() {
            return childs;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public void setChilds(ArrayList<String> childs) {
            this.childs = childs;
        }
    }
}
