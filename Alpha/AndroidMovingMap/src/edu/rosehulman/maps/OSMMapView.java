package edu.rosehulman.maps;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import edu.rosehulman.overlays.AMMItemizedOverlay;
import edu.rosehulman.overlays.AMMOverlayManager;
import edu.rosehulman.overlays.OverlayListAdapter;

public class OSMMapView extends MapView {

	private Context mContext;
	private AMMOverlayManager mOverlayManager;
	
	public OSMMapView(Context context, int tileSizePixels) {
		super(context, tileSizePixels);
//		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		getOverlays().addAll(mOverlayManager.getOverlayTypes());
	}
	
	public OSMMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		getOverlays().addAll(mOverlayManager.getOverlayTypes());
	}
	
	private float rotation;
	private float pivotX, pivotY;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		canvas.save();
		canvas.rotate(rotation);
		super.onDraw(canvas);
//		canvas.restore();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		this.setMeasuredDimension(parentWidth, parentHeight);
	};
	
	RotationGestureListener blah = new RotationGestureListener();
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		return blah.onTouchEvent(event);
		return mGestureDetector.onTouchEvent(event);
	}
	
	public AMMOverlayManager getAMMOverlayManager() {
		return mOverlayManager;
	}
	
	SimpleOnGestureListener mSimpleOnGestureListener = new SimpleOnGestureListener() {
		public void onLongPress(MotionEvent e) {
			GeoPoint location = (GeoPoint) getProjection().fromPixels(e.getX(), e.getY());
			DialogFragment addOverlayFragment = new AddOverlayDialogFragment(mOverlayManager, location, mContext);
			addOverlayFragment.show(((Activity)mContext).getFragmentManager(), "dialog");
		};
	};
	private GestureDetector mGestureDetector = new GestureDetector(mContext, mSimpleOnGestureListener);
	
	
	
	class AddOverlayDialogFragment extends DialogFragment implements OnItemClickListener {
		
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
	
	private class RotationGestureListener  {
		
		private static final int INVALID_POINTER_ID = -1;
		private float fX, fY, sX, sY, focalX, focalY;
		private int pointerID1, pointerID2;
		
		public RotationGestureListener() {
			pointerID1 = INVALID_POINTER_ID;
			pointerID2 = INVALID_POINTER_ID;
		}

		public boolean onTouchEvent(MotionEvent event) {
			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				sX = event.getX();
				sY = event.getY();
				pointerID1 = event.getPointerId(0);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				fX = event.getX();
				fY = event.getY();
				focalX = getMidpoint(fX, sX);
				focalY = getMidpoint(fY, sY);
				pivotX = focalX;
				pivotY = focalY;
				pointerID2 = event.getPointerId(event.getActionIndex());
				break;
			case MotionEvent.ACTION_MOVE:
				if (pointerID1 == INVALID_POINTER_ID || pointerID2 == INVALID_POINTER_ID) {
					break;
				}
				float nfX, nfY, nsX, nsY;
				nfX = event.getX(event.findPointerIndex(pointerID1));
				nfY = event.getY(event.findPointerIndex(pointerID1));
				nsX = event.getX(event.findPointerIndex(pointerID2));
				nsY = event.getY(event.findPointerIndex(pointerID2));
				float angle = angleBetweenLines(fX, fY, nfX, nfY, sX, sY, nsX, nsY);
				rotation += angle;
				invalidate();
				fX = nfX;
				fY = nfY;
				sX = nsX;
				sY = nsY;
				break;
			case MotionEvent.ACTION_UP:
				pointerID1 = INVALID_POINTER_ID;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				pointerID2 = INVALID_POINTER_ID;
				break;
			}
			return true;
		}
		
		private float getMidpoint(float a, float b) {
			return (a+b)/2;
		}
		
		private float angleBetweenLines(float fx1, float fy1, float fx2, float fy2, float sx1, float sy1, float sx2, float sy2) {
			float angle1 = (float) Math.atan2(fy1 - fy2, fx1 - fx2);
			float angle2 = (float) Math.atan2(sy1 - sy2, sx1 - sx2);
			return findAngleDelta(angle1, angle2);
		}
		
		private float findAngleDelta(float angle1, float angle2) {
			float from = angle2 % 360.0f;
			float to = angle1 % 360.0f;
			
			float dist = to - from;
			
			if (dist < -180.0f) {
				dist += 360.0f;
			} else if (dist > 180.0f) {
				dist -= 360.0f;
			}
			
			return dist;
		}
	}
}
