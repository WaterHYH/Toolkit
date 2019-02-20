package com.dodoo_tech.gfal.adapter;

import android.content.Context;
import androidx.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dodoo_tech.gfal.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ListView的通用adapter抽象类<br/>
 * 基本用法：继承{@link CommonAdapter}并实现{@link CommonAdapter#getView(int, ViewHolder, Object)}抽象方法，通过{@link ViewHolder#getView(int)}方法获取item内的各种控件
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

    public CommonAdapter(Context context, int itemLayout) {
        this(context,itemLayout,new ArrayList<T>());
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public boolean addItem(T item){
        return datas.add(item);
    }

    public boolean addItem(int position,T item){
        try{
            datas.add(position,item);
            return true;
        }catch (Exception e){
            LogUtil.logError(e);
        }
        return false;
    }

    public boolean addItems(Collection<? extends T> items){
        return datas.addAll(items);
    }

    public boolean addItems(int position,Collection<? extends T> items){
        return datas.addAll(position,items);
    }

    public T removeItem(int position){
        return datas.remove(position);
    }

    public boolean removeItem(T item){
        return datas.remove(item);
    }

    public void clear(){
        datas.clear();
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
        try{
            return getItem(i).hashCode();
        }catch (Exception e){
            LogUtil.logError(e);
        }
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
