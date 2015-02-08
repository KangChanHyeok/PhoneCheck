package com.kch.phonecheck.system;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SystemInfoAdapter extends BaseAdapter {

	public static final int INVALID_ROW = -1;
	public int count=0;
	protected static final int TYPE_HEADER_VIEW = 0;
	protected static final int TYPE_ROW_VIEW = 1;
	
	protected static final int TYPE_HEADER = 0;
	protected static final int TYPE_ROW = 1;
	
	private int mItemsInSections[] = null;
	private Context mContext;
	
	public SystemInfoAdapter(Context context) {
		mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}

	public int getSectionForPosition(int position) {
		for (int section = 0; section < mItemsInSections.length; section++) {
			int size = mItemsInSections[section];
			if (position == 0 || position <= size) {
				return section;
			}
			position -= (size + 1);
		}
		return -1;
	}
	
	public int getRowForPosition(int position) {
		for (int section = 0; section < mItemsInSections.length; section++) {
			int size = mItemsInSections[section];
			if (position <= size) {
				return (position - 1);
			}
			position -= (size + 1);
		}
		return INVALID_ROW;
	}
	
	protected abstract int getNumberOfSections();
	
	protected abstract int getNumberOfRowsInSection(int section);
	
	public abstract Object getItemInSectionAndRow(int section, int row);
	
	protected int getViewTypeForSection(int section) {
		return TYPE_HEADER_VIEW;
	}
	
	protected int getViewTypeForRowInSection(int section, int row) {
		return TYPE_ROW_VIEW;
	}
	
	protected int getNumberOfViewTypeForSections() {
		return 1;
	}
	
	protected int getNumberOfViewTypeForRows() {
		return 1;
	}
	
	protected abstract View newSectionView(Context context, int section, ViewGroup parent);
	
	protected abstract void bindSectionView(Context context, int section, View convertView);
	
	protected abstract View newRowViewInSection(Context context, int section, int row, ViewGroup parent);
	
	protected abstract void bindRowViewInSection(Context context, int section, int row, View convertView);
	
	public int getItemType(int position) {
		for (int section = 0; section < mItemsInSections.length; section++) {
			int size = mItemsInSections[section];
			if (position == 0) {
				return TYPE_HEADER;
			}
			if (position <= size) {
				return TYPE_ROW;
			}
			position -= (size + 1);
		}
		return IGNORE_ITEM_VIEW_TYPE;
	}
	
	@Override
	public int getItemViewType(int position) {
		for (int section = 0; section < mItemsInSections.length; section++) {
			int size = mItemsInSections[section];
			if (position == 0) {
				return getViewTypeForSection(section);
			}
			if (position <= size) {
				return getViewTypeForRowInSection(section, (position - 1));
			}
			position -= (size + 1);
		}
		return IGNORE_ITEM_VIEW_TYPE;
	} 
	
	@Override
	public int getViewTypeCount() {
		int count = getNumberOfViewTypeForSections();
		count += getNumberOfViewTypeForRows();
		return count;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		int sections = getNumberOfSections();
		mItemsInSections = new int[sections];
		
		count += sections;
		for (int section = 0; section < sections; section++) {
			int items = getNumberOfRowsInSection(section);
			mItemsInSections[section] = items;
			count += items;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		for (int section = 0; section < mItemsInSections.length; section++) {
			int size = mItemsInSections[section];
			if (position == 0) {
				// section
				if (convertView == null) {
					convertView = newSectionView(mContext, section, parent);
				}
				bindSectionView(mContext, section, convertView);
				return convertView;
			}
			if (position <= size) {
				// row
				int count=0;
				if(section>0){
					for(int i=0; i<section; i++){
						count+=mItemsInSections[i];
					}
					count+=position;
				}else{
					count=position;
				}
				if (convertView == null) {
					convertView = newRowViewInSection(mContext, section, (count - 1), parent);
				}
				
				bindRowViewInSection(mContext, section, (count - 1), convertView);
				return convertView;
			}
			position -= (size + 1);
		}
		return convertView;
	}

}