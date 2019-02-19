package com.dodoo_tech.gfal.adapter;

import android.content.Context;
import androidx.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dodoo_tech.gfal.utils.LogUtil;

import java.util.List;

/**
 * 通用的ListView的adapter
 * @param <T> item对应的数据结构
 * @author Created by waterHYH on 2019/2/18.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    //数据源
    protected List<T> datas;
    //上下文
    protected Context context;
    //关联的布局id
    private int itemLayout;

    public CommonAdapter(Context context, int itemLayout, List<T> datas) {
        this.datas = datas;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        return datas==null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int i) {
        if (datas == null) {
            return null;
        }
        try{
            return datas.get(i);
        }catch (Exception e){
            LogUtil.logError(e);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try{
            ViewHolder viewHolder = getViewHolder(view);
            getView(i,viewHolder,datas.get(i));
            return viewHolder.rootView;
        }catch (Exception e){
            LogUtil.logError(e);
        }
        return null;
    }

    public abstract void getView(int postion, ViewHolder holder, T data);

    public static class ViewHolder {
        private View rootView;

        private ViewHolder(View rootView) {
            this.rootView = rootView;
            rootView.setTag(this);
        }

        public <T extends View> T getView(@IdRes int id){
            return rootView.findViewById(id);
        }
    }

    private ViewHolder getViewHolder(View itemView) {
        ViewHolder viewHolder;
        if (itemView == null) {
            itemView = View.inflate(context, itemLayout, null);
            viewHolder = new ViewHolder(itemView);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        return viewHolder;
    }
}
