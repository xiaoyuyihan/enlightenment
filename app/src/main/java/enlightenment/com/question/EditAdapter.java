package enlightenment.com.question;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.edit.bean.EditBean;
import com.edit.custom.CustomEditFragment;
import com.edit.custom.CustomViewHolder;
import com.utils.TypeConverUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;

/**
 * Created by admin on 2018/7/1.
 */

public class EditAdapter extends RecyclerView.Adapter {
    private ArrayList<EditBean> data;
    private Context context;
    private LayoutInflater mInflater;
    public EditAdapter(Context context, ArrayList data){
        this.mInflater = LayoutInflater.from(context);
        this.data= data;
        this.context=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        switch (viewType) {
            case EditBean.TYPE_AUDIO:
                mViewHolder = new CustomViewHolder.AudioViewHolder(
                        mInflater.inflate(
                                com.webeditproject.R.layout.view_item_edit_audio, parent, false));
                break;
            case EditBean.TYPE_PHOTO:
                mViewHolder = new CustomViewHolder.PhotoViewHolder(
                        mInflater.inflate(
                                com.webeditproject.R.layout.view_item_edit_photo, parent, false),context);
                break;
            case EditBean.TYPE_TEXT:
                mViewHolder = new EditViewHolder(
                        mInflater.inflate(
                                R.layout.view_item_edit_view, parent, false));
                break;
            case EditBean.TYPE_VIDEO:
                mViewHolder = new CustomViewHolder.VideoViewHolder(
                        mInflater.inflate(
                                com.webeditproject.R.layout.view_item_edit_video, parent, false));
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final EditBean currentBean = data.get(position);
        if (holder instanceof EditViewHolder) {
            final EditViewHolder editViewHolder=(EditViewHolder)holder;
            if (currentBean.getHTML5()!=null&&!currentBean.getHTML5().equals("")){
                editViewHolder.setEditString(currentBean.getHTML5());
            }else
                editViewHolder.getEditText().setHint("请详细叙述关于你的问题详情，背景，条件");

            editViewHolder.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    currentBean.setHTML5(charSequence.toString());
                    if (charSequence.equals("")){
                        editViewHolder.getEditText().setHint("请详细叙述关于你的问题详情，背景，条件");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else if (holder instanceof CustomViewHolder.AudioViewHolder) {
            CustomViewHolder.AudioViewHolder audioViewHolder = (CustomViewHolder.AudioViewHolder) holder;
            audioViewHolder.setTopName(currentBean.getProviderName());
            audioViewHolder.setTime(TypeConverUtil.TimeMSToMin(currentBean.getTime()));
            audioViewHolder.setPlayClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof CustomEditFragment.AudioPlayClick) {
                        ((CustomEditFragment.AudioPlayClick) context).onClick(holder, currentBean.getPath());
                    }
                }
            });
        } else if (holder instanceof CustomViewHolder.VideoViewHolder) {

        } else if (holder instanceof CustomViewHolder.PhotoViewHolder) {
            CustomViewHolder.PhotoViewHolder photoViewHolder = (CustomViewHolder.PhotoViewHolder) holder;
            photoViewHolder.setPhotoView(currentBean.getPath());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }


    class EditViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.view_item_edit_view)
        EditText mEditText;

        public EditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public String getString(){
            return mEditText.getText().toString();
        }

        public void setEditString(String string){
            mEditText.setText(string);
        }

        public EditText getEditText() {
            return mEditText;
        }
    }
}
