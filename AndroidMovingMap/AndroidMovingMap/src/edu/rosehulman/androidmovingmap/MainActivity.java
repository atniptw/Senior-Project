package edu.rosehulman.androidmovingmap;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.rosehulman.maps.OSMMapView;
import edu.rosehulman.overlays.AMMItemizedOverlay;
import edu.rosehulman.overlays.AddPOITypeDialogFragment;
import edu.rosehulman.server.POI;
import edu.rosehulman.server.Server;

public class MainActivity extends Activity implements OnClickListener,
		Serializable {

	private OSMMapView mMapView;
	private LocationManager locationManager;
	private Button mPOITypeButton;

	private XYTileSource tileSource;
//	private String mapSourcePrefix = "http://king.rose-hulman.edu/~king/testMessage/";
	private String mapSourcePrefix = "http://queen.wlan.rose-hulman.edu/";
	private ArrayList<String> mapSourceNames = new ArrayList<String>(Arrays.asList("map1/", "map2/"));
	private ArrayList<Integer> mapMaxZoom = new ArrayList<Integer>(Arrays.asList(5,4));
	private int mapSourceIndex = 0;

	private int UID_to_track = 1;
	
   	private Handler invalidateDisplay = new Handler() {
	    @Override
		public void handleMessage(Message msg) {
	    	updatePOIandScreen();
	    }
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int refreshGPStime = 100;
		int refreshGPSdistance = 10;

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider provider = locationManager
				.getProvider(LocationManager.GPS_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				refreshGPStime, refreshGPSdistance, listener);

		mMapView = (OSMMapView) findViewById(R.id.map_view);
		mMapView.setClickable(true);
		mMapView.setMultiTouchControls(true);
		mMapView.setBuiltInZoomControls(true);

		// Comment back in to use MAPNIK server data
//		mMapView.setTileSource(TileSourceFactory.MAPNIK);
        tileSource = new XYTileSource("local" + mapSourceIndex, null, 0, 5, 256, ".png",
        		mapSourcePrefix + mapSourceNames.get(mapSourceIndex));
        mMapView.setTileSource(tileSource);

		mPOITypeButton = (Button) findViewById(R.id.poi_types);
		mPOITypeButton.setOnClickListener(this);

		Server.getInstance().updatePOIHandler = invalidateDisplay;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.add_poi_type) {
			Toast.makeText(this, "I can totally add POI types",
					Toast.LENGTH_SHORT).show();
			AddPOITypeDialogFragment.newInstance(
					mMapView.getAMMOverlayManager(), this).show(
					getFragmentManager(), "lol");
			return true;
		} else if (itemId == R.id.menu_settings) {
			Intent intent = new Intent(this, Preferences.class);
			startActivity(intent);
			return true;
		} else if (itemId == R.id.menu_cycle_map_type) {
			mapSourceIndex = (mapSourceIndex + 1) % mapSourceNames.size();
			tileSource = new XYTileSource("local" + mapSourceIndex, null, 0, mapMaxZoom.get(mapSourceIndex), 256, ".png",
            		mapSourcePrefix + mapSourceNames.get(mapSourceIndex));
			Log.d("menu cycle", "pathBase: " + tileSource.getTileURLString(new MapTile(0,0,0)));
			mMapView.setTileSource(tileSource);
			mMapView.invalidate();
			return true;
		} else if (itemId == R.id.menu_track_point) {
			UID_to_track += 1;
			if (UID_to_track == Server.getInstance().POIelements.size())
			{
				UID_to_track = -1;
			}				
			return true;
		} else if (itemId == R.id.menu_start_stop_sync) {
			if (Server.getInstance().serverRunning())
        	{
        		Server.getInstance().stopServer();
        	} else
        	{
        		Server.getInstance().startServer();
        	}
			return true;
		} else if (itemId == R.id.menu_push_server) {
			Log.d("POI", "attempting to sync onto server");
			for (AMMItemizedOverlay type : mMapView.getAMMOverlayManager().getOverlayTypes())
			{
	    		Iterator<POI> iter = type.mOverlays.iterator();
	    		while (iter.hasNext())
	    		{
	    			POI tempPoint = iter.next();
	    			if (tempPoint.getUID() < 0)
	    			{
						try {
							Server.getInstance().sendMessage(tempPoint.toJSONString() + "\n");
							// TODO validate this TODO 
								// TODO Seth delete item so when server pushes back we don't keep duplicate
							iter.remove();
						} catch (IOException e) {
							Log.d("POI", "failed to send POI");
						}
	    			}
				}
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			new EnableGpsDialogFragment().show(getFragmentManager(),
					"enableGpsDialog");
		}
	}

	private class EnableGpsDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.enable_gps)
					.setMessage(R.string.enable_gps_dialog)
					.setPositiveButton(R.string.enable_gps,
							new DialogInterface.OnClickListener() {
								// @Override
								public void onClick(DialogInterface dialog,
										int which) {
									enableLocationSettings();
								}
							}).setNegativeButton(R.string.server_gps, null)
					.create();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(listener);
		Server.getInstance().stopServer();
	}

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	private final LocationListener listener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onLocationChanged(Location location) {
			CharSequence loc = "Lat: " + location.getLatitude() + "\n Lon: "
					+ location.getLongitude();
			Log.d("AMM", "loc: " + loc);
			Toast.makeText(MainActivity.this, loc, Toast.LENGTH_LONG).show();
		}
	};

	public void onClick(View v) {
		if (v.getId() == R.id.poi_types) {
			Toast.makeText(this, "Pretend this is a dialog", Toast.LENGTH_SHORT)
					.show();
		}

	}

    private void updatePOIandScreen()
    {
    	for (AMMItemizedOverlay type : mMapView.getAMMOverlayManager().getOverlayTypes())
    	{
    		Iterator<POI> iter = type.mOverlays.iterator();
    		while (iter.hasNext())
    		{
    			POI testPoint = iter.next();
    			if (testPoint.getUID() >= 0)
    			{
    				iter.remove();
    			}
    		}

        	for (POI point : Server.getInstance().POIelements.values())
        	{
        		if (point.getType().equalsIgnoreCase(type.getName()))
        		{
        			type.addOverlay(point);
        		}
        		if (point.getUID() == UID_to_track)
        		{
        			mMapView.getController().setCenter(point.getGeoPoint());
        		}
        	}
    	}
    	
    	this.mMapView.invalidate();
    }
	
}
