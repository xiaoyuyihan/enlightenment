package enlightenment.com.question;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.edit.EditActivity;
import com.edit.OnBroadcastReceiverListener;
import com.edit.bean.EditBean;
import com.edit.custom.CustomEditFragment;
import com.edit.custom.CustomViewHolder;
import com.edit.custom.SimpleItemTouchHelperCallback;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.provider.utils.ContentProviderUtils;
import com.provider.utils.IntentBean;
import com.provider.view.ProviderActivity;
import com.service.MediaService;
import com.utils.MessageDialogFragment;
import com.utils.TypeConverUtil;
import com.webeditproject.R2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.view.FlowLayout;

/**
 * Created by admin on 2018/7/1.
 */

public class QuestionActivity extends AppActivity implements OnBroadcastReceiverListener, CustomEditFragment.AudioPlayClick {

    private static final int QUESTION_CURENT_BEAN_FLAG = 1;
    private static final int QUESTION_CURENT_LIST_FLAG = 2;

    @BindView(R.id.question_common_top_menu)
    FloatingActionsMenu mFloatingActionsMenu;
    @BindView(R.id.view_question_top_name)
    EditText mQuestionName;
    @BindView(R.id.view_question_top_select_layout)
    LinearLayout mSelectLayout;
    @BindView(R.id.view_question_top_flow_layout)
    FlowLayout mFlowLayout;
    @BindView(R.id.view_question_top_flow_name)
    TextView mFlowName;
    @BindView(R.id.question_content_recycler)
    RecyclerView mContentRecycler;

    private ArrayList<EditBean> mContent = new ArrayList();
    private ArrayList<ModuleBean> moduleBeans = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter mEditAdapter;
    MessageDialogFragment messageDialogFragment;

    private MediaService mediaService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaService = ((MediaService.MediaBinder) service).getService();
            if (mediaService.getMediaPlayer().isPlaying())
                mediaService.sendDurationBroad(mediaService.getMediaPlayer().getDuration());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaService = null;
        }
    };
    private EditActivity.DurationBroadcastReceiver broadcastReceiver = new EditActivity.DurationBroadcastReceiver(this);

    private SeekBar mViewHolderBar;
    private TextView mAudioTextView;
    private Handler mMainHandler = new Handler();
    private Timer mTimer = new Timer();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this);
        mEditAdapter = new EditAdapter(this, mContent);
        mContentRecycler.setPadding(12, 12, 12, 12);
        mContentRecycler.setLayoutManager(linearLayoutManager);
        mContentRecycler.setAdapter(mEditAdapter);
        mContentRecycler.setNestedScrollingEnabled(false);
        SimpleItemTouchHelperCallback mCallback = new SimpleItemTouchHelperCallback<>(mEditAdapter, mContent);
        mCallback.setSwipeState(new SimpleItemTouchHelperCallback.SwipeStateAlterHelper() {
            @Override
            public void onLeftMove(float moveX, RecyclerView.ViewHolder holder) {

            }

            @Override
            public void onRightMove(float moveX, RecyclerView.ViewHolder holder) {

            }

            @Override
            public void onMoveConsummation(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = mContentRecycler.getChildLayoutPosition(viewHolder.itemView);
                if (mContent.get(position).getType() != EditBean.TYPE_TEXT) {
                    showDeleteDialog(position);
                }else
                    mEditAdapter.notifyDataSetChanged();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mContentRecycler);
    }

    @Override
    protected void initData() {
        mContent.add(new EditBean(EditBean.TYPE_TEXT));
        moduleBeans.addAll(EnlightenmentApplication.getInstance().getMajorBeen());
        addFlowLayout();
    }

    private void addFlowLayout() {
        for (final ModuleBean bean : moduleBeans) {
            final TextView textView = createFlowTextView();
            textView.setText(bean.getName());
            textView.setTag(bean);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectLayout.getChildCount() > 2) {
                        mSelectLayout.removeViewAt(2);
                        mSelectLayout.removeViewAt(1);
                    } else if (mSelectLayout.getChildCount() == 2) {
                        mSelectLayout.removeViewAt(1);
                    }
                    mFlowLayout.removeAllViews();
                    ArrayList<ModuleChildBean> childBeans = ((ModuleBean) textView.getTag()).getChildBeen();
                    addChildFlowLayout(childBeans);
                    mSelectLayout.addView(createTextView(bean, moduleBeans));
                }
            });
            mFlowLayout.addView(textView);
        }
        mFlowLayout.invalidate();
    }

    private void addChildFlowLayout(final ArrayList<ModuleChildBean> childBeans) {
        for (final ModuleChildBean childBean : childBeans) {
            TextView textView = createFlowTextView();
            textView.setText(childBean.getName());
            textView.setTag(childBeans);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectLayout.getChildCount() > 2) {
                        mSelectLayout.removeViewAt(2);
                    }
                    mSelectLayout.addView(createTextView(childBean, childBeans));
                    setModuleViewVisibility(View.GONE);
                }
            });
            mFlowLayout.addView(textView);
        }
    }

    private TextView createFlowTextView() {
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 6, 6);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.drawable.background_main_corners_4);
        textView.setTextColor(getResources().getColor(R.color.white));
        return textView;
    }

    /**
     * @param bean check bean
     * @param data 所属数据集合
     * @return
     */
    private TextView createTextView(Object bean, ArrayList data) {
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 6, 6);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.drawable.background_green_corners_6);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTag(R.id.tag_bean_flag, bean);
        textView.setTag(R.id.tag_list_flag, data);
        if (bean instanceof ModuleBean) {
            textView.setText(((ModuleBean) bean).getName());
            textView.setId(((ModuleBean) bean).getIdentity());
        } else if (bean instanceof ModuleChildBean) {
            textView.setText(((ModuleChildBean) bean).getName());
            textView.setId(((ModuleChildBean) bean).getIdentity());
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object bean = view.getTag(R.id.tag_bean_flag);
                setModuleViewVisibility(View.VISIBLE);
                if (bean instanceof ModuleBean) {
                    mFlowLayout.removeAllViews();
                    addFlowLayout();
                    if (mSelectLayout.getChildCount() > 2) {
                        mSelectLayout.removeViewAt(2);
                    }
                } else if (bean instanceof ModuleChildBean) {
                    mFlowLayout.removeAllViews();
                    addChildFlowLayout((ArrayList<ModuleChildBean>) view.getTag(R.id.tag_list_flag));
                }
                mSelectLayout.invalidate();
                mFlowLayout.invalidate();
            }
        });
        return textView;
    }

    @OnClick(R.id.question_common_top_back)
    public void onBack(View view) {
        finish();
    }

    @OnClick({R.id.question_common_menu_photo, R.id.question_common_menu_video, R.id.question_common_menu_audio})
    public void onProviderActivity(View v) {
        Intent intent = null;
        mFloatingActionsMenu.performClick();
        int i = v.getId();
        if (i == R.id.question_common_menu_audio) {
            intent = new Intent("com.audio.ACTION_START_AUDIO");
            intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_AUDIO);

        } else if (i == R.id.question_common_menu_photo) {
            intent = new Intent("com.camera.ACTION_START_CAMERA");
            intent.putExtra("CAMERA_OPEN_TYPE", ContentProviderUtils.TYPE_PHOTO);
            intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_PHOTO);

        } else if (i == R.id.question_common_menu_video) {
            intent = new Intent("com.camera.ACTION_START_CAMERA");
            intent.putExtra("CAMERA_OPEN_TYPE", ContentProviderUtils.TYPE_VIDEO);
            intent.putExtra(ProviderActivity.TYPE_KEY, ContentProviderUtils.TYPE_VIDEO);

        }
        if (intent != null)
            startActivityForResult(intent, 2);
    }

    private void showDeleteDialog(final int position) {
        messageDialogFragment = new MessageDialogFragment();
        messageDialogFragment.setOnMsgDialogClickListener(new MessageDialogFragment.OnMsgDialogClickListener() {
            @Override
            public void onSureBut() {
                mContent.remove(position);
                if (mContent.size() - 1 != position) {
                    if (mContent.get(position).getType() == EditBean.TYPE_TEXT &&
                            mContent.get(position - 1).getType() == EditBean.TYPE_TEXT) {
                        String content = mContent.get(position - 1).getHTML5() + "\\n\\n" + mContent.get(position).getHTML5();
                        mContent.get(position - 1).setHTML5(content);
                        mContent.remove(position);
                    }
                }
                messageDialogFragment.dismiss();
                mEditAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                messageDialogFragment.dismiss();
                mEditAdapter.notifyDataSetChanged();
            }
        });
        messageDialogFragment.show(getSupportFragmentManager(), "MessageDialogFragment");
    }

    private void setModuleViewVisibility(int visibility) {
        mFlowLayout.setVisibility(visibility);
        mFlowName.setVisibility(visibility);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStartService();
        IntentFilter intentFilter = new IntentFilter();
        //设置接收广播的类型
        intentFilter.addAction(MediaService.ACTION_MEDIA_SERVICE);
        //调用Context的registerReceiver（）方法进行动态注册
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
        unregisterReceiver(broadcastReceiver);
    }


    public void onStartService() {
        Intent intent = new Intent(this, MediaService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            ArrayList d = IntentBean.getInstance().getChecks();
            EditBean bean = mContent.get(mContent.size() - 1);
            if (bean.getHTML5() != null && !bean.getHTML5().equals("")) {
                mContent.addAll(d);
                mContent.add(new EditBean(EditBean.TYPE_TEXT));
            } else {
                mContent.addAll(mContent.size() - 1, d);
            }
            IntentBean.getInstance().getChecks().clear();
        }
        mEditAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiver(Intent intent) {
        mViewHolderBar.setMax(intent.getIntExtra(MediaService.DATA_AUDIO_DURATION, 0));
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                final int current = mediaService.getMediaPlayer().getCurrentPosition();
                mViewHolderBar.setProgress(current);
                mViewHolderBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaService.onSeekTo(seekBar.getProgress());
                    }
                });
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAudioTextView.setText(TypeConverUtil.TimeMSToMin(current));
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask, 0, 10);
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, String path) {
        if (holder instanceof CustomViewHolder.AudioViewHolder) {
            mViewHolderBar = ((CustomViewHolder.AudioViewHolder) holder).getSeekBar();
            mAudioTextView = ((CustomViewHolder.AudioViewHolder) holder).getCurrentTime();
        }
        mediaService.setMediaDataSource(path);
    }
}
