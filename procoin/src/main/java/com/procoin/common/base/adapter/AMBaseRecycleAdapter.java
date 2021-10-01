package com.procoin.common.base.adapter;

import androidx.appcompat.widget.RecyclerView;

import com.procoin.http.base.Group;
import com.procoin.http.base.IoItemReplace;
import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 17-7-5.
 */

public abstract class AMBaseRecycleAdapter<T extends TaojinluType> extends RecyclerView.Adapter{

    protected Group<T> group = null;


    @Override
    public int getItemCount() {
        return (group == null) ? 0 : group.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void clearAllItem() {
        if (group != null && group.size() > 0){
            group.clear();
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        if (group != null && position < group.size()) group.remove(position);
        notifyDataSetChanged();
    }

    public boolean removeItem(T t) {
        if (group != null) return group.remove(t);
        else return false;
    }

    public void insertItem(int index, T t) {
        if (group == null) group = new Group<T>();
        group.add(index, t);
    }



    public T getItem(int position) {
        try {
            return position < getItemCount() ? group.get(position) : null;
        } catch (Exception e) {
            return null;
        }

    }

    public Group<T> getGroup() {
        return group;
    }

    public void setGroup(Group<T> g) {
        group = g;
        notifyDataSetChanged();
    }

    public void setGroupNotChang(Group<T> g) {
        group = g;
    }

    public void addItem(Group<T> g) {
        if (group == null) group = new Group<T>();
        if (g != null && group != null) {
            for (T t : g) {
                group.add(t);
            }
        }
    }

    public void addFirstItem(Group<T> g) {
        if (group == null) group = new Group<T>();
        if (g != null && group != null) {
            for (T t : g) {
                group.add(0, t);
            }
        }
    }

    public void addFirstItem(T t) {
        if (group == null) group = new Group<T>();
        if (t != null) {
            group.add(0, t);
        }
    }

    public void addItem(T g) {
        if (group == null) group = new Group<T>();
        group.add(g);
    }

    public void setGroupAndChang(Group<T> g) {
        group = g;
        notifyDataSetChanged();
    }

    public void addItemBefore(Group<T> g) {
        if (group == null) group = new Group<T>();
        if (g != null) {
            g.addAll(group);
            group.clear();
            group.addAll(g);
            g.clear();
        }
    }

    /**
     * 此方法过滤相同项根据ioItemReplace的返回值
     *
     * @param g
     * @param ioItemReplace
     */
    public void addItemBeforeReplace(Group<T> g, IoItemReplace<T> ioItemReplace) {
        if (group == null) group = new Group<T>();
        Group<T> b = new Group<T>();
        b.addAll(group);
        for (T o : group) {
            for (T n : g) {
                if (ioItemReplace.ioItemReplace(o, n)) {
                    if(b.remove(o))break;
                }
            }
        }
        g.addAll(b);
        group.clear();
        group.addAll(g);
        g.clear();
        b.clear();
        b=null;
    }

}
