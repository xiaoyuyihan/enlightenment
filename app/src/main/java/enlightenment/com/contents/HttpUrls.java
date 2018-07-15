package enlightenment.com.contents;

/**
 * Created by lw on 2017/7/21.
 */

public class HttpUrls {
    public static final String HTTP_URL="http://101.37.151.91:5000/api/";
    //public static final String HTTP_URL="http://192.168.1.106:5000/api/";
    public static final String HTTP_URL_LOGIN=HTTP_URL+"login";
    public static final String HTTP_URL_SEND_PHONE=HTTP_URL+"getVerification";
    public static final String HTTP_URL_REGISTERED=HTTP_URL+"registered";
    public static final String Http_URL_DETECT_MAJOR=HTTP_URL+"getMajor";                  //学习的模块
    public static final String Http_URL_DETECT_ORIENT=HTTP_URL+"getOrientation";            //DIY的模块
    public static final String HTTP_URL_CHANGE_PASSWORD=HTTP_URL+"changePassword";
    public static final String HTTP_UTL_GET_USER_COLUMN=HTTP_URL+"getUserColumn";

    /********************************Main***********************************/
    public static final String HTTP_URL_HOME_NEW=HTTP_URL+"newContent";
    public static final String HTTP_URL_HOME_HOT=HTTP_URL+"hotContent";
    public static final String HTTP_URL_HOME_LOVE=HTTP_URL+"recommended";

    public static final String HTTP_URL_FOUND_KONWLEDGE=HTTP_URL+"obtainContent";
    public static final String HTTP_URL_FOUND_DIY=HTTP_URL+"obtainContent";
    public static final String HTTP_URL_FOUND_HELP=HTTP_URL+"obtainHelp";
    public static final String HTTP_URL_FOUND_MYSELF=HTTP_URL+"obtainMyself";

    /*******************************Information****************************/
    public static final String HTTP_URL_NEWS_COLUMN=HTTP_URL+"setColumn";
    public static final String HTTP_URL_NEWS_CHILD_COLUMN=HTTP_URL+"setChildColumn";
    public static final String HTTP_URL_UPLOAD_CONTENT=HTTP_URL+"setColumnContent";
    public static final String HTTP_URL_SAVE_PHOTO=HTTP_URL+"savePhotoFile";
    public static final String HTTP_URL_SAVE_AUDIO=HTTP_URL+"saveAudioFile";
    public static final String HTTP_URL_SAVE_VIDEO=HTTP_URL+"saveVideoFile";

    /*******************************Details****************************/
    public static final String HTTP_URL_GET_COMMENTS=HTTP_URL+"getComments";
    public static final String HTTP_URL_SET_COMMENTS=HTTP_URL+"setComments";
    public static final String HTTP_URL_UPDATE_USER_ATTENTION=HTTP_URL+"updateUserAttention";
    public static final String HTTP_URL_UPDATE_USER_LIKE=HTTP_URL+"updateUserLive";
}
