package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import edu.rosehulman.server.POI;

public class AMMItemizedOverlay implements OnItemGestureListener<POI>{
	
	private ItemizedIconOverlay<POI> mIIO;

	// FIXME SETH did this cause he is bad and likes to make things public
	public List<POI> mOverlays = new ArrayList<POI>();

	private Drawable mIcon;
	private String mName;
	private boolean mActive;
	
	public AMMItemizedOverlay(Drawable icon, String name,
			Context context) {
		mIIO = new ItemizedIconOverlay<POI>(mOverlays, icon, (OnItemGestureListener<POI>)this, new DefaultResourceProxyImpl(context));
		mIcon = icon;
		mName = name;
		mActive = true;
		
		OverlayIconRegistry.getInstance().registerIcon(name, icon);
	}
	
	public void addOverlay(POI poi) {
		mIIO.addItem(poi);
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

	public boolean onItemLongPress(int arg0, POI arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onItemSingleTapUp(int arg0, POI arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
