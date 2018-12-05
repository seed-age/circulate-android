package cc.seedland.oa.circulate.modle.net;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;

/**
 * Created by hch on 2018/1/13.
 */

public class HttpParams {
    public LinkedHashMap<String,List<String>> formParams;
    public LinkedHashMap<String,List<FileWrapper>> fileParams;


    public HttpParams() {
        init();
    }

    private void init() {
        formParams = new LinkedHashMap<>();
        fileParams = new LinkedHashMap<>();
    }

    public void put(String key,String value) {
        if (key != null && value != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.add(value);
        }
    }
    public void put(String key,int value) {
        if (key != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.add(String.valueOf(value));
        }
    }
    public void put(String key,float value) {
        if (key != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.add(String.valueOf(value));
        }
    }

    public void put(String key,long value) {
        if (key != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.add(String.valueOf(value));
        }
    }

    public void put(String key,char value) {
        if (key != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.add(String.valueOf(value));
        }
    }

    public void put(String key,boolean value) {
        if (key != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.add(String.valueOf(value));
        }
    }

    public void put(String key,List<String> value) {
        if (key != null){
            List<String> values = formParams.get(key);
            if (values == null) {
                values = new ArrayList<>();
                formParams.put(key,values);
            }
            values.clear();
            values.addAll(value);
        }
    }

    public void put(String key,File file) {
        put(key,file,file.getName(),guessMimeType(file.getName()));
    }

    public void putFileParams(String key, List<File> files) {
        if (key != null && files != null && !files.isEmpty()) {
            for (File file : files) {
                put(key, file);
            }
        }
    }

    public void put(String key, File file, String fileName, MediaType contentType) {
        if (key != null) {
            List<FileWrapper> fileWrappers = fileParams.get(key);
            if (fileWrappers == null) {
                fileWrappers = new ArrayList<>();
                fileParams.put(key, fileWrappers);
            }
            fileWrappers.add(new FileWrapper(file, fileName, contentType));
        }
    }

    private MediaType guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        path = path.replace("#", "");   //解决文件名中含有#号异常的问题
        String contentType = fileNameMap.getContentTypeFor(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }

    /** 文件类型的包装类 */
    public static class FileWrapper {
        public File file;
        public String fileName;
        public MediaType contentType;
        public long fileSize;

        public FileWrapper(File file, String fileName, MediaType contentType) {
            this.file = file;
            this.fileName = fileName;
            this.contentType = contentType;
            this.fileSize = file.length();
        }

        @Override
        public String toString() {
            return "FileWrapper{" + "file=" + file + ", fileName='" + fileName + ", contentType=" + contentType + ", fileSize=" + fileSize + '}';
        }
    }
}
