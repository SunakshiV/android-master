package com.procoin.http.base.baseadapter;

import android.database.DataSetObserver;
import android.widget.BaseExpandableListAdapter;

import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;

/**
 * 不写unregisterDataSetObserver这个。4.0的机子会爆。
 * 
 * @author 2012-3-23 上午10:34:03
 * 
 */
public abstract class AMBaseExpandableListAdapter<T extends TaojinluType> extends BaseExpandableListAdapter {

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}

	protected Group<T> group = null;

	public Object getChild(int groupPosition, int childPosition) {
		if (group == null) return null;
		return group.get(groupPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		if (group == null) return null;
		return group.get(groupPosition);
	}

	public int getGroupCount() {
		return (group == null) ? 0 : group.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	//
	// @Override
	// public int getCount() {
	// return (group == null) ? 0 : group.size();
	// }
	//
	// @Override
	// public T getItem(int position) {
	// return group.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }

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
		notifyDataSetInvalidated();
	}

	public void addItem(Group<T> g) {
		if (g != null && group != null) {
			for (T t : g) {
				group.add(t);
			}
		}
	}
}
