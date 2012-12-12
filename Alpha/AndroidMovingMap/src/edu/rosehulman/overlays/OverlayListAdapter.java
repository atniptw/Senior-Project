package edu.rosehulman.overlays;

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
import edu.rosehulman.androidmovingmap.R;

public class OverlayListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AMMItemizedOverlay> mOverlays;
	
	public OverlayListAdapter(Context context, OverlayManager manager) {
		this.mContext = context;
		this.mOverlays = manager.getOverlayTypes();
	}
	
	public int getCount() {
		return mOverlays.size();
	}

	public Object getItem(int i) {
		return mOverlays.get(i);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		OverlayTypeView view;
		
		if (convertView == null) {
			view = new OverlayTypeView();
		} else {
			view = (OverlayTypeView) convertView;
		}
		
		AMMItemizedOverlay overlay = mOverlays.get(position);
		view.setImage(overlay.getIcon());
		view.setText(overlay.getName());
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
