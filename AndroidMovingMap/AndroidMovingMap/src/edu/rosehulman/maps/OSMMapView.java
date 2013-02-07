package edu.rosehulman.maps;

import java.io.Serializable;

import org.metalev.multitouch.controller.MultiTouchController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import edu.rosehulman.androidmovingmap.AddPOIActivity;
import edu.rosehulman.androidmovingmap.OverlayListAdapter;
import edu.rosehulman.overlays.AMMItemizedOverlay;
import edu.rosehulman.overlays.AMMOverlayManager;

public class OSMMapView extends MapView {

	private Context mContext;
	private AMMOverlayManager mOverlayManager;

	AMMGestureListener mGestureListener = new AMMGestureListener(this);
	GestureDetector mLongPressListener;
	TransformationData transform = new TransformationData();
	
	int centerX, centerY;
	
	public OSMMapView(Context context, int tileSizePixels) {
		super(context, tileSizePixels);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		mLongPressListener = new GestureDetector(context, new LongPressGestureListener());
		getOverlays().addAll(mOverlayManager.getOverlayTypes());
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
		getOverlays().addAll(mOverlayManager.getOverlayTypes());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.save();
		
		canvas.rotate(transform.getRotation());
		canvas.scale(transform.getZoom(), transform.getZoom(), centerX, centerY);
		Log.d("transform", "My zoom scale factor is " + transform.getZoom());
		super.onDraw(canvas);
//		canvas.restore();
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
	
	class AddOverlayDialogFragment extends DialogFragment implements OnItemClickListener {
		//TODO Extract this
		private AMMOverlayManager mOverlayManager;
		private ListView mOverlayListView;
		private GeoPoint mLocation;
		
		public AddOverlayDialogFragment(AMMOverlayManager manager, GeoPoint location, Context context) {
			mOverlayManager = manager;
			mLocation = location;
			mOverlayListView = new ListView(context);
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Dialog dialog = new Dialog(getActivity());
			
			dialog.setContentView(mOverlayListView);
			BaseAdapter adapter = new OverlayListAdapter(getActivity(), mOverlayManager);
			
			mOverlayListView.setAdapter(adapter);
			
			mOverlayListView.setOnItemClickListener(this);

			return dialog;
		}

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			AMMItemizedOverlay overlayType = (AMMItemizedOverlay) mOverlayListView.getItemAtPosition(pos);
			overlayType.addOverlay(new OverlayItem("lol", "teehee", mLocation));
			invalidate();
			dismiss();
		}
	}
	
	private class LongPressGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			GeoPoint location = (GeoPoint)getProjection().fromPixels(e.getX(), e.getY());
			Intent addPoiIntent = new Intent(mContext, AddPOIActivity.class);
			addPoiIntent.putExtra(AddPOIActivity.KEY_GEOPOINT, (Parcelable)location);
			addPoiIntent.putExtra(AddPOIActivity.KEY_OVERLAY_TYPES, (Serializable)mOverlayManager.getOverlayTypes());
			mContext.startActivity(addPoiIntent);
			super.onLongPress(e);
		}
	}
	
	public class TransformationData {
		private float pivotX, pivotY;
		private float rotationAngle;
		private float zoom;
		
		public TransformationData() {
			pivotX = 0.0f;
			pivotY = 0.0f;
			rotationAngle = 0.0f;
			zoom = 1.0f;
		}
		
		public float getRotation() {
			return rotationAngle;
		}
		
		public float getZoom() {
			return zoom;
		}
		
		public void scaleZoom(float scale) {
			zoom *= scale;
			if (zoom < 1) zoom = 1;
			//TODO probably need a maximum too
		}
		
		public void setScale(float scale) {
			zoom = scale;
			if (zoom < 1) zoom = 1;
		}
	}
}
