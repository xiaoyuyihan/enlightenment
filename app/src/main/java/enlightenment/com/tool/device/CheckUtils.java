package enlightenment.com.tool.device;

import android.text.TextUtils;

import java.util.List;

import enlightenment.com.base.EnlightenmentApplication;
import enlightenment.com.main.ContentBean;
import enlightenment.com.module.ModuleChildBean;
import enlightenment.com.module.ModuleFatherBean;

/**
 * Created by lw on 2018/2/10.
 */

public class CheckUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        }else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    public static String getModelName(ContentBean contentBean){
        List<ModuleFatherBean> list;
        if (Integer.valueOf(contentBean.getType())==0){
            list= EnlightenmentApplication.getInstance().getMajorBeen();
        }else {
            list= EnlightenmentApplication.getInstance().getMajorBeen();
        }
        for(ModuleFatherBean fatherBean:list){
            if (contentBean.getFatherID()-fatherBean.getIdentity()*100<100){
                for (ModuleChildBean childBean:fatherBean.getChildBeen()){
                    if(childBean.getIdentity()==contentBean.getFatherID())
                        return childBean.getName();
                }
            }
        }
        return "";
    }
}
