package enlightenment.com.information;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.R;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.tool.gson.TransformationUtils;
import enlightenment.com.tool.okhttp.ModelUtil;

/**
 * Created by lw on 2017/10/15.
 */

public class NewsColumnDialog extends DialogFragment implements View.OnClickListener{

    public static String DIALOG_MODEL_NEWS_ID="DIALOG_MODEL_NEWS_ID";
    public static String DIALOG_MODEL_NEWS_TYPE="DIALOG_MODEL_NEWS_TYPE";
    public static String DIALOG_MODEL_NEWS_CHILD="DIALOG_MODEL_NEWS_CHILD";

    private View contentView;
    private ImageView topLeftView;
    private TextView topCenterView;
    private TextView topRightView;
    private ImageView projectImageView;
    private TextInputEditText mProjectName;
    private TextInputEditText mProjectIntroduction;
    private OnClickListener onClickListener;

    private Handler mHandler=new Handler();

    private int type;
    private int modelID;
    private boolean isChild;

    public NewsColumnDialog(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_new_project,container,false);
        initView();
        modelID=getArguments().getInt(DIALOG_MODEL_NEWS_ID);
        type=getArguments().getInt(DIALOG_MODEL_NEWS_TYPE);
        isChild=getArguments().getBoolean(DIALOG_MODEL_NEWS_CHILD,false);
        return contentView;
    }

    private void initView() {
        topLeftView=(ImageView)contentView.findViewById(R.id.top_left_image);
        topLeftView.setOnClickListener(this);
        topCenterView=(TextView) contentView.findViewById(R.id.top_center_text);
        topCenterView.setText("创建新栏目");
        topRightView=(TextView) contentView.findViewById(R.id.top_right_text);
        topRightView.setOnClickListener(this);
        topRightView.setText("创建");
        projectImageView=(ImageView) contentView.findViewById(R.id.fragment_new_project_image);
        projectImageView.setOnClickListener(this);
        mProjectName=(TextInputEditText) contentView.findViewById(R.id.fragment_new_project_name);
        mProjectIntroduction=(TextInputEditText) contentView.findViewById(R.id.fragment_new_project_Introduction);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9),(int) (dm.heightPixels * 0.7));
            //点击外部不消失的方法
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_left_image:
                onClickListener.Back();
                break;
            case R.id.top_right_text:
                if (isChild){
                    subjectChildColumn();
                }else
                    subjectColumn();
                break;
        }
    }

    private NewsColumnBean onCreateBean(){
        NewsColumnBean bean=NewsColumnBean.getInstance()
                .setName(mProjectName.getText().toString())
                .setIntroduction(mProjectIntroduction.getText().toString())
                .setColumnType(String.valueOf(type))
                .setFather_id(String.valueOf(modelID));
        return bean;
    }

    private void subjectChildColumn() {
        ModelUtil.getInstance().post(HttpUrls.HTTP_URL_NEWS_CHILD_COLUMN,
                TransformationUtils.beanToMap(onCreateBean()),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
                            final ColumnBean.ColumnChildBean columnBean= GsonUtils.parseJsonWithToBean(jsonObject.toString(),ColumnBean.ColumnChildBean.class);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (onClickListener!=null)
                                        onClickListener.onNewsChildColumnCall(isChild,columnBean);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void subjectColumn() {
        ModelUtil.getInstance().post(HttpUrls.HTTP_URL_NEWS_COLUMN,
                TransformationUtils.beanToMap(onCreateBean()),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
                            final ColumnBean columnBean= GsonUtils.parseJsonWithToBean(jsonObject.toString(),ColumnBean.class);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (onClickListener!=null)
                                        onClickListener.onNewsColumnCall(isChild,columnBean);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    interface OnClickListener {
        void Back();
        void onNewsColumnCall(boolean isChild,ColumnBean columnBean);
        void onNewsChildColumnCall(boolean isChild,ColumnBean.ColumnChildBean child);
    }

    static class NewsColumnBean{

        public static NewsColumnBean getInstance() {
            return new NewsColumnBean();
        }
        private String name;
        private String introduction;
        private String columnType;
        private String father_id;
        private String token= EnlightenmentApplication.getInstance().getString(Constants.Set.SET_USER_TOKEN);

        public String getName() {
            return name;
        }

        public NewsColumnBean setName(String name) {
            this.name = name;
            return this;
        }

        public String getIntroduction() {
            return introduction;
        }

        public NewsColumnBean setIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public String getColumnType() {
            return columnType;
        }

        public NewsColumnBean setColumnType(String columnType) {
            this.columnType = columnType;
            return this;
        }

        public String getFather_id() {
            return father_id;
        }

        public NewsColumnBean setFather_id(String father_id) {
            this.father_id = father_id;
            return this;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
