package edu.rosehulman.androidmovingmap;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import edu.rosehulman.maps.OSMMapView;

public class MainActivity extends FragmentActivity {

	private OSMMapView mMapView;
	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int refreshGPStime = 1000;
		int refreshGPSdistance = 10;

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider provider = locationManager
				.getProvider(LocationManager.GPS_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				refreshGPStime, refreshGPSdistance, listener);

		mMapView = new OSMMapView(this, 256);
		mMapView.setClickable(true);
		mMapView.setMultiTouchControls(true);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setTileSource(TileSourceFactory.MAPNIK);

		setContentView(mMapView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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
			new EnableGpsDialogFragment().show(getSupportFragmentManager(),
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

}
