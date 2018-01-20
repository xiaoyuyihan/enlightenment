package enlightenment.com.base;

import android.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import enlightenment.com.contents.Constants;
import enlightenment.com.contents.FileUrls;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.main.MainActivity;
import enlightenment.com.module.ModuleFatherBean;
import enlightenment.com.mvp.BasePresenter;
import enlightenment.com.mvp.BaseView;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.tool.ModelUtil;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.tool.gson.TransformationUtils;
import enlightenment.com.tool.okhttp.OkHttpUtils;
import okhttp3.Call;

/**
 * Created by lw on 2017/7/21.
 */

public class basePresenter<T extends baseView> extends BasePresenter {
    private static basePresenter basePresenter;
    private ModelUtil mModel;
    private T mView;

    public static basePresenter getInstance() {
        if (basePresenter == null) {
            basePresenter = new basePresenter();
        }
        return basePresenter;
    }

    public basePresenter() {
        mModel = ModelUtil.getInstance();
    }

    @Override
    public void BindView(BaseView view) {
        super.BindView(view);
        mView = (T) view;
    }

    @Override
    public void unBindView(BaseView view) {
        super.unBindView(view);
        if (mView.equals(view)) {
            mView = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 登录
     * @param loginBean
     * @param <T>
     */
    public <T> void executeLogin(T loginBean) {
        mModel.post(HttpUrls.HTTP_URL_LOGIN, TransformationUtils.beanToMap(loginBean),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject data = new JSONObject(response);
                                if (data.getBoolean("Flag")) {
                                    EnlightenmentApplication.getInstance().isBooleanShared(Constants.SHARED_IS_LOGIN, true);
                                    EnlightenmentApplication.getInstance().setStringShared(Constants.SHARED_PHONE, Constants.phoneCode);
                                    mView.startNextActivity(MainActivity.class);
                                } else
                                    mView.showToast(data.getString("data"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 发送手机验证码
     * @param phone
     */
    public void sendPhoneCode(String phone) {
        Map map=new ArrayMap();
        map.put("phone",phone);
        mModel.post(HttpUrls.HTTP_URL_SEND_PHONE,map,
                new ModelUtil.CallBack(){
                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getBoolean("Flag")) {
                                    Constants.phoneVerification = object.getString("verification");
                                    Constants.VerificationTimeout = object.getLong("time");
                                } else
                                    mView.showToast("验证码申请今日已达到上限");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 比较验证码
     * @param phone
     * @param VerificationCode
     */
    public void equalsCode(String phone, String VerificationCode) {
        long current = System.currentTimeMillis();
        if (!phone.equals(Constants.phoneCode)) {
            mView.showToast("手机号与获取验证码手机号不符");
            return;
        }
        if (VerificationCode.equals(Constants.phoneVerification) &&
                current - Constants.VerificationTimeout * 1000 < 60 * 1000 * 6) {
            if (mView instanceof PhoneValidationActivity) {
                if (((PhoneValidationActivity) mView).activiyType == PhoneValidationActivity.TYPE_REGISTER)
                    mView.startNextActivity(InterestActivity.class);
                    //重置密码
                else
                    mView.startNextActivity(ResetPasswordActivity.class);
            }

        } else {
            mView.showToast("验证码错误");
        }
    }

    /**
     * 提交注册用户信息
     * @param bean
     * @param <T>
     */
    public <T> void submitInfo(String photoUrl,T bean) {

        mModel.postForm(HttpUrls.HTTP_URL_REGISTERED,"avatar",new File(photoUrl),
                TransformationUtils.beanToMap(bean), new ModelUtil.CallBack() {
                    @Override
                    public void onException(Call call, Exception e, int id) {
                        e.toString();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject data = new JSONObject(response);
                                if (data.getBoolean("Flag")) {
                                    mView.startNextActivity(LoginActivity.class);
                                    mView.showToast("注册成功");
                                } else
                                    mView.showToast(data.getString("data"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 重置密码
     * @param bean
     * @param <T>
     */
    public <T> void resetPassword(T bean) {
        mModel.post(HttpUrls.HTTP_URL_CHANGE_PASSWORD, TransformationUtils.beanToMap(bean),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject data = new JSONObject(response);
                                if (data.getBoolean("Flag"))
                                    mView.startNextActivity(LoginActivity.class);
                                mView.showToast(data.getString("data"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void obtainModule(){
        mModel.get(HttpUrls.Http_URL_DETECT_MODULE,
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String result, int id) {
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                ((InterestActivity)mView).setModuleFatherBeen(
                                        GsonUtils.parseJsonArrayWithGson(jsonArray.toString(), ModuleFatherBean[].class));
                                FileUtils.writeFileObject(FileUrls.PATH_APP_MODULES,
                                        EnlightenmentApplication.getInstance().getModuleFatherBeen());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
