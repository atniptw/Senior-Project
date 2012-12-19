package edu.rosehulman.androidmovingmap;

import java.io.Serializable;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

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
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.rosehulman.maps.OSMMapView;
import edu.rosehulman.overlays.AddPOITypeDialogFragment;

public class MainActivity extends Activity implements OnClickListener,
		Serializable {

	private OSMMapView mMapView;
	private LocationManager locationManager;
	private Button mPOITypeButton;

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
		mMapView.setTileSource(TileSourceFactory.MAPNIK);

		mPOITypeButton = (Button) findViewById(R.id.poi_types);
		mPOITypeButton.setOnClickListener(this);

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

}
