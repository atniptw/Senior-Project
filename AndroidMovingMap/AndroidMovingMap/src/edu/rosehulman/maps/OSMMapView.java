package edu.rosehulman.maps;

import java.util.HashMap;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import edu.rosehulman.androidmovingmap.AddPOIActivity;
import edu.rosehulman.androidmovingmap.R;
import edu.rosehulman.overlays.AMMItemizedOverlay;
import edu.rosehulman.overlays.AMMOverlayManager;
import edu.rosehulman.server.POI;

public class OSMMapView extends MapView {

	private Context mContext;
	private AMMOverlayManager mOverlayManager;

	private Location deviceLocation;
	private POI device;
	private AMMItemizedOverlay deviceOverlay;

	GestureDetector mLongPressListener;

	int centerX, centerY;

	// TODO Fix these ctors
	public OSMMapView(Context context, int tileSizePixels) {
		super(context, tileSizePixels);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		// mOverlayManager.addOverlay(new POI(0, "Device", latitude, longitude,
		// POItype, attributes));
		mContext = context;
		mLongPressListener = new GestureDetector(context,
				new LongPressGestureListener());
		getOverlays().addAll(mOverlayManager.getHandles());

		device = new POI(0, "Device", deviceLocation.getLatitude(),
				deviceLocation.getLongitude(), "Type_Device",
				new HashMap<String, String>());
		deviceOverlay = new AMMItemizedOverlay(getResources().getDrawable(
				R.drawable.pusheen_car), "Device", mContext);
		deviceOverlay.addOverlay(device);
		getOverlays().add(deviceOverlay.getHandle());
	}

	public OSMMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		mLongPressListener = new GestureDetector(context,
				new LongPressGestureListener());
	}

	@Override
	public void invalidate() {
		super.invalidate();
		getOverlays().addAll(mOverlayManager.getHandles());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		super.onDraw(canvas);
		canvas.restore();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = (int) (MeasureSpec.getSize(widthMeasureSpec));
		int parentHeight = (int) (MeasureSpec.getSize(heightMeasureSpec));
		centerX = parentWidth / 2;
		centerY = parentWidth / 2;
		this.setMeasuredDimension(parentWidth, parentHeight);
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Log.d("touch", "in onTouch " + event.getPointerCount());
		if (super.onTouchEvent(event)) {
		}
		if (mLongPressListener.onTouchEvent(event)) {
			return true;
		}
		return true;
	}

	public AMMOverlayManager getAMMOverlayManager() {
		return mOverlayManager;
	}

	public void setDeviceLocation(Location location) {
		deviceLocation = location;
	}

	private class LongPressGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			Log.d("touch", "in listener " + e.getPointerCount());
			GeoPoint location = (GeoPoint) getProjection().fromPixels(e.getX(),
					e.getY());
			Intent addPoiIntent = new Intent(mContext, AddPOIActivity.class);
			addPoiIntent.putExtra(AddPOIActivity.KEY_GEOPOINT,
					(Parcelable) location);
			((Activity) mContext).startActivityForResult(addPoiIntent,
					AddPOIActivity.NEW_POI_REQUEST);
			super.onLongPress(e);
		}
	}
}
