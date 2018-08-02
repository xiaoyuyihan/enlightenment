package enlightenment.com.tool.device;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

;

/**
 * Created by admin on 2018/7/30.
 */

public class AliyunOssUtils {
    private static AliyunOssUtils aliyunOssUtils;

    public static AliyunOssUtils getInstance(Context context) {
        if (aliyunOssUtils == null)
            aliyunOssUtils = new AliyunOssUtils(context);
        return aliyunOssUtils;
    }

    // 推荐使用OSSAuthCredentialsProvider，token过期后会自动刷新。
    private String stsServer = "http://192.168.0.4:5000";
    private String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    private OSSCredentialProvider credentialProvider;
    private ClientConfiguration conf;
    private OSS oss;

    private String bucketName = "enlightenment";
    private static final String AUDIO_KEY = "audio/";
    private static final String VIDEO_KEY = "video/";
    private static final String PICTURE_KEY = "picture/";
    private static final String PORTRAIT_KEY = "portrait/";
    private static final String ERROR_LOG_KEY = "log/errorLog/";
    private static final String USER_LOG_KEY = "log/userLog/";

    public static final String AUDIO_SUFFIX = "mp3,wma,wav,ogg,ra,mid";
    public static final String VIDIO_SUFFIX = "avi,rmvb,asf,mpg,mpeg,wmv,mp4,mkv";
    public static final String PHOTO_SUFFIX = "bmp,jpg,jpeg,png,gif";

    private static final String PHONE_TYPE = "android_";

    private boolean isWaitFinished = false;
    private HashMap putObjectMap = new HashMap();

    private AliyunOssUtils(Context context) {
        credentialProvider = new STSGetter(stsServer);
        //config
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时时间，默认15秒
        conf.setSocketTimeout(15 * 1000); // Socket超时时间，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(context.getApplicationContext(), endpoint, credentialProvider,conf);
        OSSLog.enableLog();
    }

    /**
     * 同步提交文件
     *
     * @param username
     * @param uploadFilePath
     */
    public void putSynchroObject(String username, String uploadFilePath) {
        String[] fileName = uploadFilePath.split("\\.");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length-1];
            String ObjectFileName = UUID.nameUUIDFromBytes(username.getBytes()).toString();
            ObjectFileName = ObjectFileName.replace("-", "") + "." + suffix;
            if (AUDIO_SUFFIX.contains(suffix)) {
                putSynchroAudio(ObjectFileName, uploadFilePath);
            } else if (VIDIO_SUFFIX.contains(suffix)) {
                putSynchroVideo(ObjectFileName, uploadFilePath);
            } else if (PHOTO_SUFFIX.contains(suffix)) {
                putSynchroPicture(ObjectFileName, uploadFilePath);
            } else {

            }
        }
    }

    /**
     * 异步提交文件
     *
     * @param username
     * @param uploadFilePath
     */
    public void putAsyncObject(String username, String uploadFilePath, OnPutObjectAsyncCall call) {
        String[] fileName = uploadFilePath.split("\\.");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length-1];
            String ObjectFileName = UUID.nameUUIDFromBytes(username.getBytes()).toString();
            ObjectFileName = ObjectFileName.replace("-", "") + "." + suffix;
            if (AUDIO_SUFFIX.contains(suffix)) {
                putAsyncAudio(ObjectFileName, uploadFilePath, call);
            } else if (VIDIO_SUFFIX.contains(suffix)) {
                putAsyncVideo(ObjectFileName, uploadFilePath, call);
            } else if (PHOTO_SUFFIX.contains(suffix)) {
                putAsyncPicture(ObjectFileName, uploadFilePath, call);
            } else {

            }
        }
    }

    /**
     * 同步提交头像
     *
     * @param username
     * @param uploadFilePath
     */
    public void putSynchroPortrait(String username, String uploadFilePath) {
        String[] fileName = uploadFilePath.split("/");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length];
            String ObjectFileName = UUID.nameUUIDFromBytes(username.getBytes()).toString();
            ObjectFileName = ObjectFileName.replace("-", "") + "." + suffix;
            if (PHOTO_SUFFIX.contains(suffix)) {
                submitSynchroObject(PORTRAIT_KEY + ObjectFileName, uploadFilePath);
            } else {

            }
        }
    }

    /**
     * 异步提交头像
     *
     * @param username
     * @param uploadFilePath
     */
    public void putAsyncPortrait(String username, String uploadFilePath, OnPutObjectAsyncCall call) {
        String[] fileName = uploadFilePath.split("/");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length];
            String ObjectFileName = UUID.nameUUIDFromBytes(username.getBytes()).toString();
            ObjectFileName = ObjectFileName.replace("-", "") + "." + suffix;
            if (PHOTO_SUFFIX.contains(suffix)) {
                submitAsyncObject(PORTRAIT_KEY + ObjectFileName, uploadFilePath, call);
            } else {

            }
        }
    }


    public void putSynchroErrorLog(String username, String uploadFilePath) {
        String[] fileName = uploadFilePath.split("/");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length];
            String ObjectFileName = ERROR_LOG_KEY + PHONE_TYPE + username + "_" + System.currentTimeMillis() + "." + suffix;
            submitSynchroObject(ObjectFileName, uploadFilePath);
        }
    }

    public void putSynchroUserLog(String username, String uploadFilePath) {
        String[] fileName = uploadFilePath.split("/");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length];
            String ObjectFileName = USER_LOG_KEY + PHONE_TYPE + username + "_" + System.currentTimeMillis() + "." + suffix;
            submitSynchroObject(ObjectFileName, uploadFilePath);
        }
    }

    public void putAsyncErrorLog(String username, String uploadFilePath, OnPutObjectAsyncCall call) {
        String[] fileName = uploadFilePath.split("/");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length];
            String ObjectFileName = ERROR_LOG_KEY + PHONE_TYPE + username + "_" + System.currentTimeMillis() + "." + suffix;
            submitAsyncObject(ObjectFileName, uploadFilePath, call);
        }
    }

    public void putAsyncUserLog(String username, String uploadFilePath, OnPutObjectAsyncCall call) {
        String[] fileName = uploadFilePath.split("/");
        if (fileName.length > 1) {
            String suffix = fileName[fileName.length];
            String ObjectFileName = USER_LOG_KEY + PHONE_TYPE + username + "_" + System.currentTimeMillis() + "." + suffix;
            submitAsyncObject(ObjectFileName, uploadFilePath, call);
        }
    }

    private void putSynchroAudio(String fileName, String uploadFilePath) {
        submitSynchroObject(AUDIO_KEY + fileName, uploadFilePath);
    }

    private void putSynchroVideo(String fileName, String uploadFilePath) {
        submitSynchroObject(VIDEO_KEY + fileName, uploadFilePath);
    }

    private void putSynchroPicture(String fileName, String uploadFilePath) {
        submitSynchroObject(PICTURE_KEY + fileName, uploadFilePath);
    }

    private void putAsyncAudio(String fileName, String uploadFilePath, OnPutObjectAsyncCall call) {
        submitAsyncObject(AUDIO_KEY + fileName, uploadFilePath, call);
    }

    private void putAsyncVideo(String fileName, String uploadFilePath, OnPutObjectAsyncCall call) {
        submitAsyncObject(VIDEO_KEY + fileName, uploadFilePath, call);
    }

    private void putAsyncPicture(String fileName, String uploadFilePath, OnPutObjectAsyncCall call) {
        submitAsyncObject(PICTURE_KEY + fileName, uploadFilePath, call);
    }


    private void submitSynchroObject(String objectKey, String uploadFilePath) {
        File file = new File(uploadFilePath);
        if (null == file || !file.exists()) {
            // 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
            return;
        }
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);
        /*// 文件元信息的设置是可选的
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream"); // 设置content-type
        try {
            metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
        } catch (IOException e) {
            e.printStackTrace();
        }
        put.setMetadata(metadata);*/
        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    private void submitAsyncObject(String objectKey, final String uploadFilePath, final OnPutObjectAsyncCall onPutObjectAsyncCall) {
        if (objectKey.equals("")) {
            Log.w("AsyncPutImage", "ObjectNull");
            return;
        }
        File file = new File(uploadFilePath);
        if (!file.exists()) {
            Log.w("AsyncPutImage", "FileNotExist");
            Log.w("LocalFile", uploadFilePath);
            return;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if (onPutObjectAsyncCall != null)
                    onPutObjectAsyncCall.onProgress((int)(100*currentSize/totalSize));
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                putObjectMap.remove(uploadFilePath);
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                if (onPutObjectAsyncCall != null)
                    onPutObjectAsyncCall.onPutObjectCall(result.getETag());
            }

            @Override
            public void onFailure(PutObjectRequest request,
                                  ClientException clientExcepion,
                                  ServiceException serviceException) {
                putObjectMap.remove(uploadFilePath);
                if (onPutObjectAsyncCall != null)
                    onPutObjectAsyncCall.onFailure(clientExcepion, uploadFilePath);
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        putObjectMap.put(uploadFilePath, task);
        if (isWaitFinished)
            task.waitUntilFinished(); // 可以等待任务完成
    }

    public boolean removePutObjectQuetion(String uploadFilePath) {
        if (putObjectMap.containsKey(uploadFilePath)) {
            putObjectMap.remove(uploadFilePath);
            return true;
        }
        return false;
    }

    /**
     * Created by Administrator on 2015/12/9 0009.
     * 重载OSSFederationCredentialProvider生成自己的获取STS的功能
     */
    public class STSGetter extends OSSFederationCredentialProvider {

        private String stsServer = " http://oss-demo.aliyuncs.com/app-server/sts.php";

        public STSGetter() {
            stsServer = "http://oss-demo.aliyuncs.com/app-server/sts.php";
        }

        public STSGetter(String stsServer) {
            this.stsServer = stsServer;
        }

        public OSSFederationToken getFederationToken() {
            // 您需要在这里实现获取一个FederationToken，并构造成OSSFederationToken对象返回
            // 如果因为某种原因获取失败，可直接返回null

            try {
                URL stsUrl = new URL("http://192.168.0.4:5000/api/updateOSSToken");
                HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
                InputStream input = conn.getInputStream();
                String jsonText = IOUtils.readStreamAsString(input, OSSConstants.DEFAULT_CHARSET_NAME);
                JSONObject jsonObjs = new JSONObject(jsonText);
                String ak = jsonObjs.getString("AccessKeyId");
                String sk = jsonObjs.getString("AccessKeySecret");
                String token = jsonObjs.getString("SecurityToken");
                String expiration = jsonObjs.getString("Expiration");
                return new OSSFederationToken(ak, sk, token, expiration);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public interface OnPutObjectAsyncCall {
        void onPutObjectCall(String tag);

        void onProgress(int progress);

        void onFailure(ClientException clientException, String uploadFilePath);
    }

}
