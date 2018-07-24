package enlightenment.com.information;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edit.EditActivity;
import com.provider.view.LoadingDialog;
import com.utils.MessageDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.operationBean.ColumnBean;
import enlightenment.com.operationBean.InformationBean;
import enlightenment.com.service.UploadService;
import enlightenment.com.tool.device.SystemState;

/**
 * Created by lw on 2018/1/22.
 * 创作文本信息完善并提交（有所属栏目or无所属栏目就新建）
 */

public class InformationActivity extends AppActivity implements InformationView,
        View.OnClickListener, TypeCheckDialog.OnClickTypeCheck,
        ModelCheckDialog.OnClickModelCheck, OnItemClickListener,
        MessageDialogFragment.OnMsgDialogClickListener {
    public static final int ACTIVITY_INFORMATION_FLAG = 1;

    @BindView(R.id.top_center_text)
    TextView mTopCenterView;
    @BindView(R.id.top_right_text)
    TextView mTopSubject;

    @BindView(R.id.text_information_column)
    TextView mSaveColumnText;
    @BindView(R.id.text_information_news_column)
    TextView mNewsColumnText;
    @BindView(R.id.text_information_stub_column)
    ViewStub mStubColumn;
    @BindView(R.id.text_information_stub_news_column)
    ViewStub mNewsStubColumn;
    @BindView(R.id.text_information_column_name)
    EditText mName;
    @BindView(R.id.text_information_column_check)
    CheckBox mCheck;

    private LinearLayout mCreateColumnLayout;

    private LinearLayout mTypeLayout;
    private TextView mTypeView;
    private LinearLayout mModelLayout;
    private TextView mModelView;
    private LinearLayout mColumnLayout;
    private TextView mColumnView;

    private RecyclerView mColumnRecycler;

    private int mColumnClickType = -1;

    private boolean isVisibility = false;
    private String mTextData = null;
    /**
     * 视图类型
     */
    private int mType;
    private int mInformationType = -1;
    private int mModelID = -1;
    private int columnID = -1;
    private int fatherID = -1;

    private InformationPresenter informationPresenter;

    private ColumnAdapter columnAdapter;

    private ServiceConnection mCon;

    NewsColumnDialog newsProjectModelDialog;
    LoadingDialog loadingDialog;
    TypeCheckDialog typeCheckDialog;
    ModelCheckDialog modelCheckDialog;

    MessageDialogFragment messageDialogFragment;

    InformationBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_text_information;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mType = getIntent().getExtras().getInt(EditActivity.ACTIVITY_EDIT_TYPE);
        mTextData = getIntent().getExtras().getString(EditActivity.ACTIVITY_EDIT_TEXT, "");
        mTopCenterView.setText("作品信息");
        mTopSubject.setText("提交");
        mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isVisibility = b;
            }
        });
    }

    @Override
    protected void initData() {
        informationPresenter = InformationPresenter.getInstance();
        informationPresenter.BindView(this);
        informationPresenter.onStart();
    }

    @Override
    protected void clearData() {
        columnAdapter = null;
        informationPresenter.unBindView(this);
        informationPresenter.onStop();
        informationPresenter = null;
    }

    @OnClick(R.id.top_left_image)
    public void onBack(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.ACTIVITY_EDIT_TYPE, mType);
        intent.putExtra(EditActivity.ACTIVITY_EDIT_TEXT, mTextData);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.top_right_text)
    public void onSubject(View view) {
        checkNotification();
    }

    private void checkNotification() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (!manager.areNotificationsEnabled()) {
            messageDialogFragment = new MessageDialogFragment();
            messageDialogFragment.setMessage("我们找不到通知你的方式，请给我们开启访问通知对权限");
            messageDialogFragment.setMessageImageVisibility(View.GONE);
            messageDialogFragment.setOnMsgDialogClickListener(this);
            messageDialogFragment.show(getSupportFragmentManager(), "MessageDialogFragment");
        } else {
            subjectContent();
        }
        showCustomToast("通知开启：" + manager.areNotificationsEnabled());
    }

    private void subjectContent() {
        if (SystemState.isNetworkState()) {
            createInformationBean();
            onBindService();
        } else
            showToast("我找不到回家的地址了，请检查网络");
    }


    @SuppressLint("WrongConstant")
    private void onBindService() {
        if (bean == null) {
            return;
        }
        mCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof UploadService.UploadBinder)
                    ((UploadService.UploadBinder) iBinder).addUploadData(bean);
                unbindService(mCon);
                finish();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intentService = new Intent(this, UploadService.class);
        startService(intentService);
        bindService(intentService, mCon, Service.BIND_AUTO_CREATE);
    }

    private void createInformationBean() {
        String informName = mName.getText().toString();
        if (informName.equals("")) {
            showToast("标题不能为空");
            return;
        }
        if (columnID < 0) {
            showToast("文章栏目所属不能为空");
            return;
        }
        if (mInformationType < 0) {
            showToast("文章所属类型不能为空");
            return;
        }
        bean = new InformationBean();
        bean.setName(informName);
        bean.setContent(mTextData);
        bean.setVisition(isVisibility ? "2" : "1");
        bean.setViewType(String.valueOf(mType));
        bean.setType(String.valueOf(mInformationType));
        bean.setColumnID(String.valueOf(columnID));
        bean.setFatherID(String.valueOf(fatherID));
    }

    /**
     * 选择栏目
     *
     * @param view
     */
    @OnClick(R.id.text_information_column)
    public void onClickColumn(View view) {
        mColumnClickType = 1;
        showLoadingView(null);
        informationPresenter.loadColumn();
    }

    private void initRecyclerView() {
        if (columnAdapter != null) {
            columnAdapter.notifyDataSetChanged();
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            columnAdapter = new ColumnAdapter(informationPresenter.getColumnBeanList());
            columnAdapter.setOnItemClickListener(this);
            mColumnRecycler.setLayoutManager(linearLayoutManager);
            mColumnRecycler.setAdapter(columnAdapter);
        }
    }

    /**
     * 新建栏目
     *
     * @param view
     */
    @OnClick(R.id.text_information_news_column)
    public void onClickNewsColumn(View view) {
        mColumnClickType = 2;
        showColumnNews();
    }

    private void showLoadingView(String msg) {
        loadingDialog = new LoadingDialog();
        if (msg != null) {
            Bundle bundle = new Bundle();
            bundle.putString(LoadingDialog.DIALOG_LOAD_NAME, msg);
            loadingDialog.setArguments(bundle);
        }
        loadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
    }

    /**
     * 新建栏目并保存
     */
    private void initNewsColumnView() {
        if (mCreateColumnLayout == null) {
            return;
        }
        mTypeLayout = (LinearLayout) mCreateColumnLayout.findViewById(R.id.layout_information_create_type);
        mTypeLayout.setOnClickListener(this);
        mTypeView = (TextView) mCreateColumnLayout.findViewById(R.id.view_information_create_type);
        mModelLayout = (LinearLayout) mCreateColumnLayout.findViewById(R.id.layout_information_create_model);
        mModelLayout.setOnClickListener(this);
        mModelView = (TextView) mCreateColumnLayout.findViewById(R.id.view_information_create_model);
        mColumnLayout = (LinearLayout) mCreateColumnLayout.findViewById(R.id.layout_information_create_column);
        mColumnLayout.setOnClickListener(this);
        mColumnView = (TextView) mCreateColumnLayout.findViewById(R.id.view_information_create_column);
    }

    @Override
    public void showToast(String message) {
        //Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        showCustomToast(message);
    }

    @Override
    public Object getObj() {
        return null;
    }

    /**
     * show column 选择
     */
    @Override
    public void showColumnCheck() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        if (mCreateColumnLayout != null) {
            mCreateColumnLayout.setVisibility(View.GONE);
        }
        if (mColumnRecycler == null) {
            mColumnRecycler = (RecyclerView) mStubColumn.inflate();
            initRecyclerView();
        } else {
            mColumnRecycler.setVisibility(View.VISIBLE);
        }
    }

    /**
     * show 栏目
     */
    @Override
    public void showColumnNews() {
        if (mColumnRecycler != null) {
            mColumnRecycler.setVisibility(View.GONE);
        }
        if (mCreateColumnLayout == null) {
            mCreateColumnLayout = (LinearLayout) mNewsStubColumn.inflate();
            initNewsColumnView();
        } else {
            mCreateColumnLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 新建栏目Dialog
     *
     * @param fatherID 父ID
     * @param isChild  是否为子栏目
     */
    public void showNewsColumnDialog(int fatherID, boolean isChild) {
        newsProjectModelDialog = new NewsColumnDialog(new NewsColumnDialog.OnClickListener() {
            @Override
            public void Back() {
                newsProjectModelDialog.dismiss();
                newsProjectModelDialog = null;
            }

            @Override
            public void onNewsColumnCall(boolean isChild, ColumnBean columnBean) {
                newsProjectModelDialog.dismiss();
                newsProjectModelDialog = null;
                mColumnView.setText(columnBean.getName());
                columnID = columnBean.getId();
                showLoadingView("文本上传中···");
            }

            @Override
            public void onNewsChildColumnCall(boolean isChild, ColumnBean.ColumnChildBean child) {
                newsProjectModelDialog.dismiss();
                newsProjectModelDialog = null;
                if (isChild) {
                    //如果是选择栏目中新建子栏目
                    if (mColumnClickType == 1) {
                        mSaveColumnText.performClick();
                    } else {
                        //保存子栏目
                    }
                } else {
                    mColumnView.setText(child.getName());
                    showLoadingView("文本上传中···");
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt(NewsColumnDialog.DIALOG_MODEL_NEWS_TYPE, mInformationType);
        bundle.putInt(NewsColumnDialog.DIALOG_MODEL_NEWS_ID, fatherID);
        bundle.putBoolean(NewsColumnDialog.DIALOG_MODEL_NEWS_CHILD, isChild);
        newsProjectModelDialog.setArguments(bundle);
        newsProjectModelDialog.show(getSupportFragmentManager(), "NewsColumnDialog");
    }

    /**
     * 类型选择
     */
    public void showTypeDialog() {
        typeCheckDialog = new TypeCheckDialog();
        typeCheckDialog.setOnClickTypeCheck(this);
        typeCheckDialog.show(getSupportFragmentManager(), "TypeCheckDialog");
    }

    @Override
    public void showModelDialog() {
        loadingDialog.dismiss();
        loadingDialog = null;
        modelCheckDialog = new ModelCheckDialog();
        Bundle data = new Bundle();
        data.putParcelableArrayList(ModelCheckDialog.DIALOG_MODEL_DATA, informationPresenter.getModuleBeanList());
        modelCheckDialog.setArguments(data);
        modelCheckDialog.setOnClickModelCheck(this);
        modelCheckDialog.show(getSupportFragmentManager(), "ModelCheckDialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_information_create_type:
                showTypeDialog();
                break;
            case R.id.layout_information_create_model:
                if (mInformationType < 0) {
                    showToast("未选择类型");
                } else
                    loadingModule();
                break;
            case R.id.layout_information_create_column:
                if (mModelID < 0) {
                    showToast("未选择方向");
                } else
                    showNewsColumnDialog(mModelID, false);
                break;
        }
    }


    private void loadingModule() {
        showLoadingView(null);
        informationPresenter.loadModel(mInformationType);
    }

    @Override
    public void onClickType(int type) {
        mInformationType = type;
        if (type == TypeCheckDialog.CHECK_TYPE_DIY) {
            mTypeView.setText("创造DIY");
        } else {
            mTypeView.setText("学习笔记");
        }
        typeCheckDialog.dismiss();
        typeCheckDialog = null;
    }

    @Override
    public void onClickCheck(int fatherPosition, int childPosition) {
        ModuleBean currentBean = (ModuleBean) informationPresenter.getModuleBeanList().get(fatherPosition);
        mModelView.setText(currentBean.getName() + "—" + currentBean.getChildBeen().get(childPosition).getName());
        mModelID = currentBean.getChildBeen().get(childPosition).getIdentity();
        modelCheckDialog.dismiss();
        modelCheckDialog = null;
    }

    /**
     * 栏目选择item
     *
     * @param columnID
     * @param fatherID
     * @param columnName
     */
    @Override
    public void onItemClick(int columnID, int fatherID, String columnName) {
        this.columnID = columnID;
        this.fatherID = fatherID;
        SpannableString spanned = new SpannableString("保存到 " + columnName);
        spanned.setSpan(new ForegroundColorSpan(
                        getResources().getColor(R.color.black_50)),
                0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanned.setSpan(new ForegroundColorSpan(
                        getResources().getColor(R.color.colorPrimary)),
                4, spanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小
        spanned.setSpan(new AbsoluteSizeSpan(42), 4, spanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSaveColumnText.setText(spanned);
    }

    @Override
    public void onSureBut() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, ACTIVITY_INFORMATION_FLAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ACTIVITY_INFORMATION_FLAG) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            if (manager.areNotificationsEnabled() && messageDialogFragment != null) {
                messageDialogFragment.dismiss();
                messageDialogFragment = null;
                subjectContent();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onCancel() {
        messageDialogFragment.dismiss();
        messageDialogFragment = null;
        subjectContent();
    }

    class ColumnAdapter extends RecyclerView.Adapter {

        private ArrayList<ColumnBean> columnBeanArrayList;
        private LayoutInflater layoutInflater;
        private OnItemClickListener onItemClickListener;
        private int type = -1;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ColumnAdapter(ArrayList<ColumnBean> bean) {
            this.columnBeanArrayList = bean;
            layoutInflater = LayoutInflater.from(InformationActivity.this);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ColumnViewHolder columnViewHolder = new ColumnViewHolder(
                    layoutInflater.inflate(R.layout.item_information_column, parent, false));
            return columnViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ColumnViewHolder columnViewHolder = ((ColumnViewHolder) holder);
            final ColumnBean columnBean = columnBeanArrayList.get(position);
            columnViewHolder.setChildRecycler(columnBean.getChild(), layoutInflater);
            columnViewHolder.setOnItemClick(onItemClickListener);
            columnViewHolder.setColumnName(columnBean.getName());
            if (type != columnBean.getType() && columnBean.getType() == 1) {
                columnViewHolder.showStub("学习");
                type = columnBean.getType();
            } else if (type != columnBean.getType() && columnBean.getType() == 2) {
                columnViewHolder.showStub("创造");
                type = columnBean.getType();
            }
            columnViewHolder.setColumnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(columnBean.getId(), -1, columnBean.getName());
                    mInformationType = columnBean.getType();
                }
            });
            columnViewHolder.setImageOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInformationType = columnBean.getType();
                    showNewsColumnDialog(columnBean.getId(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return columnBeanArrayList.size();
        }
    }

    class ColumnViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_information_column_stub)
        ViewStub mRecyclerStub;
        @BindView(R.id.item_information_column_name)
        TextView mColumnName;
        @BindView(R.id.item_information_column_recycler)
        RecyclerView mChildRecycler;
        @BindView(R.id.item_information_column_add_child)
        ImageView mChildImage;

        private ColumnChildAdapter columnChildAdapter;
        private LinearLayoutManager linearLayoutManager;

        public ColumnViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showStub(String name) {
            TextView mName = (TextView) mRecyclerStub.inflate();
            mName.setText(name);
        }

        public void setColumnName(String columnName) {
            mColumnName.setText(columnName);
        }

        public void setImageOnClick(View.OnClickListener onClick) {
            mChildImage.setOnClickListener(onClick);
        }

        public void setColumnClick(View.OnClickListener onClickListener) {
            mColumnName.setOnClickListener(onClickListener);
        }

        public void setChildRecycler(ArrayList<ColumnBean.ColumnChildBean> childBeen, LayoutInflater inflater) {
            columnChildAdapter = new ColumnChildAdapter(childBeen, inflater);
            linearLayoutManager = new LinearLayoutManager(InformationActivity.this, LinearLayoutManager.HORIZONTAL, false);
            mChildRecycler.setLayoutManager(linearLayoutManager);
            mChildRecycler.setAdapter(columnChildAdapter);
        }

        public void setOnItemClick(OnItemClickListener onItemClick) {
            columnChildAdapter.setOnItemClickListener(onItemClick);
        }

    }

    class ColumnChildAdapter extends RecyclerView.Adapter {

        private ArrayList<ColumnBean.ColumnChildBean> columnChildBeen;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        private LayoutInflater inflater;

        public ColumnChildAdapter(ArrayList<ColumnBean.ColumnChildBean> mData, LayoutInflater inflater) {
            this.columnChildBeen = mData;
            this.inflater = inflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ColumnChildViewHolder columnChildViewHolder = new ColumnChildViewHolder(
                    inflater.inflate(R.layout.item_information_text, parent, false));
            return columnChildViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ColumnChildViewHolder columnChildViewHolder = ((ColumnChildViewHolder) holder);
            final ColumnBean.ColumnChildBean bean = columnChildBeen.get(position);
            columnChildViewHolder.setInformationText(bean.getName());
            columnChildViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(bean.getFatherID(), bean.getId(), bean.getName());
                    mInformationType = bean.getType();
                }
            });
        }

        @Override
        public int getItemCount() {
            return columnChildBeen.size();
        }
    }

    class ColumnChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_information_text)
        TextView mInformationText;

        public ColumnChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setInformationText(String name) {
            mInformationText.setText(name);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mInformationText.setOnClickListener(onClickListener);
        }
    }
}
