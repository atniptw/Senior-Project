package edu.rosehulman.androidmovingmap;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import edu.rosehulman.maps.OSMMapView;

public class MainActivity extends Activity {
	
	private OSMMapView mMapView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
    	return super.onOptionsItemSelected(item);
    }
}
