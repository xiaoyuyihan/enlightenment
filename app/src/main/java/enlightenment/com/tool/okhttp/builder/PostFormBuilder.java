package enlightenment.com.tool.okhttp.builder;

import enlightenment.com.tool.okhttp.request.PostFormRequest;
import enlightenment.com.tool.okhttp.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on 15/12/14.
 */
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, files, id).build();
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PostFormBuilder files(Map<String, ArrayList<File>> files) {
        for (String key : files.keySet()) {
            for (File file : files.get(key)) {
                this.files.add(new FileInput(key, file.getName(), file));
            }
        }
        return this;
    }

    public PostFormBuilder files(String name, ArrayList<File> files) {
        for (File file : files) {
            this.files.add(new FileInput(name, file.getName(), file));
        }
        return this;
    }

    public PostFormBuilder addFile(String name, File file) {
        files.add(new FileInput(name, file.getName(), file));
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }


    @Override
    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

}
