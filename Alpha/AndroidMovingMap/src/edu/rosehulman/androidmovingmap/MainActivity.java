package edu.rosehulman.androidmovingmap;

import java.io.Serializable;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.rosehulman.maps.OSMMapView;
import edu.rosehulman.overlays.AddPOITypeDialogFragment;

public class MainActivity extends Activity implements OnClickListener, Serializable {
	
	private OSMMapView mMapView;
	private Button mPOITypeButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mMapView = (OSMMapView)findViewById(R.id.map_view);
        mMapView.setClickable(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        
        mPOITypeButton = (Button)findViewById(R.id.poi_types);
        mPOITypeButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.add_poi_type) {
    		AddPOITypeDialogFragment.newInstance(mMapView.getAMMOverlayManager(), this).show(getFragmentManager(), "lol");
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

	public void onClick(View v) {
		if (v.getId() == R.id.poi_types) {
			Toast.makeText(this, "Pretend this is a dialog", Toast.LENGTH_SHORT).show();
		}
	}
}
