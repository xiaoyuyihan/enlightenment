package enlightenment.com.tool.gson;

import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by lw on 2017/9/3.
 */

public class TransformationUtils {
    /**
     * 将对象装换为map
     * @param bean
     * @return
     */
    public static <T> Map<String, String> beanToMap(T bean) {
        Map<String, String> map = new ArrayMap<>();
        if (bean != null) {
            Field[] fields = null;
            fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String proName = field.getName();
                if (proName.contains("this")||proName.contains("$"))
                    continue;
                Object proValue = null;
                try {
                    proValue = field.get(bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (proValue!=null)
                    map.put(proName,proValue.toString() );
            }
            return map;
        }
        return map;
    }
}
