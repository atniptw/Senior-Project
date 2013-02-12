package edu.rosehulman.androidmovingmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.osmdroid.util.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.rosehulman.overlays.OverlayIconRegistry;
import edu.rosehulman.server.POI;

public class AddPOIActivity extends Activity implements Serializable {
	public static final int NEW_POI_REQUEST = 1;
	public static final String KEY_GEOPOINT = "KEY_GEOPOINT";
	public static final String KEY_POI_NAME = "KEY_POI_NAME";
	public static final String KEY_POI_TYPE = "KEY_POI_TYPE";
	public static final String KEY_POI_DESCR = "KEY_POI_DESCR";
	public static final String KEY_OVERLAY_TYPES = "KEY_OVERLAY_TYPES";
	
	private GeoPoint mGeoPoint;
	
	private TextView mTypeName;
	private ImageButton mTypeIcon;
	
	private EditText mPoiName;
	private EditText mDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poi);
        
        mTypeName = (TextView)findViewById(R.id.poi_type_name);
        mPoiName = (EditText)findViewById(R.id.poi_name);
        mDescription = (EditText)findViewById(R.id.poi_descr);
        
        mGeoPoint = (GeoPoint) getIntent().getParcelableExtra(KEY_GEOPOINT);
        
        mTypeIcon = (ImageButton) findViewById(R.id.poi_type_btn);
        mTypeIcon.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				PoiTypeListDialog dialog = PoiTypeListDialog.newInstance(AddPOIActivity.this);
				dialog.show(getFragmentManager(), "dialog");
			}
		});
        
        Button confirmButton = (Button) findViewById(R.id.add_poi_create_btn);
        confirmButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent result = new Intent();
        		
        		result.putExtra(KEY_POI_NAME, mPoiName.getText().toString());
        		result.putExtra(KEY_GEOPOINT, (Serializable)mGeoPoint);
        		result.putExtra(KEY_POI_TYPE, mTypeName.getText().toString());
        		result.putExtra(KEY_POI_DESCR, mDescription.getText().toString());
        		
        		setResult(RESULT_OK, result);
        		finish();
        	}
        });
        
    }
    
    public void setPOIType(String name) {
    	mTypeName.setText(name);
    	mTypeIcon.setImageDrawable(OverlayIconRegistry.getInstance().getIcon(name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_poi, menu);
        return true;
    }
}
