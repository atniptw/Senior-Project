package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class AMMItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Drawable mIcon;
	private String mName;
	private boolean mActive;
	
	public AMMItemizedOverlay(Drawable pDefaultMarker, String name,
			Context context) {
		super(pDefaultMarker, new DefaultResourceProxyImpl(context));
		mIcon = pDefaultMarker;
		mName = name;
		mActive = true;
	}
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected OverlayItem createItem(int index) {
		return mOverlays.get(index);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public Drawable getIcon() {
		return mIcon;
	}
	
	public String getName() {
		return mName;
	}
	
	public boolean isActive() {
		return mActive;
	}

}
