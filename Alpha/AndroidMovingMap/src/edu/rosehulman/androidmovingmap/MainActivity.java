package edu.rosehulman.androidmovingmap;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import edu.rosehulman.maps.OSMMapView;
import edu.rosehulman.overlays.AddOverlayDialogFragment;
import edu.rosehulman.overlays.OverlayManager;

public class MainActivity extends Activity {
	
	private OverlayManager mOverlayManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MapView mapView = new OSMMapView(this, 256);
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        
        mOverlayManager = new OverlayManager(this);
        
        setContentView(mapView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.add_poi:
    		DialogFragment addOverlayFragment = new AddOverlayDialogFragment(mOverlayManager, this);
    		addOverlayFragment.show(getFragmentManager(), "dialog");
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
}
