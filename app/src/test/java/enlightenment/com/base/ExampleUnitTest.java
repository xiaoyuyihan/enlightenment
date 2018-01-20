package enlightenment.com.base;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import enlightenment.com.contents.Constants;
import enlightenment.com.contents.HttpUrls;
import enlightenment.com.main.MainActivity;
import enlightenment.com.tool.ModelUtil;
import enlightenment.com.tool.gson.TransformationUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.print(1);
    }

    @Test
    public void TestModelUtil(){
        ModelUtil.getInstance().get(HttpUrls.Http_URL_DETECT_MODULE,
                new ModelUtil.CallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            System.out.print(response);
                        }
                    }
                });
    }
}