package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import edu.rosehulman.server.POI;

public class AMMItemizedOverlay implements IItemizedOverlay, OnItemGestureListener<POI>{
	
	private ItemizedIconOverlay<POI> mIIO;

	private List<POI> mOverlays = new ArrayList<POI>();

	private Drawable mIcon;
	private String mName;
	private boolean mActive;
	private Context mContext;
	
	public AMMItemizedOverlay(Drawable icon, String name,
			Context context) {
		mIIO = new ItemizedIconOverlay<POI>(mOverlays, icon, (OnItemGestureListener<POI>)this, new DefaultResourceProxyImpl(context));
		mIcon = icon;
		mName = name;
		mActive = true;
		mContext = context;
		
		OverlayIconRegistry.getInstance().registerIcon(name, icon);
	}
	
	public List<POI> getOverlays() {
		return mOverlays;
	}
	
	public void addOverlay(POI poi) {
		mIIO.addItem(poi);
	}
	
	public void deleteOverlay(POI poi) {
		mIIO.removeItem(poi);
	}
	
	public int size() {
		return mOverlays.size();
	}
	
	public void clear() {
		mOverlays.clear();
	}
	
	public Drawable getIcon() {
		return mIcon;
	}
	
	public String getName() {
		return mName;
	}
	
	public ItemizedOverlay<POI> getHandle() {
		return mIIO;
	}
	
	public boolean isActive() {
		return mActive;
	}
	
	public void setActive(boolean active) {
		mActive = active;
	}

	public boolean onItemLongPress(int arg0, POI item) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onItemSingleTapUp(int arg0, POI item) {
		// TODO Auto-generated method stub
		Log.d("AMM", "I touched something! Wee!");
		POIinfoDialog mDialog = new POIinfoDialog(item);
		mDialog.show(((Activity)mContext).getFragmentManager(), "POI info");
		return true;
	}
	
	private class POIinfoDialog extends DialogFragment {

		private POI mPOI;
		
		public POIinfoDialog(POI item) {
			mPOI = item;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setMessage(mPOI.toString()).setNegativeButton("Delete", new OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							deleteOverlay(mPOI);
						}
					})
					.create();
		}
	}

}
