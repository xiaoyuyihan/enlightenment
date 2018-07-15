package enlightenment.com.tool;

/**
 * Created by admin on 2018/3/22.
 */

public class HTMLTemplate {

    public static String HTML_TEMPLATE_CSS =
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://101.37.151.91:5000/uploads/html/css/reset.css\" />\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://101.37.151.91:5000/uploads/html/css/index.css\" />";

    public static String HTML_TEMPLATE_SCRIPT =
            "<script src=\"http://101.37.151.91:5000/uploads/html/js/jquery-1.7.2.min.js\"></script>\n" +
            "<script src=\"http://101.37.151.91:5000/uploads/html/js/jquery-ui-1.8.17.custom.min.js\"></script>\n" +
            "<script type=\"text/javascript\" src=\"http://101.37.151.91:5000/uploads/html/js/a.js\"></script>\n" +
            "<script src=\"http://101.37.151.91:5000/uploads/html/js/index.js\"></script>";
    public static String HTML_TEMPLATE_AUDIO_1 ="";

    public static String HTML_TEMPLATE_PHOTO_1="";

    public static String HTML_TEMPLATE_TEXT_1="";


    public static String getHTML_Template_audio_1(String audioUrl,String time){
        return "<div id=\"player\">\n" +
                "  <source id=\"audio\" src=\""+audioUrl+"\">\n" +
                "  <div class=\"top\">\n" +
                "    <img src=\"http://101.37.151.91:5000/uploads/html/image/tu.png\" alt=\"\">\n" +
                "  </div>\n" +
                "  <div class=\"middle\">\n" +
                "    <div class=\"left\">\n" +
                "      <div class=\"timer\">0:00</div>\n" +
                "    </div>\n" +
                "    <div class=\"center\">\n" +
                "      <div class=\"progressB\">\n" +
                "        <div class=\"progress\">\n" +
                "          <div class=\"circle\"></div>\n" +
                "          <div class=\"slider\"><!--进度条（整）-->\n" +
                "            <div class=\"loaded\"></div><!--缓冲条-->\n" +
                "            <div class=\"pace\"></div><!--进度条-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class=\"right\">\n" +
                "      <div class=\"fullTimer\">"+time+"</div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <div class=\"bottom\">\n" +
                "    <div class=\"left\">\n" +
                "      <div class=\"playback\">\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class=\"right\">\n" +
                "      <div class=\"volumeImg\">\n" +
                "        <img src=\"http://101.37.151.91:5000/uploads/html/image/voice.png\" alt=\"\">\n" +
                "      </div>\n" +
                "      <div class=\"volume\"><!--声音-->\n" +
                "        <div class=\"slider\"><!---->\n" +
                "          <div class=\"pace\"></div><!--音量-->\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>";
    }

    public static String getHTML_Template_photo_1(String photoUrl){
        return "<div class=\"bb\"> <img src=\""+photoUrl+"\" alt=\"\"></div>";
    }

    public static String getHTML_Template_text_1(String text){
        return "<div class=\"aa\">\n" +
                "<span class=\"hd-text\">" +text+
                "</span>\n" +
                "</div>";
    }
}
