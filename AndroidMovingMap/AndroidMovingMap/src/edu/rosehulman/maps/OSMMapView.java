package edu.rosehulman.maps;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import edu.rosehulman.androidmovingmap.AddPOIActivity;
import edu.rosehulman.overlays.AMMOverlayManager;

public class OSMMapView extends MapView {

	private Context mContext;
	private AMMOverlayManager mOverlayManager;

	private MyLocationOverlay deviceOverlay;

	GestureDetector mLongPressListener;

	int centerX, centerY;

	// TODO Fix these ctors
	public OSMMapView(Context context, int tileSizePixels) {
		super(context, tileSizePixels);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		mLongPressListener = new GestureDetector(context,
				new LongPressGestureListener());
		
		deviceOverlay = new MyLocationOverlay(mContext, this);
		deviceOverlay.enableFollowLocation();
		deviceOverlay.enableMyLocation();
		
		setOverylays();
	}

	public void setOverylays() {
		getOverlays().clear();
		getOverlays().addAll(mOverlayManager.getHandles());
		
		getOverlays().add(deviceOverlay);
	}

	public OSMMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		mLongPressListener = new GestureDetector(context,
				new LongPressGestureListener());
		
		deviceOverlay = new MyLocationOverlay(mContext, this);
		deviceOverlay.enableFollowLocation();
		deviceOverlay.enableMyLocation();
		getOverlays().add(deviceOverlay);
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
