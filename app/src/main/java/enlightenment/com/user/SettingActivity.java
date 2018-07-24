package enlightenment.com.user;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.contents.Constants;
import enlightenment.com.main.ItemViewHolder;
import enlightenment.com.main.myself.MyselfFragment;

/**
 * Created by admin on 2018/6/9.
 * 用户自定义设置
 */

public class SettingActivity extends AppActivity {

    @BindView(R.id.top_left_image)
    ImageView mTopLeft;
    @BindView(R.id.top_right_text)
    TextView mTopRight;
    @BindView(R.id.top_center_text)
    TextView mContentText;

    @BindView(R.id.setting_recycler)
    RecyclerView mSetRecycler;

    private String[] mMessageSet;
    private String[] mSystemSet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mContentText.setText("设置");
        mTopRight.setTextColor(getResources().getColor(R.color.mainTopColor));
        mMessageSet = getResources().getStringArray(R.array.user_setting_message);
        mSystemSet = getResources().getStringArray(R.array.user_setting_system);
    }

    @Override
    protected void initData() {
        mSetRecycler.setLayoutManager(new LinearLayoutManager(this));
        mSetRecycler.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RecyclerView.ViewHolder holder;
                if (viewType == 0) {
                    View view = LayoutInflater.from(SettingActivity.this)
                            .inflate(R.layout.item_user_setting, parent, false);
                    holder = new SettingViewHolder(view);
                } else {
                    holder = new ItemViewHolder.BaseViewHolder(
                            ItemViewHolder.createTextView(SettingActivity.this,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (position == 0) {
                    ((TextView) holder.itemView).setText("消息设置");
                } else if (mMessageSet.length >= position && position > 0) {
                    SettingViewHolder viewHolder = (SettingViewHolder) holder;
                    viewHolder.setSetName(mMessageSet[position - 1]);
                    setSwitchOpen(viewHolder);
                } else if (position == mMessageSet.length + 1) {
                    ((TextView) holder.itemView).setText("系统设置");
                } else {
                    SettingViewHolder viewHolder = (SettingViewHolder) holder;
                    viewHolder.setSetName(mSystemSet[position - 2 - mMessageSet.length]);
                    setSwitchOpen(viewHolder);
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 1;
                } else if (position == mMessageSet.length + 1) {
                    return 1;
                } else {
                    return 0;
                }
            }

            @Override
            public int getItemCount() {
                return mMessageSet.length + mSystemSet.length + 2;
            }
        });
    }

    @Override
    protected void clearData() {

    }

    private void setSwitchOpen(SettingViewHolder viewHolder) {
        String name = viewHolder.getSetName();
        switch (name) {
            case "消息震动":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_MESSAGE_SHOCK, true));
                break;
            case "消息声音":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_MESSAGE_VOICE, true));
                break;
            case "操作声音":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_OPERATION_VOICE, true));
                break;
            case "点赞通知":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_PRAISE_NOTICE, true));
                break;
            case "计划通知":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_PLAN_NOTICE, true));
                break;
            case "收藏通知":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_COLLECT_NOTICE, true));
                break;
            case "打赏通知":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_REWARD_NOTICE, true));
                break;
            case "智能无图模式":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_PICTURE_MODE, false));
                break;
            case "夜间模式":
                viewHolder.setSwitchView(getSetSharedPreferences(
                        Constants.Set.SET_NIGHT_MODE, false));
                break;
        }
    }

    @OnClick(R.id.top_left_image)
    public void OnLeftClick(View v) {
        finish();
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_user_setting_image)
        ImageView mSetImage;
        @BindView(R.id.item_user_setting_name)
        TextView mSetName;
        @BindView(R.id.item_user_setting_switch)
        SwitchView mSwitchView;

        public SettingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.item_user_setting_switch)
        public void OnSetSwitch(View v) {
            String name = getSetName();
            switch (name) {
                case "消息震动":
                    setSetSharedPreferences(
                            Constants.Set.SET_MESSAGE_SHOCK, mSwitchView.isOpened());
                    break;
                case "消息声音":
                    setSetSharedPreferences(
                            Constants.Set.SET_MESSAGE_VOICE, mSwitchView.isOpened());
                    break;
                case "操作声音":
                    setSetSharedPreferences(
                            Constants.Set.SET_OPERATION_VOICE, mSwitchView.isOpened());
                    break;
                case "点赞通知":
                    setSetSharedPreferences(
                            Constants.Set.SET_PRAISE_NOTICE, mSwitchView.isOpened());
                    break;
                case "计划通知":
                    setSetSharedPreferences(
                            Constants.Set.SET_PLAN_NOTICE, mSwitchView.isOpened());
                    break;
                case "收藏通知":
                    setSetSharedPreferences(
                            Constants.Set.SET_COLLECT_NOTICE, mSwitchView.isOpened());
                    break;
                case "打赏通知":
                    setSetSharedPreferences(
                            Constants.Set.SET_REWARD_NOTICE, mSwitchView.isOpened());
                    break;
                case "智能无图模式":
                    setSetSharedPreferences(
                            Constants.Set.SET_PICTURE_MODE, mSwitchView.isOpened());
                    break;
                case "夜间模式":
                    setSetSharedPreferences(
                            Constants.Set.SET_NIGHT_MODE, mSwitchView.isOpened());
                    updateNight();
                    break;
            }
        }

        private void updateNight() {
            ((EnlightenmentApplication) getApplication()).updateDefaultNightMode();
            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }

        public ImageView getSetImage() {
            return mSetImage;
        }

        public void setSetImage(Drawable mSetImage) {
            this.mSetImage.setImageDrawable(mSetImage);
        }

        public String getSetName() {
            return mSetName.getText().toString();
        }

        public void setSetName(String mSetName) {
            this.mSetName.setText(mSetName);
        }

        public boolean isOpen() {
            return mSwitchView.isOpened();
        }

        public void setSwitchView(boolean isFlag) {
            this.mSwitchView.setOpened(isFlag);
        }

        public SwitchView getSwitchView() {
            return mSwitchView;
        }
    }

}
