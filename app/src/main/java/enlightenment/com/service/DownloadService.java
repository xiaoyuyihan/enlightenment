package enlightenment.com.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.base.LoginActivity;
import enlightenment.com.contents.Constants;
import enlightenment.com.contents.FileUrls;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.module.ModuleBean;
import enlightenment.com.tool.File.FileUtils;
import enlightenment.com.tool.gson.TransformationUtils;
import enlightenment.com.tool.okhttp.ModelUtil;
import enlightenment.com.tool.gson.GsonUtils;
import okhttp3.Call;

/**
 * Created by lw on 2017/8/17.
 * 检测模块是否存在或更新
 */

public class DownloadService extends Service {
    public static final String SERVICE_DATA_EXTRA = "MessageService_Extra_Type";

    public static final String SERVICE_REQUEST_TOKEN_PHONE="SERVICE_REQUEST_TOKEN_PHONE";
    public static final String SERVICE_REQUEST_TOKEN_PASSWORD="SERVICE_REQUEST_TOKEN_PASSWORD";

    public static final int ACTION_NO = -1;
    public static final int ACTION_DETECT_MODULE_NEW = 1;       //模块更新检测
    public static final int ACTION_DETECT_REQUEST_TOKEN=2;      //请求token


    private DownloadBinder messageBinder = new DownloadBinder(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * @param intent  Intent对象，
     * @param flags   flags代表flags表示启动服务的方式：, START_FLAG_REDELIVERY, or START_FLAG_RETRY.
     *                START_FLAG_REDELIVERY：你实现onStartCommand()来安排异步工作或者在另一个线程中工作，
     *                需要使用START_FLAG_REDELIVERY来 让系统重新发送一个intent。
     *                这样如果你的服务在处理它的时候被Kill掉, Intent不会丢失.
     *                START_FLAG_RETRY：表示服务之前被设为START_STICKY，则会被传入这个标记。
     * @param startId 对这个service请求的activity或者其他实体的编号。
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int status = super.onStartCommand(intent, flags, startId);
        switchService(intent);
        return status;
    }

    private void switchService(Intent intent) {
        int action = intent.getIntExtra(DownloadService.SERVICE_DATA_EXTRA, DownloadService.ACTION_NO);
        switch (action) {
            case DownloadService.ACTION_DETECT_MODULE_NEW:
                extractMajor();
                extractOrientation();
                break;
            case ACTION_DETECT_REQUEST_TOKEN:
                String phone=intent.getStringExtra(DownloadService.SERVICE_REQUEST_TOKEN_PHONE);
                String password=intent.getStringExtra(DownloadService.SERVICE_REQUEST_TOKEN_PASSWORD);
                requestToken(phone,password);
                break;
        }
    }

    private void extractOrientation() {
        if (FileUtils.isFile(FileUrls.PATH_APP_ORIENTATION)) {
            //检测更新状态
            List<ModuleBean> list = FileUtils.readFileObject(FileUrls.PATH_APP_ORIENTATION);
            if (list == null)
                list = new ArrayList<>();
            EnlightenmentApplication.getInstance().setOrientationBeen(list);
        } else
            downOrientation();
    }

    private void downOrientation() {
        ModelUtil.getInstance().get(HttpUrls.Http_URL_DETECT_ORIENT,
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String result, int id) {
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                EnlightenmentApplication.getInstance().setOrientationBeen(
                                        GsonUtils.parseJsonArrayWithGson(jsonArray.toString(), ModuleBean[].class));
                                FileUtils.writeFileObject(FileUrls.PATH_APP_ORIENTATION,
                                        EnlightenmentApplication.getInstance().getOrientationBeen());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void requestToken(String phone,String password){
        ModelUtil.getInstance().post(HttpUrls.HTTP_URL_LOGIN,
                TransformationUtils.beanToMap(
                        new LoginActivity.LoginBean(phone,password)),
                new ModelUtil.CallBack() {
                    @Override
                    public void onException(Call call, Exception e, int id) {
                        super.onException(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject data = new JSONObject(response);
                                if (data.getBoolean("Flag")) {
                                    JSONObject MSG=data.getJSONObject("data");
                                    EnlightenmentApplication.getInstance().setStringShared(
                                            Constants.Set.SET_USER_TOKEN,
                                            MSG.getString("token"));
                                    EnlightenmentApplication.getInstance().setStringShared(
                                            Constants.Set.SET_USER_TOKEN_TIME,
                                            MSG.getString("time"));
                                    EnlightenmentApplication.getInstance().setStringShared(
                                            Constants.Set.SET_USER_TOKEN_LONG,
                                            MSG.getString("cycle"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class DownloadBinder extends Binder {
        private DownloadService messageService;

        public DownloadBinder(DownloadService service) {
            this.messageService = service;
        }

        public DownloadService getMessageService() {
            return messageService;
        }
    }

    private void extractMajor() {
        if (FileUtils.isFile(FileUrls.PATH_APP_MAJOR)) {
            //检测更新状态
            List<ModuleBean> list = FileUtils.readFileObject(FileUrls.PATH_APP_MAJOR);
            if (list == null) {
                list = new ArrayList<>();
                downModule();
            }
            EnlightenmentApplication.getInstance().setMajorBeen(list);
        } else
            downModule();
    }

    private void downModule() {
        ModelUtil.getInstance().get(HttpUrls.Http_URL_DETECT_MAJOR,
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String result, int id) {
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                EnlightenmentApplication.getInstance().setMajorBeen(
                                        GsonUtils.parseJsonArrayWithGson(jsonArray.toString(), ModuleBean[].class));
                                FileUtils.writeFileObject(FileUrls.PATH_APP_MAJOR,
                                        EnlightenmentApplication.getInstance().getMajorBeen());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
