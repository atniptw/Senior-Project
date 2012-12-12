package edu.rosehulman.maps;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
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
		mOverlayManager = new AMMOverlayManager(context);
		mContext = context;
		getOverlays().addAll(mOverlayManager.getOverlayTypes());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
}
