package xwc.andorid.recyclergridview;

import android.support.annotation.IdRes;

/**
 * 简单视图匹配
 * Created by 向文成 on 2016-7-7.
 */
public class SimpleViewMatcher {
    private int id;
    private int type;

    public SimpleViewMatcher(@IdRes int id, int type){
        this.id = id;
        this.type = type;
    }

    public int getId(){ return id; }

    public int getType(){ return this.type; }

    public static final int TEXTVIEW = 1;
    public static final int IMAGEVIEW = 2;
}
