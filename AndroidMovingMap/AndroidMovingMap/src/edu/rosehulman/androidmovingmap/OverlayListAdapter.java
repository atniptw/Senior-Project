package edu.rosehulman.androidmovingmap;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.rosehulman.overlays.AMMItemizedOverlay;
import edu.rosehulman.overlays.IOverlayManager;
import edu.rosehulman.overlays.OverlayIconRegistry;

public class OverlayListAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mOverlayNames;
	
	public OverlayListAdapter(Context context, List<String> overlayNames) {
		this.mContext = context;
		this.mOverlayNames = overlayNames;
	}
	
	public int getCount() {
		return mOverlayNames.size();
	}

	public Object getItem(int i) {
		return mOverlayNames.get(i);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		OverlayTypeView view;
		OverlayIconRegistry iconRegistry = OverlayIconRegistry.getInstance();
		
		if (convertView == null) {
			view = new OverlayTypeView();
		} else {
			view = (OverlayTypeView) convertView;
		}
		
		String name = mOverlayNames.get(position);
		Drawable icon = iconRegistry.getIcon(name);
		view.setImage(icon);
		view.setText(name);
		return view;
	}
	
	private class OverlayTypeView extends LinearLayout {

		private ImageView mImageView;
		private TextView mTextView;
		
		public OverlayTypeView() {
			super(mContext);
			
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			inflater.inflate(R.layout.overlay_list_item, this);
			
			mImageView = (ImageView)findViewById(R.id.overlay_icon);
			mTextView = (TextView)findViewById(R.id.overlay_text);
		}
		
		public void setImage(Drawable icon) {
			mImageView.setImageDrawable(icon);
		}
		
		public void setText(String text) {
			mTextView.setText(text);
		}
		
	}

}
