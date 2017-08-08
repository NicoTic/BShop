
package com.example.alipay.myreadingapplication.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Erich Lee
 * @desc <pre></pre>
 * @Date Mar 6, 2013
 */
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> col) {
        return col == null || col.size() == 0;
    }

    /**
     * 添加一个modle到集合
     * @param col
     * @param model
     * @param <T>
     */
    public static <T> void add(Collection<T> col,T model) {
        if (col==null){
            col=new ArrayList<>();
        }
        if (model!=null){
            col.add(model);
        }
    }

    /**
     * 添加一个model集合到集合
     * @param col
     * @param models
     * @param <T>
     */
    public static <T> void addAll(Collection<T> col,Collection<T> models) {
        if (col==null){
            col=new ArrayList<>();
        }
        if (models!=null){
            col.addAll(models);
        }
    }
    public static <T> boolean isNotEmpty(Collection<T> col) {
        return !isEmpty(col);
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.size() == 0;
    }

    public static <T> List<T> copy(List<T> list) {
        if (isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }
        List<T> copied = new ArrayList<T>();
        copied.addAll(list);
        return copied;
    }

}
