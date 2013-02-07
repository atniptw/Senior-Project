package edu.rosehulman.androidmovingmap;

import java.util.List;

import org.osmdroid.util.GeoPoint;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import edu.rosehulman.overlays.AMMItemizedOverlay;

public class AddPOIActivity extends Activity {
	public static final String KEY_GEOPOINT = "KEY_GEOPOINT";
	public static final String KEY_OVERLAY_TYPES = "KEY_OVERLAY_TYPES";
	
	private GeoPoint mGeoPoint;
	private List<AMMItemizedOverlay> mOverlayTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poi);
        
        mGeoPoint = (GeoPoint) getIntent().getParcelableExtra(KEY_GEOPOINT);
        
        ImageButton poiTypeButton = (ImageButton) findViewById(R.id.poi_type_btn);
        poiTypeButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			}
		});
        
        Button confirmButton = (Button) findViewById(R.id.add_poi_create_btn);
        confirmButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		
        	}
        });
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_poi, menu);
        return true;
    }
}
