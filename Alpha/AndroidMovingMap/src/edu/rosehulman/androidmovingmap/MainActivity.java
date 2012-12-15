package edu.rosehulman.androidmovingmap;

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

public class MainActivity extends Activity implements OnClickListener {
	
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

	public void onClick(View v) {
		if (v.getId() == R.id.poi_types) {
			Toast.makeText(this, "Pretend this is a dialog", Toast.LENGTH_SHORT).show();
		}
	}
}
