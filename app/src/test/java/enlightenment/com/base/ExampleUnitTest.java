package enlightenment.com.base;

import org.junit.Test;

import enlightenment.com.contents.HttpUrls;
import enlightenment.com.tool.okhttp.ModelUtil;

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
        ModelUtil.getInstance().get(HttpUrls.Http_URL_DETECT_MAJOR,
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