package edu.rosehulman.maps;


import org.metalev.multitouch.controller.MultiTouchController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import edu.rosehulman.androidmovingmap.AddPOIActivity;
import edu.rosehulman.overlays.AMMOverlayManager;

public class OSMMapView extends MapView {

	private Context mContext;
	private AMMOverlayManager mOverlayManager;

	AMMGestureListener mGestureListener = new AMMGestureListener(this);
	GestureDetector mLongPressListener;
	
	int centerX, centerY;
	
	
	//TODO Fix these ctors
	public OSMMapView(Context context, int tileSizePixels) {
		super(context, tileSizePixels);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		mLongPressListener = new GestureDetector(context, new LongPressGestureListener());
		getOverlays().addAll(mOverlayManager.getHandles());
	}
	
	public OSMMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		mLongPressListener = new GestureDetector(context, new LongPressGestureListener());
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
		centerX = parentWidth/2;
		centerY = parentWidth/2;
		this.setMeasuredDimension(parentWidth, parentHeight);
	};
	
	private MultiTouchController<Object> multiTouchController = new MultiTouchController<Object>(mGestureListener);
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLongPressListener.onTouchEvent(event)) {
			return true;
		}
		if (multiTouchController.onTouchEvent(event)) {
			invalidate();
			return true;
		}
		if (super.onTouchEvent(event)) {
			return true;
		}
		return false;
	}
	
	public AMMOverlayManager getAMMOverlayManager() {
		return mOverlayManager;
	}	
	
	private class LongPressGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			GeoPoint location = (GeoPoint)getProjection().fromPixels(e.getX(), e.getY());
			Intent addPoiIntent = new Intent(mContext, AddPOIActivity.class);
			addPoiIntent.putExtra(AddPOIActivity.KEY_GEOPOINT, (Parcelable)location);
			((Activity)mContext).startActivityForResult(addPoiIntent, AddPOIActivity.NEW_POI_REQUEST);
			super.onLongPress(e);
		}
	}
}
