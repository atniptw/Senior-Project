package edu.rosehulman.androidmovingmap;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import edu.rosehulman.overlays.AMMItemizedOverlay;
import edu.rosehulman.overlays.IItemizedOverlay;
import edu.rosehulman.overlays.OverlayIconRegistry;

public class OverlayListAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mOverlayNames;
	private int mChoiceMode;
	private List<IItemizedOverlay> mOverlays;

	public OverlayListAdapter(Context context, List<String> overlayNames,
			int choiceMode) {
		this.mContext = context;
		this.mOverlayNames = overlayNames;
		this.mChoiceMode = choiceMode;
	}
	
	public OverlayListAdapter(Context context, List<String> overlayNames,
			int choiceMode, List<IItemizedOverlay> overlays) {
		this.mContext = context;
		this.mOverlayNames = overlayNames;
		this.mChoiceMode = choiceMode;
		this.mOverlays = overlays;
		
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
			view = new OverlayTypeView(mContext, mChoiceMode);
		} else {
			view = (OverlayTypeView) convertView;
		}

		String name = mOverlayNames.get(position);
		Drawable icon = iconRegistry.getIcon(name);
		view.setImage(icon);
		view.setText(name);
		return view;
	}

	public View getView(int position, View convertView) {
		OverlayTypeView view;
		OverlayIconRegistry iconRegistry = OverlayIconRegistry.getInstance();

		AMMItemizedOverlay currentOverlay = (AMMItemizedOverlay) mOverlays.get(position);
		Log.d("AMM", ";DFJ;LAIEJFIOAJ;LKDFJSA         " + currentOverlay.toString());
		
		if (convertView == null) {
			view = new OverlayTypeView(mContext, mChoiceMode, currentOverlay);
		} else {
			view = (OverlayTypeView) convertView;
			view.setOverlay(currentOverlay);
		}

		
		String name = mOverlayNames.get(position);
		Drawable icon = iconRegistry.getIcon(name);
		view.setImage(icon);
		view.setText(name);
		
		return view;
	}	

}
