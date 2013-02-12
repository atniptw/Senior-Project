package edu.rosehulman.androidmovingmap;

import java.io.Serializable;

import org.osmdroid.util.GeoPoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class AddPOIActivity extends Activity implements Serializable {
	public static final String KEY_GEOPOINT = "KEY_GEOPOINT";
	public static final String KEY_OVERLAY_TYPES = "KEY_OVERLAY_TYPES";
	
	private GeoPoint mGeoPoint;
	private String mPoiTypeName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poi);
        
        mGeoPoint = (GeoPoint) getIntent().getParcelableExtra(KEY_GEOPOINT);
        
        ImageButton poiTypeButton = (ImageButton) findViewById(R.id.poi_type_btn);
        poiTypeButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				PoiTypeListDialog dialog = PoiTypeListDialog.newInstance(AddPOIActivity.this);
				dialog.show(getFragmentManager(), "dialog");
			}
		});
        
        Button confirmButton = (Button) findViewById(R.id.add_poi_create_btn);
        confirmButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        	}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_poi, menu);
        return true;
    }
}
