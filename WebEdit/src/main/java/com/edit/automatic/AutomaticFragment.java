package com.edit.automatic;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edit.EditActivity;
import com.edit.OnEditOperationListener;
import com.edit.OnFragmentResultListener;
import com.edit.bean.EditBean;
import com.provider.utils.ContentProviderUtils;
import com.provider.utils.IntentBean;
import com.utils.BitmapUtil;
import com.webedit.HtmlEditActivity;
import com.webeditproject.R;
import com.webeditproject.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lw on 2018/1/18.
 *
 * @author 系统排版
 */

public class AutomaticFragment extends Fragment implements OnFragmentResultListener {
    private static AutomaticFragment mAutomaticFragment;
    private OnEditOperationListener listener;

    public OnEditOperationListener getOnEditOperationListener() {
        return listener;
    }

    public void setOnEditOperationListener(OnEditOperationListener listener) {
        this.listener = listener;
    }

    public static AutomaticFragment getInstance(OnEditOperationListener listener) {
        if (mAutomaticFragment == null)
            mAutomaticFragment = new AutomaticFragment();
        mAutomaticFragment.setOnEditOperationListener(listener);
        return mAutomaticFragment;
    }

    @BindView(R2.id.fragment_automatic_tool)
    LinearLayout mToolLayout;
    @BindView(R2.id.fragment_automatic_tool_top)
    ImageView mToolTopView;
    @BindView(R2.id.fragment_automatic_tool_recycler)
    RecyclerView mToolContentRecycler;
    @BindView(R2.id.fragment_automatic_tool_recycler_bottom)
    RecyclerView mToolRecycler;
    @BindView(R2.id.fragment_automatic_edit)
    EditText mEditText;

    private ArrayList<EditBean> mEditBeanList = new ArrayList<>();
    private ArrayList<EditBean> mCheckBeanList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private AutomaticEditAdapter mAdapter;
    private String[] mToolNames;
    private int[] mToolRes;

    private int height = 400;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_automatic, container, false);
        ButterKnife.bind(this, contentView);
        init();
        return contentView;
    }


    private void init() {
        int mType=getArguments().getInt(EditActivity.ACTIVITY_EDIT_TYPE);
        if (mType==EditActivity.ACTIVITY_EDIT_TYPE_AUTOMATIC){
            String text=getArguments().getString(EditActivity.ACTIVITY_EDIT_TEXT);
            mEditText.setText(text);
            //mEditBeanList.clear();
            //mEditBeanList.addAll(IntentBean.getInstance().getData());
        }
        initToolView();
    }

    private void initToolView() {
        TypedArray ar = getResources().obtainTypedArray(R.array.fragment_automatic_tool_drawable);
        mToolRes = new int[ar.length()];
        for (int i = 0; i < mToolRes.length; i++)
            mToolRes[i] = ar.getResourceId(i, 0);
        ar.recycle();
        mToolNames = getResources().getStringArray(R.array.fragment_automatic_tool_string);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        mToolContentRecycler.setLayoutManager(linearLayoutManager);
        mAdapter = new AutomaticEditAdapter();
        mToolContentRecycler.setAdapter(mAdapter);

        mToolRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mToolRecycler.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView mToolName = new TextView(getActivity());
                mToolName.setGravity(Gravity.CENTER);
                mToolName.setPadding(6, 48, 6, 24);
                mToolName.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new Holder(mToolName);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                ((TextView) holder.itemView).setText(mToolNames[position]);
                Drawable drawable = getResources().getDrawable(mToolRes[position]);
                drawable.setBounds(0, 0, 72, 72);
                ((TextView) holder.itemView).setPadding(6, 24, 6, 24);
                ((TextView) holder.itemView).setCompoundDrawables(
                        null, drawable, null, null);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onToolClick(position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mToolNames.length;
            }
        });

        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onDown();
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void onToolClick(int position) {
        switch (position) {
            case 0:
                mCheckBeanList.clear();
                mCheckBeanList.addAll(mEditBeanList);
                mAdapter.notifyDataSetChanged();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                listener.onDeleteAll(mCheckBeanList);
                mEditBeanList.removeAll(mCheckBeanList);
                mCheckBeanList.clear();
                if (mEditBeanList.size() == 0) {
                    onDown();
                }
                mAdapter.notifyDataSetChanged();
                break;
            case 4:
                onDown();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1 && data != null) {
            mEditBeanList.add((EditBean) data.getParcelableExtra(HtmlEditActivity.HTML_EDIT_DATA_KEY));
        } else if (requestCode == 2) {
            ArrayList d = IntentBean.getInstance().getChecks();
            mEditBeanList.addAll(d);
            IntentBean.getInstance().getChecks().clear();
        } else if (requestCode == 3 && data != null) {
            EditBean editBean = (EditBean) data.getParcelableExtra(HtmlEditActivity.HTML_EDIT_DATA_KEY);
            int position = data.getIntExtra(HtmlEditActivity.HTML_EDIT_POSITION_KEY, -1);
            if (position != -1) {
                mEditBeanList.remove(position);
                mEditBeanList.add(position, editBean);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSubject(Intent intent) {
        String text=mEditText.getText().toString();
        if (!text.equals(" ")&&!text.equals("")||mEditBeanList.size()>0){
            // mEditBeanList and Text
            intent.putExtra(EditActivity.ACTIVITY_EDIT_TYPE, EditActivity.ACTIVITY_EDIT_TYPE_AUTOMATIC);
            intent.putExtra(EditActivity.ACTIVITY_EDIT_TEXT, mEditText.getText().toString());
            IntentBean.getInstance().setData(mEditBeanList);
            return true;
        }else
            return false;
    }

    class AutomaticEditAdapter extends RecyclerView.Adapter {

        private LayoutInflater mInflater;
        private boolean mFlag = false;

        public AutomaticEditAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder mViewHolder =
                    new AutomaticViewHolder(mInflater.inflate(R.layout.view_item_automatic, parent, false));
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            AutomaticViewHolder automaticViewHolder = (AutomaticViewHolder) holder;
            final View itemView = automaticViewHolder.itemView;
            final EditBean editBean = mEditBeanList.get(position);
            if (editBean.getType() == ContentProviderUtils.TYPE_PHOTO) {
                setImageBitmap(editBean.getPath(), itemView, automaticViewHolder.getAutomaticImage());
            } else if (editBean.getType() == ContentProviderUtils.TYPE_AUDIO) {
                if (editBean.getBackgroundPath() != null)
                    setImageBitmap(editBean.getBackgroundPath(), itemView, automaticViewHolder.getAutomaticImage());
                else {
                    setProportion(null, itemView);
                    automaticViewHolder.getAutomaticType().setImageDrawable(getResources().getDrawable(R.drawable.ic_music_white));
                }
            } else if (editBean.getType() == ContentProviderUtils.TYPE_VIDEO) {
                if (editBean.getBackgroundPath() != null)
                    setImageBitmap(editBean.getBackgroundPath(), itemView, automaticViewHolder.getAutomaticImage());
                else {
                    setProportion(null, itemView);
                    automaticViewHolder.getAutomaticType().setImageDrawable(getResources().getDrawable(R.drawable.ic_video_white));
                }
            }
            automaticViewHolder.setChecked(mCheckBeanList.contains(editBean));
            automaticViewHolder.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !mCheckBeanList.contains(editBean)) {
                        mCheckBeanList.add(editBean);
                    } else if (!isChecked) {
                        if (mCheckBeanList.contains(editBean)) {
                            mCheckBeanList.remove(editBean);
                        }
                    }
                }
            });
            automaticViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mFlag != true) {
                        notifyDataSetChanged();
                        onUp();
                    }
                    return true;
                }
            });
            if (mFlag) {
                automaticViewHolder.showToolLayout();
                if (editBean.getType() == ContentProviderUtils.TYPE_PHOTO)
                    automaticViewHolder.getAutomaticType().setVisibility(View.GONE);
            } else {
                automaticViewHolder.dissToolLayout();
            }
        }

        @Override
        public int getItemCount() {
            return mEditBeanList.size();
        }

        private void setImageBitmap(String BitmapUrl, final View fatherView, ImageView bitmapView) {
            setProportion(BitmapUrl, fatherView);
            Glide.with(AutomaticFragment.this).load(BitmapUrl).centerCrop().into(bitmapView);
        }

        /**
         * 设置View比例
         *
         * @param BitmapUrl
         * @param fatherView
         */
        private void setProportion(String BitmapUrl, final View fatherView) {
            if (fatherView != null) {
                final float size = BitmapUtil.getImageSizeAhead(BitmapUrl);
                fatherView.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams = fatherView.getLayoutParams();
                        int width = (int) (fatherView.getHeight() * size);
                        if (width < 240)
                            width = 240;
                        layoutParams.width = width;
                        fatherView.setLayoutParams(layoutParams);
                    }
                });
            }
        }
    }

    class AutomaticViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.view_item_automatic_image)
        ImageView mAutomaticImage;
        @BindView(R2.id.view_item_automatic_type)
        ImageView mAutomaticType;
        @BindView(R2.id.view_item_automatic_tool_layout)
        RelativeLayout mToolLayout;
        @BindView(R2.id.view_item_automatic_check)
        CheckBox mAutomaticCheck;

        public AutomaticViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getAutomaticImage() {
            return mAutomaticImage;
        }

        public ImageView getAutomaticType() {
            return mAutomaticType;
        }

        public void showToolLayout() {
            mToolLayout.setVisibility(View.VISIBLE);
            mAutomaticType.setVisibility(View.VISIBLE);
        }

        public void dissToolLayout() {
            mToolLayout.setVisibility(View.GONE);
        }

        public void setCheckListener(CompoundButton.OnCheckedChangeListener listener) {
            mAutomaticCheck.setOnCheckedChangeListener(listener);
        }

        public void setChecked(boolean isCheck) {
            mAutomaticCheck.setChecked(isCheck);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }

    }

    public void onDown() {
        if (!mAdapter.mFlag) {
            return;
        }
        ValueAnimator scaleY = ValueAnimator.ofInt(height, 0); ////第二个高度 需要注意一下, 因为view默认是GONE  无法直接获取高度
        scaleY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatorValue = Integer.valueOf(animation.getAnimatedValue() + "");
                mToolRecycler.getLayoutParams().height = animatorValue;
                mToolRecycler.requestLayout();
            }
        });
        scaleY.setDuration(1000);
        scaleY.start();
        mAdapter.mFlag = false;
    }

    public void onUp() {
        if (mAdapter.mFlag) {
            return;
        }
        ValueAnimator scaleY = ValueAnimator.ofInt(0, height); ////第二个高度 需要注意一下, 因为view默认是GONE  无法直接获取高度
        scaleY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatorValue = Integer.valueOf(animation.getAnimatedValue() + "");
                mToolRecycler.getLayoutParams().height = animatorValue;
                mToolRecycler.requestLayout();
            }
        });
        scaleY.setDuration(1000);
        scaleY.start();
        mAdapter.mFlag = true;
    }
}
