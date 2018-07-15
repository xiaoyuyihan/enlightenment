package enlightenment.com.resources;

import android.Manifest;
import android.content.ContentResolver;
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
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

public class AlbumActivity extends AppActivity implements GeneralAdapter.GeneralAdapterHelp{

    public static final int FLAG_PICTURE=1;
    private static final int REQUEST_PERMISSION_CAMERA_CODE = 2;
    public static final String EXTRA_DATA="extra_data";

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
    private ArrayList<AlbumBean> mData=new ArrayList();
    private GeneralAdapter<AlbumViewHolder> adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        rightText.setTextColor(getResources().getColor(R.color.mainTopColor));
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AlbumActivity.this,RegisteredActivity.class);
                startActivity(intent);
                finish();
            }
        });

        centerText.setText("相册");
        mThreadHandler=new Handler(EnlightenmentApplication.getInstance().getHandlerThread().getLooper());
        mHandler=new Handler(Looper.getMainLooper());
        adapter=new GeneralAdapter<>(
                AlbumViewHolder.class,this,R.layout.item_album_view,mData,this);
        //线性布局管理器
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestCameraPermission();
            }else {
                loadingAlbumCreate();
            }
        }else {
            loadingAlbumCreate();
        }

    }

    @Override
    protected void initData() {

    }


    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            if (granted){
                loadingAlbumCreate();
            }
        }
    }

    private void loadingAlbumCreate() {
        recyclerView.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {

                loadingAlbum();
            }
        });
    }

    private void loadingAlbum() {

        Map<String ,List> mAlbumData=new ArrayMap<>();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = getContentResolver();

        //只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

        if(mCursor == null){
            return;
        }

        while (mCursor.moveToNext()) {
            //获取图片的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));

            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();


            //根据父路径名将图片放入到mGroupMap中
            if (!mAlbumData.containsKey(parentName)) {
                List<String> chileList = new ArrayList<>();
                chileList.add(path);
                mAlbumData.put(parentName, chileList);
            } else {
                mAlbumData.get(parentName).add(path);
            }
        }
        mCursor.close();
        MapDataToMainList(mAlbumData);
        loadingConduct();
    }

    /**
     * 加载完成
     */
    private void loadingConduct() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void MapDataToMainList(Map Data) {
       Iterator<Map.Entry> items=Data.entrySet().iterator();
        while (items.hasNext()){
            Map.Entry<String,List<String>> item=items.next();
            AlbumBean mAlbumBean = new AlbumBean();
            mAlbumBean.setFolderName(item.getKey());
            mAlbumBean.setCounts(item.getValue().size());
            mAlbumBean.setFirstImagePath(item.getValue().get(0));
            mAlbumBean.setChilds((ArrayList<String>) item.getValue());
            mData.add(mAlbumBean);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AlbumViewHolder){
            AlbumViewHolder viewHolder=(AlbumViewHolder)holder;
            viewHolder.setText(mData.get(position).getFolderName());
            viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AlbumActivity.this,PictureActivity.class);
                    intent.putStringArrayListExtra(PictureActivity.PICTURE_EXTRA,mData.get(position).getChilds());
                    startActivityForResult(intent,FLAG_PICTURE);
                }
            });
            Glide.with(this).load(new File(mData.get(position).getFirstImagePath()))
                    .into(viewHolder.getImageView());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==FLAG_PICTURE){
            setResult(RegisteredActivity.START_SELECT_PHOTO,data);
            finish();
        }
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView name;
        private View view;

        public View getView() {
            return view;
        }

        public AlbumViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)itemView.findViewById(R.id.item_album_image);
            name=(TextView)itemView.findViewById(R.id.item_album_name);
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
        public void setText(String name){
            this.name.setText(name);
        }
    }

    static class AlbumBean{
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
