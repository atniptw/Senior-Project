package edu.rosehulman.androidmovingmap;

import java.io.Serializable;
import java.util.List;

import org.osmdroid.util.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.rosehulman.overlays.OverlayIconRegistry;

public class AddPOIActivity extends Activity implements Serializable {
	public static final int NEW_POI_REQUEST = 1;
	public static final String KEY_GEOPOINT = "KEY_GEOPOINT";
	public static final String KEY_POI_NAME = "KEY_POI_NAME";
	public static final String KEY_POI_TYPE = "KEY_POI_TYPE";
	public static final String KEY_POI_DESCR = "KEY_POI_DESCR";
	public static final String KEY_OVERLAY_TYPES = "KEY_OVERLAY_TYPES";

	private GeoPoint mGeoPoint;

	private Spinner mTypeSpinner;
	private EditText mPoiName;
	private EditText mDescription;

	private List<String> poiTypeChoices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_poi);

		mPoiName = (EditText) findViewById(R.id.poi_name);
		mDescription = (EditText) findViewById(R.id.poi_descr);
		mGeoPoint = (GeoPoint) getIntent().getParcelableExtra(KEY_GEOPOINT);

		poiTypeChoices = OverlayIconRegistry.getInstance()
				.getRegisteredOverlays();
		mTypeSpinner = (Spinner) findViewById(R.id.poi_type_spinner);
		OverlayListAdapter typeSpinnerAdapter = new OverlayListAdapter(this,
				poiTypeChoices);
		mTypeSpinner.setAdapter(typeSpinnerAdapter);

		Button confirmButton = (Button) findViewById(R.id.add_poi_create_btn);
		confirmButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent result = new Intent();

				result.putExtra(KEY_POI_NAME, mPoiName.getText().toString());
				result.putExtra(KEY_GEOPOINT, (Serializable) mGeoPoint);
				result.putExtra(KEY_POI_TYPE, getPOIType());
				result.putExtra(KEY_POI_DESCR, mDescription.getText()
						.toString());

				setResult(RESULT_OK, result);
				finish();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.add_poi_cancel_btn);
		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent result = new Intent();
				setResult(RESULT_CANCELED, result);
				finish();

			}
		});

	}

	private String getPOIType() {
		int pos = mTypeSpinner.getSelectedItemPosition();
		return poiTypeChoices.get(pos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_poi, menu);
		return true;
	}
}
