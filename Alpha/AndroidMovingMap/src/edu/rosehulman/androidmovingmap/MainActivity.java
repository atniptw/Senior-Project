package edu.rosehulman.androidmovingmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import edu.rosehulman.overlays.AMMOverlayManager;
import edu.rosehulman.overlays.AddOverlayDialogFragment;
import edu.rosehulman.overlays.AddPOITypeDialogFragment;
import edu.rosehulman.server.POI;
import edu.rosehulman.server.Server;

public class MainActivity extends Activity implements OnClickListener,
		Serializable {

	private OSMMapView mMapView;
	private LocationManager locationManager;
	private Button mPOITypeButton;

	private Server server = new Server();
	private Server.ListenPOISocket POIThread = null;
	private String server_poi_name = "POI_from_server";

	private XYTileSource tileSource;
	private String mapSourcePrefix = "http://137.112.156.177/~king/testMessage/";
	private ArrayList<String> mapSourceNames = new ArrayList<String>(Arrays.asList("map2/", "map1/"));
	private int mapSourceIndex = 0;

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

		int refreshGPStime = 1000;
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
        tileSource = new XYTileSource("local" + mapSourceIndex, null, 0, 4, 256, ".png",
        		mapSourcePrefix + mapSourceNames.get(mapSourceIndex));
        mMapView.setTileSource(tileSource);

		mPOITypeButton = (Button) findViewById(R.id.poi_types);
		mPOITypeButton.setOnClickListener(this);

    	mMapView.getAMMOverlayManager().addCustomOverlay(server_poi_name);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_poi_type:
			Toast.makeText(this, "I can totally add POI types",
					Toast.LENGTH_SHORT).show();
			AddPOITypeDialogFragment.newInstance(
					mMapView.getAMMOverlayManager(), this).show(
					getFragmentManager(), "lol");
			return true;

		case R.id.menu_settings:
			Intent intent = new Intent(this, Preferences.class);
			startActivity(intent);
			return true;
        
		case R.id.menu_cycle_map_type:
        	mapSourceIndex = (mapSourceIndex + 1) % mapSourceNames.size();
        	tileSource = new XYTileSource("local" + mapSourceIndex, null, 0, 4, 256, ".png",
            		mapSourcePrefix + mapSourceNames.get(mapSourceIndex));
        	Log.d("menu cycle", "pathBase: " + tileSource.getTileURLString(new MapTile(0,0,0)));
        	mMapView.setTileSource(tileSource);
            mMapView.invalidate();
            return true;

        case R.id.menu_start_stop_sync:
        	if (this.POIThread == null)
        	{
        		this.POIThread = this.server.new ListenPOISocket();
        		this.POIThread.updatePOIHandler = this.invalidateDisplay;
        		this.POIThread.start();
        	} else
        	{
            	this.POIThread.stopThread = true;
            	this.POIThread = null;
        	}
//        	this.invalidateOptionsMenu();
        	return true;
 
        case R.id.menu_display:
        	this.updatePOIandScreen();
        	return true;

        case R.id.menu_quit:
        	if (this.POIThread != null)
        	{
        		this.POIThread.stopThread = true;
        		Log.d("test", "setting closeThread");
        		try {
        			this.POIThread.join();
        			Log.d("test", "joined");
        		} catch (InterruptedException e) {}
        	}
        	// TODO I do not know how to close cleanly
        	android.os.Process.killProcess(android.os.Process.myPid());
        	return true;
		
        default:
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

	// TODO REVIEW THIS SOMEONE NOT SETH
    private void updatePOIandScreen()
    {
    	AMMItemizedOverlay server_poi_type = null;
    	for (AMMItemizedOverlay type : mMapView.getAMMOverlayManager().getOverlayTypes())
    	{
    		if (type.getName() == server_poi_name)
    		{
    			server_poi_type = type;
    		}
    	}
    	server_poi_type.mOverlays.clear();
	
    	for (POI point : Server.POIelements.values())
    	{
			server_poi_type.addOverlay(new OverlayItem("lol", "teehee", point.getGeoPoint()));
    	}
    	this.mMapView.invalidate();
    }
	
}
