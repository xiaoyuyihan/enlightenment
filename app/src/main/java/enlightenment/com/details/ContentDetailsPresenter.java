package enlightenment.com.details;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.contents.HttpUrls;
import enlightenment.com.mvp.BaseView;
import enlightenment.com.operationBean.ContentBean;
import enlightenment.com.mvp.BasePresenter;
import enlightenment.com.operationBean.CommentBean;
import enlightenment.com.operationBean.CommentGetBean;
import enlightenment.com.operationBean.CommentSubjectBean;
import enlightenment.com.operationBean.FollowBean;
import enlightenment.com.operationBean.LiveBean;
import enlightenment.com.tool.gson.GsonUtils;
import enlightenment.com.tool.gson.TransformationUtils;
import enlightenment.com.tool.okhttp.ModelUtil;
import okhttp3.Call;

/**
 * Created by admin on 2018/3/23.
 */

public class ContentDetailsPresenter<T extends DetailsView> extends BasePresenter {
    private static ContentDetailsPresenter contentDetailsPresenter;
    private boolean flag = false;
    private T mView;

    public List<CommentBean> getCommentBeans() {
        return commentBeans;
    }

    private List<CommentBean> commentBeans = new ArrayList<>();

    public static ContentDetailsPresenter getInstance() {
        if (contentDetailsPresenter == null) {
            contentDetailsPresenter = new ContentDetailsPresenter();
        }
        return contentDetailsPresenter;
    }

    public ContentDetailsPresenter() {
        super();
    }

    @Override
    public void BindView(BaseView view) {
        super.BindView(view);
        mView = (T) view;
    }

    @Override
    public void unBindView(BaseView baseView) {
        super.unBindView(baseView);
        if (mView.equals(baseView))
            mView = null;
        contentDetailsPresenter = null;
    }


    public void onComment(final String comment) {
        CommentSubjectBean commentSubjectBean = CommentSubjectBean.createContentCommentBean(
                String.valueOf(getObj().getId()), comment);
        updateComment(commentSubjectBean);
    }

    public void onCommentComment(String comment, String commentID) {
        CommentSubjectBean commentSubjectBean = CommentSubjectBean.createCommentCommentBean(
                commentID, comment, String.valueOf(getObj().getId()));
        updateComment(commentSubjectBean);
    }

    private void updateComment(final CommentSubjectBean comment) {
        mModel.post(HttpUrls.HTTP_URL_SET_COMMENTS,
                TransformationUtils.beanToMap(comment),
                new ModelUtil.CallBack() {
                    @Override
                    public void onException(Call call, Exception e, int id) {
                        super.onException(call, e, id);
                        updateComment(comment);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("Flag")) {
                                    //刷新评论

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void onUpdateComment() {
        mModel.post(HttpUrls.HTTP_URL_GET_COMMENTS,
                TransformationUtils.beanToMap(new CommentGetBean(
                        String.valueOf(getObj().getId()), "1")),
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("Flag")) {
                                    commentBeans = GsonUtils.parseJsonArrayWithGson(
                                            jsonObject.getJSONArray("data").toString(),
                                            CommentBean[].class);
                                    mView.updateComment();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    //喜欢
    public void onLive() {
        LiveBean liveBean = LiveBean.createLiveBean(String.valueOf(getObj().getId()), "0");
        updateLive(liveBean);
    }

    public void onUnLive() {
        LiveBean liveBean = LiveBean.createUnLiveBean(String.valueOf(getObj().getId()), "0");
        updateLive(liveBean);
    }

    private void updateLive(LiveBean liveBean) {
        mModel.post(HttpUrls.HTTP_URL_UPDATE_USER_LIKE,
                TransformationUtils.beanToMap(liveBean),
                new ModelUtil.CallBack() {
                    @Override
                    public void onException(Call call, Exception e, int id) {
                        super.onException(call, e, id);
                        onLive();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("Flag"))
                                    onLive();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    //关注
    public void onFollow() {
        updateFollow(FollowBean.createFollowBean(getObj().getPhone(), "1", "1"));
    }

    public void onUnFollow() {
        updateFollow(FollowBean.createUnFollowBean(getObj().getPhone(), "1", "1"));
    }


    private void updateFollow(FollowBean followBean) {
        mModel.post(HttpUrls.HTTP_URL_UPDATE_USER_ATTENTION,
                TransformationUtils.beanToMap(followBean),
                new ModelUtil.CallBack() {
                    @Override
                    public void onException(Call call, Exception e, int id) {
                        super.onException(call, e, id);
                        onLive();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("Flag"))
                                    onLive();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    //打赏
    public void onReward() {

    }

    private ContentBean getObj() {
        Object data = mView.getObj();
        if (data != null) {
            return (ContentBean) data;
        }
        return null;
    }

}
