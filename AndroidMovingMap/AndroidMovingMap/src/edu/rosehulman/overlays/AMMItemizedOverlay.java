package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import edu.rosehulman.server.POI;

public class AMMItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	// FIXME SETH did this cause he is bad and likes to make things public
	public List<POI> mOverlays = new ArrayList<POI>();
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

	public void addOverlay(POI overlay) {
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

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// TODO: figure out where POI info is stored... 
//		DialogFragment poiInfo = new POIinfoDialog(this.getItem(0).mUid, );
		return super.onTouchEvent(event, mapView);
	}

	class POIinfoDialog extends DialogFragment {
		private String mUID;
		private String mName;
		private String mLat;
		private String mLon;
		
		public POIinfoDialog(String UID, String name, String lat, String lon) {
			mUID = UID;
			mName = name;
			mLat = lat;
			mLon = lon;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle("POI: " + mUID)
					.setMessage(
							"Name: " + mName + "\nLatitude: " + mLat
									+ "\nLongitude" + mLon).create();
		}
	};
}
