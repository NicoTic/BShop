package cniao5.com.admin.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by Administration on 2017/4/20.
 */
public class JsonUtil {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static Gson getGson(){
        return gson;
    }

    public static <T> T fromJson(String json,Class<T> clz){
        return  gson.fromJson(json,clz);
    }
    public static <T> T fromJson(String json,Type type){
        return  gson.fromJson(json,type);
    }
    public static String toJSON(Object object){
        return gson.toJson(object);
    }
}
