package enlightenment.com.service;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.util.ArrayMap;

import com.edit.bean.EditBean;
import com.provider.utils.IntentBean;
import com.utils.TypeConverUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import enlightenment.com.base.R;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.operationBean.InformationBean;

import com.edit.bean.WebContentBean;

import enlightenment.com.tool.HTMLTemplate;
import enlightenment.com.tool.Imagecompression.ImageCompression;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.tool.gson.TransformationUtils;
import enlightenment.com.tool.okhttp.ModelUtil;
import enlightenment.com.tool.okhttp.callback.StringCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lw on 2018/3/2.
 */

public class UploadService extends AppService {

    private static String UP_LOAD_INFORMATION_KEY = "UP_LOAD_INFORMATION_KEY";       //提交数据的信息
    private static String UP_LOAD_RESOURCES_KEY = "UP_LOAD_RESOURCES_KEY";         //提交数据的资源

    private static ArrayList<Map> mUploadData = new ArrayList<>();

    private UploadBinder uploadBinder = new UploadBinder(this);



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return uploadBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void addUploadData(InformationBean informationBean) {
        Map<String, Object> map = new ArrayMap<>();
        map.put(UP_LOAD_INFORMATION_KEY, informationBean);
        map.put(UP_LOAD_RESOURCES_KEY, IntentBean.getInstance().getData());
        mUploadData.add(map);
    }

    public void startUpload(InformationBean informationBean) {
        addUploadData(informationBean);
        showNotification(R.mipmap.logo,
                "开始提交(" + mUploadData.size() + ")",
                "提交中···", NOTIFICATION_ACTION_SUBJECT);
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                upload();
            }
        });
    }

    private void upload() {
        for (Map bean : mUploadData) {
            InformationBean informationBean = (InformationBean) bean.get(UP_LOAD_INFORMATION_KEY);
            ArrayList<EditBean> beanArrayList = (ArrayList) bean.get(UP_LOAD_RESOURCES_KEY);
            if (informationBean.getContent().equals("")) {
                //H5
                onSaveEditBean(beanArrayList, informationBean.getToken());
                onSubject(bean, informationBean, beanArrayList, false);
            } else {
                //普通
                onSubject(bean, informationBean, beanArrayList);
            }
        }

    }

    private void onSubject(final Map map, final InformationBean informationBean, final ArrayList<EditBean> beanArrayList, boolean falg) {
        informationBean.setContent(createJsonH5(beanArrayList));
        ModelUtil.getInstance()
                .postFileProgress(HttpUrls.HTTP_URL_UPLOAD_CONTENT,
                        TransformationUtils.beanToMap(informationBean), new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id, int code) {
                                super.onError(call, e, id, code);
                                onSubject(map, informationBean, beanArrayList, false);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("Flag")) {
                                        mUploadData.remove(map);
                                        mMainHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkNotification();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onSubject(map, informationBean, beanArrayList);
                                }
                            }
                        });
    }

    private String createH5(ArrayList<EditBean> beanArrayList) {
        String content = "";
        for (EditBean bean : beanArrayList) {
            switch (bean.getType()) {
                case EditBean.TYPE_AUDIO:
                    content += HTMLTemplate.getHTML_Template_audio_1(bean.getHttpPath(),
                            TypeConverUtil.TimeMSToMin(bean.getTime()));
                    break;
                case EditBean.TYPE_PHOTO:
                    content += HTMLTemplate.getHTML_Template_photo_1(bean.getHttpPath());
                    break;
                case EditBean.TYPE_TEXT:
                    content += HTMLTemplate.getHTML_Template_text_1(bean.getHTML5());
                    break;
                case EditBean.TYPE_VIDEO:
                    break;
            }
        }
        return HTMLTemplate.HTML_TEMPLATE_CSS + content + HTMLTemplate.HTML_TEMPLATE_SCRIPT;
    }

    private String createJsonH5(ArrayList<EditBean> beanArrayList) {
        ArrayList<WebContentBean> content = new ArrayList<>();
        for (EditBean bean : beanArrayList) {
            switch (bean.getType()) {
                case EditBean.TYPE_AUDIO:
                    content.add(new WebContentBean(bean.getProviderName(), bean.getHttpPath(),
                            EditBean.TYPE_AUDIO, 0, Integer.valueOf(bean.getTime())));
                    break;
                case EditBean.TYPE_PHOTO:
                    content.add(new WebContentBean(bean.getProviderName(), bean.getHttpPath(),
                            EditBean.TYPE_PHOTO, 0, Integer.valueOf(bean.getTime())));
                    break;
                case EditBean.TYPE_TEXT:
                    content.add(new WebContentBean(bean.getProviderName(), bean.getHTML5(),
                            EditBean.TYPE_TEXT, 0, Integer.valueOf(bean.getTime())));
                    break;
                case EditBean.TYPE_VIDEO:
                    content.add(new WebContentBean(bean.getProviderName(), bean.getHttpPath(),
                            EditBean.TYPE_VIDEO, 0, Integer.valueOf(bean.getTime())));
                    break;
            }
        }
        return GsonUtils.parseBeanToJson(content);
    }

    /**
     * 根据 类型加载不同的js和css
     *
     * @param beanArrayList
     * @return
     */
    private String createTypeH5(ArrayList<EditBean> beanArrayList) {
        String content = "";
        Map<String, String> includeMap = new ArrayMap<>();
        for (EditBean bean : beanArrayList) {
            switch (bean.getType()) {
                case EditBean.TYPE_AUDIO:
                    if (!includeMap.containsKey(String.valueOf(EditBean.TYPE_AUDIO)))
                        includeMap.put(String.valueOf(EditBean.TYPE_AUDIO), HTMLTemplate.HTML_TEMPLATE_AUDIO_1);
                    content += HTMLTemplate.getHTML_Template_audio_1(bean.getHttpPath(),
                            TypeConverUtil.TimeMSToMin(bean.getTime()));
                    break;
                case EditBean.TYPE_PHOTO:
                    if (!includeMap.containsKey(String.valueOf(EditBean.TYPE_PHOTO)))
                        includeMap.put(String.valueOf(EditBean.TYPE_AUDIO), HTMLTemplate.HTML_TEMPLATE_PHOTO_1);
                    content += HTMLTemplate.getHTML_Template_photo_1(bean.getHttpPath());
                    break;
                case EditBean.TYPE_TEXT:
                    if (!includeMap.containsKey(String.valueOf(EditBean.TYPE_TEXT)))
                        includeMap.put(String.valueOf(EditBean.TYPE_AUDIO), HTMLTemplate.HTML_TEMPLATE_TEXT_1);
                    content += HTMLTemplate.getHTML_Template_text_1(bean.getHTML5());
                    break;
                case EditBean.TYPE_VIDEO:
                    break;
            }
        }
        return addContentInclude(content, includeMap);
    }

    /***
     * 讲不通的js和css 加入
     * @param content
     * @param includeMap
     * @return
     */
    private String addContentInclude(String content, Map<String, String> includeMap) {
        String Html = "<div>";
        Iterator<String> includes = includeMap.keySet().iterator();
        while (includes.hasNext()) {
            Html += includeMap.get(includes.next());
        }
        return Html + content + "</div>";
    }

    /**
     * @param beanArrayList
     * @param token
     */
    private void onSaveEditBean(final ArrayList<EditBean> beanArrayList, final String token) {
        synchronized (this) {
            for (final EditBean bean : beanArrayList) {
                if (bean.getType() != EditBean.TYPE_TEXT && bean.getPath() != null &&
                        !bean.getPath().equals("")) {
                    ImageCompression.getInstance()
                            .compressionImage(bean.getPath(),
                                    new ImageCompression.OnCompressionListener() {
                                        @Override
                                        public void onCompression(String oldUrl, String url) {
                                            bean.setPath(url);
                                            bean.setHttpPath(onPutService(bean, token));
                                        }
                                    });
                }
            }
        }
    }

    private String onPutService(EditBean bean, String token) {
        Response response;
        try {
            response = ModelUtil.getInstance().postSynchFile(
                    getTypeURL(bean.getType()),
                    "file", new File(bean.getPath()), token
            );
            String data = response.body().string();
            return new JSONObject(data).getString("data");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTypeURL(int type) {
        switch (type) {
            case EditBean.TYPE_PHOTO:
                return HttpUrls.HTTP_URL_SAVE_PHOTO;
            case EditBean.TYPE_AUDIO:
                return HttpUrls.HTTP_URL_SAVE_AUDIO;
            case EditBean.TYPE_VIDEO:
                return HttpUrls.HTTP_URL_SAVE_VIDEO;

        }
        return "";
    }

    private void onSubject(final Map map, final InformationBean informationBean, final ArrayList<EditBean> beanArrayList) {
        ModelUtil.getInstance()
                .postFileProgress(HttpUrls.HTTP_URL_UPLOAD_CONTENT,
                        getAutomaticFile(beanArrayList),
                        TransformationUtils.beanToMap(informationBean), new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id, int code) {
                                super.onError(call, e, id, code);
                                onSubject(map, informationBean, beanArrayList);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("Flag")) {
                                        mUploadData.remove(map);
                                        mMainHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkNotification();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onSubject(map, informationBean, beanArrayList);
                                }
                            }
                        });
    }

    private void checkNotification() {
        if (mUploadData.size() <= 0) {
            deleteNotification(NOTIFICATION_ACTION_SUBJECT);
            Intent iUpload = new Intent(this, UploadService.class);
            stopService(iUpload);
        } else
            showNotification(R.mipmap.logo,
                    "开始提交(" + mUploadData.size() + ")",
                    "提交中···", NOTIFICATION_ACTION_SUBJECT);
    }

    private Map<String, ArrayList<File>> getAutomaticFile(ArrayList<EditBean> beens) {
        Map<String, ArrayList<File>> map = new ArrayMap<>();
        for (EditBean editBean : beens) {
            if (editBean.getType() == EditBean.TYPE_PHOTO) {
                addMapTypeList(map, "photo", editBean);
            } else if (editBean.getType() == EditBean.TYPE_AUDIO) {
                addMapTypeList(map, "audio", editBean);
            } else if (editBean.getType() == EditBean.TYPE_VIDEO)
                addMapTypeList(map, "video", editBean);
        }
        return map;
    }

    private void addMapTypeList(Map<String, ArrayList<File>> map, String key, EditBean editBean) {
        if (map.containsKey(key)) {
            map.get(key).add(new File(editBean.getPath()));
        } else {
            ArrayList<File> list = new ArrayList<>();
            list.add(new File(editBean.getPath()));
            map.put(key, list);
        }
    }

    public class UploadBinder extends Binder {
        private UploadService messageService;

        public UploadBinder(UploadService service) {
            this.messageService = service;
        }

        public void addUploadData(InformationBean informationBean) {
            messageService.startUpload(informationBean);
        }
    }
}
