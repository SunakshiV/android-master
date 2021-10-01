package com.procoin.http.base.baseadapter;

import android.database.DataSetObserver;
import android.widget.BaseAdapter;

import com.procoin.http.base.Group;
import com.procoin.http.base.IoItemReplace;
import com.procoin.http.base.TaojinluType;

/**
 * 不写unregisterDataSetObserver这个。4.0的机子会爆。
 * 
 * @author 2012-3-23 上午10:34:03
 * 
 */
public abstract class AMBaseAdapter<T extends TaojinluType> extends BaseAdapter {

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}

	protected Group<T> group = null;

	public void clearAllItem() {
		if (group != null && getCount() > 0) group.clear();
	}

	public void removeItem(int position) {
		if (group != null && position < getCount()) group.remove(position);
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

	@Override
	public int getCount() {
		return (group == null) ? 0 : group.size();
	}

	@Override
	public T getItem(int position) {
		try {
			return position < getCount() ? group.get(position) : null;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return (group == null) ? true : group.isEmpty();
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
